package org.zeppelin.p3.parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.zeppelin.p3.common.CommonConstants;


public class WikiNewsSaxParsers extends DefaultHandler {

	Boolean isPage = false;
	Boolean isTitle = false;
	Boolean isContent = false;
	
	List<String> categories; // = new ArrayList<String>();
	String content;
	String title;

	
	List<Document> docList = new ArrayList<Document> ();
	Document document = new Document();

	public List<Document> parseDocument(String fileName) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
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
		return docList;
	}

	@Override
	public void startDocument() throws SAXException {
		// System.out.println("\nStart document");
		content = "";
		title = "";
		categories = new ArrayList<String> ();
		document = new Document ();
	}

	@Override
	public void endDocument() throws SAXException {
		// System.out.println("\nContent :: "+content);
		// System.out.println("\nTitle :: "+title);
		// System.out.println("\nAuthor :: "+author);
		// System.out.println("\nSource :: "+source);
		// System.out.println("\nCategories :: ");
		// for(String cat:categories){
		// System.out.println(cat);
		// }
		// System.out.println("\nTags :: "+tags.size());
		// for(String tag:tags){
		// System.out.println(tag);
		// }
		// System.out.println("\nEnded document");
		//document.setSource(CommonConstants.SRC_WIKI);

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if (qName.equals("page")) {
			isPage = true;
		} else if (qName.equals("title") && isPage) {
			isTitle = true;
			document.setTitle(title);
		} else if (qName.equals("text")) {
			isContent = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("page")) {
			document.setCategories(categories);
			document.setSource(CommonConstants.SRC_WIKI);
			docList.add(document);
			isPage = false;
		} else if (qName.equals ("title")) {
			isTitle = false;
			document.setContent(content);
		} else if (qName.equals ("text")) {
			document.setContent(content);
			isContent = false;
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (isTitle) {
			String s = new String(new String(ch, start, length));
			title = s;
			isTitle = false;
		} else if (isContent) {
			String s = new String (new String (ch, start, length));
			content = s;
			isContent = false;
		}

	}
}
