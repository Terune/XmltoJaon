package cooktalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jdom2.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.usermodel.Range;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.json.simple.JSONValue;
//import org.json.simple.JSONArray;


public class Config {
	// public static String serverIp = "http://localhost:8080";
	// public static String serverIp = "http://163.239.27.42:8080";
	// FileInputStream fis = null;

	public static void runConfig() {
		File files[];

		files = set_filename();

		String Sessions = "";
		for (File runfile : files) {
			if (!runfile.getName().startsWith(".")) {
				Sessions += setupConfig(runfile.getName()) + "\n";
			}
		}

		try {
			FileWriter sessionFile = new FileWriter("result/con_log.sessions");

			sessionFile.write(Sessions);
			sessionFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// static String filename="source/20141015_093128_김슬기_F29_log.xml";
	// File file = new File(filename);

	static File[] set_filename() {
		File dir = new File("source/");
		File fileList[] = dir.listFiles();
		return fileList;
	}

	public static String setupConfig(String filename) {

		filename = "source/" + filename;
		XmlParser config = new XmlParser(filename);
		
		// String[] s = config.getText("turn","act_tag");
		Element root = config.getXmlRoot();
		Element turnin = config.getXmlElement(root, "talksori_log");
		JSONObject obj = new JSONObject();
		JSONArray turns_items = new JSONArray();
		JSONObject turn_thing = new JSONObject();

		String filenamecov;
		filenamecov = filename.replace(".xml", "");
		filenamecov = filenamecov.replace("source/", "");
		filenamecov = filenamecov.substring(0, 15);

		Vector<String> newContent = new Vector<String>();

		obj.put("session-id", filenamecov);
		// turnlist = config.getElement("turn");
		// if (s != null) {
		// serverIp = s[0];
		// }
		// Element result= turnin("act_tag");
		// System.out.println(s[1]);
		List<Element> itemList = turnin.getChildren("turn");
		String slot_start = "(";
		String equal = "=";
		JSONObject input_obj = new JSONObject();
		JSONObject output_obj = new JSONObject();

		int turn_idx = 0;

		for (Element turns : itemList) {
			Element useritem = turns.getChild("user");
			Element systemitem = turns.getChild("system");
			String slot = null;

			if (turn_idx != Integer.parseInt(turns.getAttributeValue("idx")) - 1) {
				turn_thing.put("output", output_obj);
				turn_thing.put("input", input_obj);
				turn_thing.put("turn-index", turn_idx);
				turn_thing.put("restart", false);

				turns_items.add(turn_thing);
				// System.out.println("object:"+obj);
				turn_thing.clear();

				turn_idx = Integer.parseInt(turns.getAttributeValue("idx")) - 1;

			}

			if (systemitem != null) {
				JSONArray dialog_act_items = new JSONArray();
				JSONObject slots = new JSONObject();
				JSONArray slotvalue = new JSONArray();

				String transcript = systemitem.getChildText("transcript");
				String all_system_result = systemitem.getChildText("act_tag");

				newContent.addElement("systemAct");
				newContent.addElement(transcript);
				newContent.addElement(all_system_result);

				System.out.println("턴:" + turns.getAttributeValue("idx"));
				// System.out.println("턴:"+turns.getAttributeValue("idx")+"결과:"+system_result);

				String[] system_results = null;

				// if(all_system_result.matches(".*,.*")){
				// System.out.println("now");
				// }
				// {
				system_results = all_system_result.split(",");
				// }
				// else
				// {
				// system_results[0] = all_system_result;
				// }

				for (String system_result : system_results) {
					system_result.trim();
					int io_slot = system_result.indexOf(slot_start);
					// if(system_result.substring(io_slot).indexOf(")")<0||io_slot<0)
					// {
					// System.out.println("error!");
					// }
					slot = system_result.substring(io_slot + 1, system_result
							.substring(io_slot).indexOf(")") + io_slot);

					if (slot.length() > 1) {
						// 슬롯용 오브젝트 생성

						String slot_value, slot_name;
						int io_slot_value = slot.indexOf(equal);

						// 슬롯 이름, 값이 각각 기록되는 쌍인 경
						if (io_slot_value > 0) {
							slot_value = slot.substring(io_slot_value + 1);
							slot_name = slot.substring(0, io_slot_value);
						}
						// request(menu)와 같이 슬롯 이름이 따로 표시되지 않는 경우
						else {
							slot_value = slot;
							slot_name = "slot";
						}
						system_result = system_result.substring(0, io_slot);
						System.out.println("act:" + system_result);
						System.out.println("슬롯이름:" + slot_name + "\t슬롯값:"
								+ slot_value);

						slotvalue.add(slot_name);
						slotvalue.add(slot_value);
						slots.put("slots", slotvalue);
						slotvalue.clear();
						
						slots.put("act", system_result);

						dialog_act_items.add(slots);
						output_obj.put("dialog_acts", dialog_act_items);
					} else {
						system_result = system_result.replace("()", "");

						slots.put("slots", slotvalue);
						int io_double_act = system_result.indexOf(",");
						// system_result.split(",");
						if (io_double_act > 0) {
							String acts[] = system_result.split(",");
							// String double_act_1 =
							// system_result.substring(0,io_double_act);
							// String double_act_2 =
							// system_result.substring(io_double_act+1);
							// if(double_act_2.length()>1){
							for (int i = 0; acts[i].length() > 1; i++) {
								System.out.println("act[" + i + "]" + acts[i]);
								slots.put("act", acts[i]);
								dialog_act_items.add(slots);
								output_obj.put("dialog_acts", dialog_act_items);
								slots.clear();

								// slots.put("slots", slotvalue);
								// slots.put("act",double_act_2);
								// dialog_act_items.add(slots);
								// output_obj.put("dialog_acts",
								// dialog_act_items);
							}
						} else {
							System.out.println("act:" + system_result);
							slots.put("act", system_result);
							dialog_act_items.add(slots);
							output_obj.put("dialog_acts", dialog_act_items);
		
						}
					}
				}
			}
			slot = null;
			if (useritem != null) {
				JSONObject slu_hyps = new JSONObject();
				JSONArray slu_hyps_items = new JSONArray();
				JSONArray slu_hyp_items = new JSONArray();
				JSONObject live = new JSONObject();
				JSONObject slots = new JSONObject();
				JSONArray slotvalue = new JSONArray();
				JSONArray slot_items = new JSONArray();
				// Element turnin3= turnin2.getChild("system");
				// Element turnin4= turnin3.getChild("act_tag");
				String all_user_result = useritem.getChildText("act_tag");

				// String[] slot = user_result.split("(");
				// System.out.println(slot[0]);
				String transcript = useritem.getChildText("transcript");
				newContent.addElement("userAct");
				newContent.addElement(transcript);
				newContent.addElement(all_user_result);
				

				System.out.println("턴:" + turns.getAttributeValue("idx"));

				String[] user_results = null;

				user_results = all_user_result.split(",");

				for (String user_result : user_results) {
					
					user_result.trim();
					int io_slot = user_result.indexOf(slot_start);
					slot = user_result.substring(io_slot + 1, user_result
							.substring(io_slot).indexOf(")") + io_slot);
					if (slot.length() > 1) {
						String slot_value, slot_name;
						int io_slot_value = slot.indexOf(equal);
						slot_value = slot.substring(io_slot_value + 1);
						slot_name = slot.substring(0, io_slot_value);
						user_result = user_result.substring(0, io_slot);

						slotvalue.add(slot_name);
						slotvalue.add(slot_value);

						slot_items.add(slotvalue);

						slotvalue.clear();
						slots.put("slots", slot_items);
						slots.put("act", user_result);
						slot_items.clear();
						slu_hyp_items.add(slots);
						slots.clear();
						
						slu_hyps.put("slu-hyp", slu_hyp_items);
						slu_hyps.put("score", 1);
						slu_hyp_items.clear();
						slu_hyps_items.add(slu_hyps);
						slu_hyps.clear();

						
						
						System.out.println("act:" + user_result);
						System.out.println("슬롯이름:" + slot_name + "\t슬롯값:"
								+ slot_value);
						
						
					} else {
						user_result = user_result.replace("()", "");

						slots.put("slots", slotvalue);

						int io_double_act = user_result.indexOf(",");
						if (io_double_act > 0) {
							String double_act_1 = user_result.substring(0,
									io_double_act);
							String double_act_2 = user_result
									.substring(io_double_act + 1);
							if (double_act_2.length() > 1) {
								System.out.println("act1:" + double_act_1
										+ " act2:" + double_act_2);
								slots.put("act", double_act_1);
								slu_hyp_items.add(slots);
								slu_hyps.put("slu-hyp", slu_hyp_items);
								slu_hyps.put("score", 1);

								slu_hyps_items.add(slu_hyps);

								live.put("slu-hyps", slu_hyps_items);
								input_obj.put("live", live);

								slots.clear();

								slots.put("slots", slotvalue);
								slots.put("act", double_act_2);
								slu_hyp_items.add(slots);
								slots.clear();
								
								slu_hyps.put("slu-hyp", slu_hyp_items);
								slu_hyp_items.clear();
								slu_hyps.put("score", 1);

								slu_hyps_items.add(slu_hyps);
								slu_hyps.clear();
								

								
							}
						} else {
							System.out.println("act:" + user_result);
							slots.put("act", user_result);
							slu_hyp_items.add(slots);
							slots.clear();
							
							slu_hyps.put("slu-hyp", slu_hyp_items);
							slu_hyp_items.clear();
							
							slu_hyps.put("score", 1);

							slu_hyps_items.add(slu_hyps);
							slu_hyps.clear();

						
						}
					}
				}
				live.put("slu-hyps", slu_hyps_items);
				input_obj.put("live", live);
			}
		}

		turn_thing.put("output", output_obj);
		turn_thing.put("input", input_obj);
		turn_thing.put("turn-index", turn_idx);
		turn_thing.put("restart", false);

		turns_items.add(turn_thing);
		obj.put("turns", turns_items);
		System.out.println("object:" + obj);
		// System.out.println(turnin);
		String mkFolder = "result/" + filenamecov;
		try {

			File desti = new File(mkFolder);
			if (!desti.exists()) {
				desti.mkdir();
			}
			FileWriter out_file = new FileWriter("result/" + filenamecov
					+ "/log.json");
			FileWriter out_file2 = new FileWriter("result/" + filenamecov
					+ "/dstc.log.json");

			out_file2.write(obj.toString());
			out_file2.flush();
			out_file2.close();

			out_file.write(obj.toString());
			out_file.flush();
			out_file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 새로운 엑셀 워크 시트 생성
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();

		
		//String[] results = null;
		//fs.main(null);
		// 해당 시트의 행별로 결과값 입력
		//File outfile=new File("result/loglist/"+filenamecov+".docx");
		List<String> list = new ArrayList<String>();
		list.add("result/loglist/"+filenamecov+".docx");
		Row namerow = sheet.createRow(0);

		Cell c1 = namerow.createCell(0);
		c1.setCellValue("System Act");
		Cell c2 = namerow.createCell(1);
		c2.setCellValue("user Act");

		int line = 0;
		for (int rownum = 1; rownum < newContent.size() + 1
				&& line < newContent.size(); rownum = rownum + 2) {
			Row r1 = sheet.createRow(rownum);
			Row r2 = sheet.createRow(rownum + 1);
			// 열별로 나눔 구분자 '\t'
			// String[] str = newContent.get(rownum).split("	");
			if (newContent.elementAt(line) == "systemAct") {
				Cell tran = r1.createCell(0);
				// tran.setCellStyle(arg0)
				tran.setCellValue(newContent.elementAt(++line));
				list.add("System:"+newContent.elementAt(line));
				Cell tag = r2.createCell(0);
				tag.setCellValue(newContent.elementAt(++line));
				list.add("Act:"+newContent.elementAt(line));
				line++;
				/*
				 * for (int cellnum = 0; cellnum < str.length; cellnum++) { Cell
				 * c = r.createCell(cellnum);
				 * 
				 * c.setCellValue(str[cellnum]); }
				 */

			} else if (newContent.elementAt(line) == "userAct") {
				Cell tran = r1.createCell(1);
				tran.setCellValue(newContent.elementAt(++line));
				list.add("User:"+newContent.elementAt(line));
				
				Cell tag = r2.createCell(1);
				tag.setCellValue(newContent.elementAt(++line));
				list.add("Act: "+newContent.elementAt(line));
				line++;
			}
			for (int i = 0; i < 3; i++) {
				sheet.autoSizeColumn((short) i);
				sheet.setColumnWidth(i, (sheet.getColumnWidth(i))); // 윗줄만으로는
																	// 컬럼의 width
																	// 가 부족하여 더
																	// 늘려야 함.
			}

		}
		String results[]={};
		results=list.toArray(new String[list.size()]);
		WordFileManager fs=new WordFileManager();
		fs.main(results);		
		
		//fs.initWordFile()
		//File outfile=new File("result/loglist/"+filenamecov+".doc");
		//if(!outfile.exists())
		//{
			
	//	}
		
		/*try {
			fs.initWordFile(outfile);
			//fs.initWordFile(new File("doc2.doc"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fs.writeWordFile("test",outfile);
		*/
		/*POIFSFileSystem fs = new POIFSFileSystem();
		HWPFDocument doc=null;
		try {
			doc = new HWPFDocument(fs);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Range range= doc.getRange();
		int wordline=0;
		range.insertBefore(newContent.elementAt(wordline));
		try{
			FileOutputStream fos=new FileOutputStream("result/loglist/"+filenamecov+".word");
			
			try {
				doc.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
		*/
		
		// 해당 워크시트를 저장함.
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream("result/loglist/" + filenamecov
					+ ".xlsx");
			wb.write(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mkFolder;

	}

}
