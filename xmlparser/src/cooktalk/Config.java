package cooktalk;

import java.io.File;
import java.io.FileInputStream;

import org.jdom2.Element;





public class Config {
	//public static String serverIp = "http://localhost:8080";
	public static String serverIp = "http://163.239.27.42:8080";
	FileInputStream fis = null;
	File file = new File("source/20141015_093128_김슬기_F29_log.xml");
	public static void setupConfig() {
		
		XmlParser config = new XmlParser("source/20141015_093128_김슬기_F29_log.xml");
		String[] s = config.getText("turn");
		Element turnin= config.getXmlElement("turn");
		//turnlist = config.getElement("turn");
		if (s != null) {
			serverIp = s[0];
		}
	//	Element result= turnin("act_tag");
		System.out.println(s[1]);
		System.out.println(turnin);
	}
}
