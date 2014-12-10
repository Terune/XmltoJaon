package cooktalk;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import org.json.simple.JSONValue;
//import org.json.simple.JSONArray;


public class Config {
	//public static String serverIp = "http://localhost:8080";
	//public static String serverIp = "http://163.239.27.42:8080";
	//FileInputStream fis = null;
	static String filename="source/20141015_093128_김슬기_F29_log.xml";
	File file = new File(filename);
	public static void setupConfig() {
		
		XmlParser config = new XmlParser(filename);
		//String[] s = config.getText("turn","act_tag");
		Element root = config.getXmlRoot();
		Element turnin= config.getXmlElement(root,"talksori_log");
		JSONObject obj= new JSONObject();
		JSONArray turns_items= new JSONArray();
		JSONObject turn_thing = new JSONObject();
		
		String filenamecov;
		filenamecov =filename.replace(".xml", "");
		filenamecov = filenamecov.replace("source/", "");
		
		obj.put("session-id", filenamecov);
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
		
		int turn_idx=0;
		
		for(Element turns : itemList){
			Element useritem= turns.getChild("user");
			Element systemitem = turns.getChild("system");
			String slot=null;
			
			if(turn_idx!=Integer.parseInt(turns.getAttributeValue("idx"))-1){
				turn_thing.put("output", output_obj);
				turn_thing.put("input", input_obj);
				turn_thing.put("turn-index", turn_idx);
				turn_thing.put("restart", false);
				
				turns_items.add(turn_thing);
				//System.out.println("object:"+obj);
				turn_thing.clear();
				
				turn_idx=Integer.parseInt(turns.getAttributeValue("idx"))-1;
				
			}
			
			if(systemitem!=null){
				JSONArray dialog_act_items = new JSONArray();
				JSONObject slots = new JSONObject();
				JSONArray slotvalue = new JSONArray();
				
				String system_result = systemitem.getChildText("act_tag");
				
				System.out.println("턴:"+turns.getAttributeValue("idx"));
				//System.out.println("턴:"+turns.getAttributeValue("idx")+"결과:"+system_result);
				int io_slot = system_result.indexOf(slot_start);
				slot=system_result.substring(io_slot+1,system_result.substring(io_slot).indexOf(")")+io_slot);
				
			
				
				if(slot.length()>1){
					//슬롯용 오브젝트 생성
					
					
					
					String slot_value,slot_name;
					int io_slot_value= slot.indexOf(equal);
					
					slot_value=slot.substring(io_slot_value+1);
					slot_name=slot.substring(0,io_slot_value);
					system_result=system_result.substring(0,io_slot);
					System.out.println("act:"+system_result);
					System.out.println("슬롯이름:"+slot_name+"\t슬롯값:"+slot_value);
					
					slotvalue.add(slot_name);
					slotvalue.add(slot_value);
					slots.put("slots", slotvalue);
					slots.put("act", system_result);
					
					dialog_act_items.add(slots);
					output_obj.put("dialog_acts", dialog_act_items);
				}
				else{
					system_result=system_result.replace("()", "");
					
					slots.put("slots",slotvalue);
					int io_double_act = system_result.indexOf(",");
					if(io_double_act>0)
					{
						String double_act_1 = system_result.substring(0,io_double_act);
						String double_act_2 = system_result.substring(io_double_act+1);
						if(double_act_2.length()>1){
							System.out.println("act1:"+double_act_1+" act2:"+double_act_2);
							slots.put("act",double_act_1);
							dialog_act_items.add(slots);
							output_obj.put("dialog_acts", dialog_act_items);
							slots.clear();
							
							slots.put("slots", slotvalue);
							slots.put("act",double_act_2);
							dialog_act_items.add(slots);
							output_obj.put("dialog_acts", dialog_act_items);
						}
					}
					else{
						System.out.println("act:"+system_result);
						slots.put("act",system_result);
						dialog_act_items.add(slots);
						output_obj.put("dialog_acts", dialog_act_items);
					}
				}
			}
			slot=null;
			if(useritem!=null)
			{
				JSONArray dialog_act_items = new JSONArray();
				JSONObject slots = new JSONObject();
				JSONArray slotvalue = new JSONArray();
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
					
					slotvalue.add(slot_name);
					slotvalue.add(slot_value);
					slots.put("slots", slotvalue);
					slots.put("act", user_result);
					
					dialog_act_items.add(slots);
					input_obj.put("dialog_acts", dialog_act_items);
					
					System.out.println("act:"+user_result);
					System.out.println("슬롯이름:"+slot_name+"\t슬롯값:"+slot_value);
				}
				else{
					user_result=user_result.replace("()", "");
					
					slots.put("slots",slotvalue);
					
					int io_double_act = user_result.indexOf(",");
					if(io_double_act>0)
					{
						String double_act_1 = user_result.substring(0,io_double_act);
						String double_act_2 = user_result.substring(io_double_act+1);
						if(double_act_2.length()>1){
							System.out.println("act1:"+double_act_1+" act2:"+double_act_2);
							slots.put("act",double_act_1);
							dialog_act_items.add(slots);
							input_obj.put("dialog_acts", dialog_act_items);
							slots.clear();
							
							slots.put("slots", slotvalue);
							slots.put("act",double_act_2);
							dialog_act_items.add(slots);
							input_obj.put("dialog_acts", dialog_act_items);
						}
					}
					else{
						System.out.println("act:"+user_result);
						slots.put("act",user_result);
						dialog_act_items.add(slots);
						input_obj.put("dialog_acts", dialog_act_items);
					}
				}
			}		
		}
		
		turn_thing.put("output", output_obj);
		turn_thing.put("input", input_obj);
		turn_thing.put("turn-index", turn_idx);
		turn_thing.put("restart", false);
		
		turns_items.add(turn_thing);
		obj.put("turns", turns_items);
		System.out.println("object:"+obj);
		//System.out.println(turnin);
		
		try{
			FileWriter out_file= new FileWriter("result/"+filenamecov+".json");
		
			out_file.write(obj.toString());
			out_file.flush();
			out_file.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
