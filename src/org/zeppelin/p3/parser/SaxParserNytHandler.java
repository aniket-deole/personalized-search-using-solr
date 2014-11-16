package org.zeppelin.p3.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.zeppelin.p3.common.CommonConstants;

public class SaxParserNytHandler extends DefaultHandler{
	
	Boolean isBlockFullText = false;
	Boolean isContent = false;
	Boolean isTitle = false;
	Boolean isAuthor = false;
	
	StringBuilder content = new StringBuilder();
	StringBuilder title = new StringBuilder();
	StringBuilder author = new StringBuilder();
	
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
		  System.out.println("\nContent :: "+content);
		  System.out.println("\nTitle :: "+title);
		  System.out.println("\nAuthor :: "+author);
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
		}
		
		else if(qName.equals("hl1")){
			isTitle = true;
		}
		
		else if(qName.equals("person")){
			isAuthor = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("block")){
               isBlockFullText = false;
		}
		else if(isBlockFullText && qName.equals("p")){
			isContent = false;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	   if(isContent){
		   StringBuilder s = new StringBuilder(new String(ch, start, length));
		   content.append(s).append(CommonConstants.WHITESPACE);
		   isContent = false;
	   }
	   
	   else if(isTitle){
		   StringBuilder s = new StringBuilder(new String(ch, start, length));
		   title.append(s);
		   isTitle = false;
	   }
	   
	   else if(isAuthor){
		   StringBuilder s = new StringBuilder(new String(ch, start, length));
		   author.append(s);
		   isAuthor = false;
	   }
	}
}
