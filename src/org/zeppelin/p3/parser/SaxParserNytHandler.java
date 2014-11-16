package org.zeppelin.p3.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParserNytHandler extends DefaultHandler{
	
	Boolean isBlockFullText = false;
	Boolean isContent = false;
	
	public void parseDocument(String fileName){
	  SAXParserFactory factory = SAXParserFactory.newInstance();
	  SAXParser parser;
			try {
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				parser = factory.newSAXParser();
			    parser.parse(new File(fileName), this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    }
	
	@Override
	public void startDocument() throws SAXException {
          System.out.println("\nStart document");
	}
	
	@Override
	public void endDocument() throws SAXException {
          System.out.println("\nEnded document");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(qName.equals("block")){
			String cla=attributes.getValue("class");
			if((cla).equals("full_text")){
				isBlockFullText = true;
			}
		}
		
		else if(qName.equals("p") && isBlockFullText){
			isContent = true;
			isBlockFullText = false;	
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//System.out.println("\nEnd Element "+qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
       //System.out.println("\nCharacters");
	   if(isContent){
		   String s = new String(ch, start, length);
		   System.out.println("\nContent :"+s);
		   isContent = false;
	   }
	}
}
