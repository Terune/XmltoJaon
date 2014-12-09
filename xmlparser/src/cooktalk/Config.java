package cooktalk;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.jdom2.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Config {
	//public static String serverIp = "http://localhost:8080";
	//public static String serverIp = "http://163.239.27.42:8080";
	//FileInputStream fis = null;
	static String filename="source/20141015_093128_김슬기_F29_log.xml";
	File file = new File(filename);
	public static void setupConfig() {
		
		XmlParser config = new XmlParser("source/20141015_093128_김슬기_F29_log.xml");
		//String[] s = config.getText("turn","act_tag");
		Element root = config.getXmlRoot();
		Element turnin= config.getXmlElement(root,"talksori_log");
		JSONObject obj= new JSONObject();
		JSONArray array= new JSONArray();
		obj.put("session-id", filename);
		//turnlist = config.getElement("turn");
		//if (s != null) {
			//serverIp = s[0];
	//	}
	//	Element result= turnin("act_tag");
		//System.out.println(s[1]);
		List<Element> itemList = turnin.getChildren("turn");
		String slot_start = "(";
		String equal="=";
		JSONObject input_obj = new JSONObject();
		JSONObject output_obj= new JSONObject();
		
		for(Element turns : itemList){
			Element useritem= turns.getChild("user");
			Element systemitem = turns.getChild("system");
			String slot=null;
			if(systemitem!=null){
				JSONArray sysitems = new JSONArray();
				String system_result = systemitem.getChildText("act_tag");
				
				System.out.println("턴:"+turns.getAttributeValue("idx"));
				//System.out.println("턴:"+turns.getAttributeValue("idx")+"결과:"+system_result);
				int io_slot = system_result.indexOf(slot_start);
				slot=system_result.substring(io_slot+1,system_result.substring(io_slot).indexOf(")")+io_slot);
				if(slot.length()>1){
					String slot_value,slot_name;
					int io_slot_value= slot.indexOf(equal);
					slot_value=slot.substring(io_slot_value+1);
					slot_name=slot.substring(0,io_slot_value);
					system_result=system_result.substring(0,io_slot);
					System.out.println("act:"+system_result);
					System.out.println("슬롯이름:"+slot_name+"\t슬롯값:"+slot_value);
				}
				else{
					system_result=system_result.replace("()", "");
					int io_double_act = system_result.indexOf(",");
					if(io_double_act>0)
					{
					String double_act_1 = system_result.substring(0,io_double_act);
					String double_act_2 = system_result.substring(io_double_act+1);
					if(double_act_2.length()>1){
						System.out.println("act1:"+double_act_1+" act2:"+double_act_2);
					}
					}
					else
						System.out.println("act:"+system_result);
				}
			}
			slot=null;
			if(useritem!=null)
			{
		//	Element turnin3= turnin2.getChild("system");
			//Element turnin4= turnin3.getChild("act_tag");
				String user_result = useritem.getChildText("act_tag");
				
				//String[] slot = user_result.split("(");
				//System.out.println(slot[0]);
				
				System.out.println("턴:"+turns.getAttributeValue("idx"));
				int io_slot = user_result.indexOf(slot_start);
				slot=user_result.substring(io_slot+1,user_result.substring(io_slot).indexOf(")")+io_slot);
				if(slot.length()>1){
					String slot_value,slot_name;
					int io_slot_value= slot.indexOf(equal);
					slot_value=slot.substring(io_slot_value+1);
					slot_name=slot.substring(0,io_slot_value);
					user_result=user_result.substring(0,io_slot);
					System.out.println("act:"+user_result);
					System.out.println("슬롯이름:"+slot_name+"\t슬롯값:"+slot_value);
				}
				else{
					user_result=user_result.replace("()", "");
					int io_double_act = user_result.indexOf(",");
					if(io_double_act>0)
					{
						String double_act_1 = user_result.substring(0,io_double_act);
						String double_act_2 = user_result.substring(io_double_act+1);
						if(double_act_2.length()>1){
							System.out.println("act1:"+double_act_1+" act2:"+double_act_2);
						}
					}
					else
						System.out.println("act:"+user_result);
				}
			}		
		}
			
		//System.out.println(turnin);
	}
}
