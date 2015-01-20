package cooktalk;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;

//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.usermodel.Range;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class WordFileManager{
	private static WordprocessingMLPackage wordMLPackage = null;
	//private static WordprocessingMLPackage wordMLPackage = null;
	
	
	public static void main(String[] args){
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=1;i<args.length;i++)
			wordMLPackage.getMainDocumentPart().addParagraphOfText(args[i]);
		try {
			wordMLPackage.save(new java.io.File(args[0]));
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create
	}
	/*private HWPFDocument doc = null;
	public void initWordFile(File file) throws IOException{
		if(file.exists())
		{
		FileInputStream stream = new FileInputStream(file);
		doc=new HWPFDocument(stream);
		}
		
	}
	public String readFullText()
	{
		return doc.getRange().text();
	}
	
	public void writeWordFile(String text,File filename){
		OutputStream out = null;
		//WordExtractor extractor= null;
		try {
			//extractor= new WordExtractor(doc);
			if(filename.exists())
			{
			Range range = doc.getRange();
			
			
			range.insertBefore(text);
			}
			
			
			out= new FileOutputStream(filename);
			doc.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally
		{
			try {
				out.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}
		}
	}*/
}