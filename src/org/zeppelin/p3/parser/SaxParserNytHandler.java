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

public class SaxParserNytHandler extends DefaultHandler {

	Boolean isBlockFullText = false;
	Boolean isContent = false;
	Boolean isTitle = false;
	Boolean isAuthor = false;
	Boolean isTag = false;

	String content = "";
	String title = "";
	String author = "";
	String source = "";
	Date date = null;
	String place = "";
	String summary = "";
	List<String> categories; // = new ArrayList<String>();
	List<String> tags = new ArrayList<String>();

	Document document = new Document();

	public Document parseDocument(String fileName) {
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
		return document;
	}

	@Override
	public void startDocument() throws SAXException {
		// System.out.println("\nStart document");
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

		document.setAuthor(author);
		document.setCategories(categories);
		document.setContent(content);
		document.setPublishedDate(date);
		document.setPlace(place);
		document.setTags(tags);
		document.setTitle(title);
		document.setSource(source);
		document.setSummary(summary);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("block")) {
			String cla = attributes.getValue("class");
			if ((cla).equals("full_text")) {
				isBlockFullText = true;
			}
		}

		else if (qName.equals("p") && isBlockFullText) {
			isContent = true;
		}

		else if (qName.equals("title")) {
			isTitle = true;
		}

		else if (qName.equals("person")) {
			isAuthor = true;
		}

		else if (qName.equals("doc.copyright")) {
			if (attributes.getValue("holder") != null) {
				source = attributes.getValue("holder");
			}
		} else if (qName.equals("pubdata")) {
			if (attributes.getValue("date.publication") != null) {
				String dateValue = attributes.getValue("date.publication");
				// Format the date as required by the Solr Parser
				// YYYY-MM-DDThh:mm:ssZ
				String formatteddate = dateValue.substring(0, 4) + "-"
						+ dateValue.substring(4, 6) + "-"
						+ dateValue.substring(6, 11) + ":"
						+ dateValue.substring(11, 13) + ":"
						+ dateValue.substring(13, 15) + "Z";
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss'Z'");
				try {
					date = formatter.parse(formatteddate);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		} else if (qName.equals("meta")) {
			String name = attributes.getValue("name");
			if (name != null && name.equals("online_sections")) {
				String cats = attributes.getValue("content");
				if (cats != null) {
					// TODO - need to trim?
					String[] catArr = cats.split(CommonConstants.SEMICOLON);
					categories = Arrays.asList(catArr);
				}
			}
		}

		else if (qName.equals("org")) {
			isTag = true;
		}

		else if (qName.equals("classifier")) {
			String classname = attributes.getValue("class");
			String type = attributes.getValue("type");
			if (("online_producer").equals(classname)
					&& ("general_descriptor").equals(type)) {
				isTag = true;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("block")) {
			isBlockFullText = false;
		} else if (isBlockFullText && qName.equals("p")) {
			isContent = false;
		} else if (qName.equals("title")) {
			isTitle = false;
		} else if (qName.equals("classifier") && isTag) {
			isTag = false;
		}

		else if (qName.equals("org") && isTag) {
			isTag = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isContent) {
			String s = new String(new String(ch, start, length));
			content = s + CommonConstants.WHITESPACE;
			isContent = false;
		}

		else if (isTitle) {
			String s = new String(new String(ch, start, length));
			title = s;
			isTitle = false;
		}

		else if (isAuthor) {
			String s = new String(new String(ch, start, length));
			author = s;
			isAuthor = false;
		}

		else if (isTag) {
			String s = new String(ch, start, length);
			if (s != null && s.length() > 0) {
				tags.add(s);
			}
		}

	}
}
