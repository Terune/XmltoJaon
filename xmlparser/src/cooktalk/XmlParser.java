package cooktalk;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlParser {

	Document mDoc;
	Element mRoot;
	//---------- constructors ---------------------------
	public XmlParser(String xmlfilename) {
		mRoot = null;
		try {
			File xmlFile = new File(xmlfilename);
			FileInputStream in = new FileInputStream(xmlFile);
			SAXBuilder builder = new SAXBuilder();
			mDoc = builder.build(in);
			mRoot =  mDoc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmlParser(FileInputStream in) {
		mRoot = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			mDoc = builder.build(in);
			mRoot = mDoc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmlParser(InputStream in) {
		mRoot = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			mDoc = builder.build(in);
			mRoot = mDoc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmlParser(String x, Charset c) {
		//byte[] b = x.getBytes(c);
		mRoot = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			mDoc = builder.build(new StringReader(x));//new ByteArrayInputStream(b));
			mRoot = mDoc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmlParser(byte[] b) {
		mRoot = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			mDoc = builder.build(new ByteArrayInputStream(b));
			mRoot = mDoc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmlParser(Document d) {
		mRoot = null;
		mDoc = d;
		mRoot = mDoc.getRootElement();
	}
	
	public XmlParser(String rootname, boolean dummy) {
		mDoc = new Document();
		mDoc.setRootElement( new Element(rootname) );
		mRoot = mDoc.getRootElement();
	}

	// ---------- end of constructors --------------------
	

	//-- handy apis for item ---------- 
	public void insertElement(String name, String s_path, String ref_name) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals("") )
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				e.addContent(e1 = new Element(path[i]));
			}
			e = e1;
		}
		e.addContent(ref_name == null ? e.getContentSize() : e.indexOf(e.getChild(ref_name)), e1 = new Element(name));
	}
	
	public void removeElement(String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		int i;
		for ( i = 0 ; i < path.length-1 ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return;
			}
			e = e1;
		}
		e.removeChild(path[i]);
	}
	
	
	//--- handy apis for Attribute ------
	public String getAttribute(String name, String s_path ) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return null;
			}
			e = e1;
		}
		return e.getAttributeValue(name);
	}
	
	public boolean addAttribute(String name, String value, String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if ( path[i].equals("") )
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return false;
			}
			e = e1;
		}
		e.setAttribute(name, value);
		return true;
	}
	
	public boolean removeAttribute(String name, String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return false;
			}
			e = e1;
		}
		return e.removeAttribute(name);
	}
	
	public boolean setAttribute(String name, String value, String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				e.addContent(e1 = new Element(path[i]));
			}
			e = e1;
		}
		e.setAttribute(name, value);
		return true;
	}
	
	//--- handy apis for text array ------
	public String[] getText(String s_path, String name) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return null;
			}
			e = e1;
		}
		List<Element> l = e.getChildren(name);
		if ( l.size() > 0 ) {
			String [] s = new String [l.size()];
			int i = 0;
			for ( Element ee : l ) {
				s[i] = ee.getValue();
				i++;
			}
			return s;
		}
		List<Content> c = e.getContent();
		String [] s = new String [c.size()];
		int i = 0;
		for ( Content cc : c ) {
			s[i] = cc.getValue();
			i++;
		}
		return s;
	}
	
	public String[] getText(String s_path) {
		return getText(s_path, "item");
	}
	
	public void setText(String text, String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				e.addContent(e1 = new Element(path[i]));
			}
			e = e1;
		}
		e.setText(text);
	}
	
	public void insertText(String text, String s_path, int at) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				e.addContent(e1 = new Element(path[i]));
			}
			e = e1;
		}
		e1 = new Element("item").setText(text);
		if ( at >= 0 )
			e.addContent(at,e1);
		else
			e.addContent(e1);
	}
	
	public boolean removeText(String s_path, int at) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = e.getChild(path[i]);
			if ( null == e1 ) {
				return false;
			}
			e = e1;
		}
		e.removeContent(at);
		return true;
	}

	
	//------------ document apis ----------------
	public byte []  getXml() {
		byte [] xml = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(mDoc, out);
			xml = out.toByteArray();
			//String s = new String(protocol);
			//String s1 = s;
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return xml;
	}
	
    public Element getXmlRoot() {
    	return mRoot;
    }
    
	
	//----------- general apis ---------------
	public Element getElement(String s_path) {
		String[] path = s_path.split("/");
		Element e = mRoot;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e = e.getChild(path[i]);
		}
		return e;
	}
    
	public void putElement(Element e, String s_path) {
		String[] path = s_path.split("/");
		Element el = mRoot;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			el = el.getChild(path[i]);
		}
		el.addContent(e);
	}
	
	
	public void setElement(Element e, String s_path) {
		String[] path = s_path.split("/");
		Element el = mRoot;
		Element e1;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			e1 = el.getChild(path[i]);
			if ( null == e1 ) {
				el.addContent(e1 = new Element(path[i]));
			}
			el = e1;
		}
		el.addContent(e);
	}
	
	
	public Element getXmlElement(String s_path) {
		return getXmlElement(mRoot, s_path);
	}
	
	public Element getXmlElement(Element parent, String s_path) {
		String[] path = s_path.split("/");
		int i = 0;
		// check element path
		if ( parent == mRoot ) {
			if ( !path[0].equals(mRoot.getName()) )
				return null;
			i = 1 ;
		}
		
		for (  ; i < path.length ; i++ ) {
			Element e = parent.getChild(path[i]);
			if ( e == null )
				return null;
			parent = e;
		}
		return parent;
	}

	public Element getXmlElementNoRoot(String s_path) {
		String[] path = s_path.split("/");
		Element parent = mRoot;
		for ( int i = 0 ; i < path.length ; i++ ) {
			if (path[i].equals(""))
				continue;
			Element e = parent.getChild(path[i]);
			if ( e == null )
				return null;
			parent = e;
		}
		return parent;
	}

	public static String [] getItemArray(Element element) {
		List<Element> itemlist = element.getChildren("item");
		if ( ( itemlist == null ) || itemlist.size() == 0 )
			return null;
		int size = itemlist.size();
		String [] ret = new String [size];
		for ( int i = 0 ; i < size ; i++ )
			ret[i] = itemlist.get(i).getValue();
		return ret;
	}

	public static String [] getItemArray(Element element, String parent) {
		List<Element> itemlist = element.getChildren("item");
		if ( ( itemlist == null ) || itemlist.size() == 0 )
			return null;
		int size = itemlist.size();
		String [] ret = new String [size+1];
		ret[0] = parent;
		for ( int i = 0 ; i < size ; i++ )
			ret[i+1] = itemlist.get(i).getValue();
		return ret;
	}

	public void finalize() {
    }
	
}	
