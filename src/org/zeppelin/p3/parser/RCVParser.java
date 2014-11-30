/**
 * 
 */
package org.zeppelin.p3.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zeppelin.p3.common.CommonConstants;

/**
 * @author nikhillo Class that parses a given file into a Document
 */
public class RCVParser {

	/**
	 * Static method to parse the given file into the Document object
	 * 
	 * @param filename
	 *            : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException
	 *             In case any error occurs during parsing
	 */
	public static Document parseDocument(String filename) throws ParserException {
		// TODO YOU MUST IMPLEMENT THIS
		// System.out.println("\nFileName:: " + filename);
		if (filename == null) {
			throw new ParserException();
		} else if (filename.isEmpty()) {
			throw new ParserException();
		}
		BufferedReader reader;
		// create the Document object
		Document document = new Document();
		try {
			int snippetLineCnt = 0;
			// variables to hold the "Document" attributes
			String title, place = "", newsDate, content = "",snippet="";

			// flags to hold if the fields have been populated or not
			boolean hasAuthor = false;
			boolean hasTitle = false;
			boolean hasPlaceDate = false;
			// handle NULL file

			// Read the file contents into a buffer
			reader = new BufferedReader(new FileReader(filename));

			// line buffer to read a line once at a time
			String line;

			// populate Field Id and Category
			populateFileIdCat(document, filename);

			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0) {
					// Populate the title
					if (!hasTitle) {
						title = line.trim();
						document.setTitle(title);
						hasTitle = true;

					} else if (line.contains("<AUTHOR>")
							|| line.contains("<author>")) {
						fetchAndSetAuthorDetails(document, line.trim());
						hasAuthor = true;
					} else if (!hasPlaceDate) {
						snippet = "\n"+line;
						snippetLineCnt++;
						String placeDateContent[] = line.split("-");
						if (placeDateContent != null
								&& placeDateContent.length > 1) {
							content = content
									+ fetchAndSetPlaceAndDate(document,
											placeDateContent, content);
						}
						hasPlaceDate = true;
					} else {
						// Whenever adding a line, add a space between two lines
						// so that last word and first word of next line don't
						// combine.
						content = content + " " + line;
						if(snippetLineCnt<3){
							snippet = snippet +"\n"+line;
							snippetLineCnt++;
						}
					}

				}
				content = content.trim();
				document.setContent(content);
				document.setSource("Reuters");
				document.setSnippet(snippet);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found ::" + filename);
			throw new ParserException();
		} catch (IOException e) {
			System.out.println("I/O Error occured while reding the file ::"
					+ filename);
			throw new ParserException();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * This method fetches place and date from the content and sets it in the
	 * document object
	 * 
	 * @param docObj
	 * @param content
	 */
	private static String fetchAndSetPlaceAndDate(Document document,
			String[] placeDateContent, String content) {

		String place = "", newsDate = "";
		String placeDate = placeDateContent[0];
		int placeDateContLength = placeDateContent.length;
		// Populate content-If there are multiple '-' within
		// the same line where there are place and date
		for (int i = 1; i <= placeDateContLength - 1; i++) {
			content = content + placeDateContent[i];
		}
		// Populate place and date
		// Trim the string before matching so that all unwanted spaces are gone
		placeDate = placeDate.trim();
		Matcher matcher = CommonConstants.PATTERN_FOR_PLACE_DATE
				.matcher(placeDate);
		if (matcher.matches()) {
			// Find place and set it
			place = matcher.group(1);
			// Trim unwanted spaces if any
			place = place.trim();
			// It may contain a leading comma which is not a part of the place
			// If it contains leading comma trim it
			if (place.endsWith(",")) {
				// Since substring eats a word
				place = place.substring(0, place.length() - 1);
			}
			// Set the place to the document
			document.setPlace(place);
			// Find date and set it
			newsDate = matcher.group(2);
			// Trim unwanted space
			newsDate = newsDate.trim();
			// Set the Date to the document
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			/*try {
				Date date = formatter.parse(newsDate);
				document.setPublishedDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}*/
			
		}
		// This implementation fails for Places/Date like
		// "LAKE SUCCESS, N.Y. March 3"
		// String[] placeDateArr = placeDate.split(",");
		// if (placeDateArr != null && placeDateArr.length > 1) {
		// int placeDateArrLength = placeDateArr.length;
		// newsDate = placeDateArr[placeDateArrLength - 1];
		//
		// for (int i = 0; i <= placeDateArrLength - 2; i++) {
		// place = place + "," + placeDateArr[i];
		// }
		// place = place.substring(1, place.length());
		// place = place.trim();
		// document.setField(FieldNames.PLACE, place);
		// newsDate = newsDate.trim();
		// document.setField(FieldNames.NEWSDATE, newsDate);
		// }
		return content;

	}

	private static void fetchAndSetAuthorDetails(Document docObj, String content) {
		String authorName = "";
		String authorOrg = "";
		// Retrieve the content inside the Author Tag
		// Using regular expressions instead of startsWith and endsWith because
		// the author tag may or may not be case sensitive
		String regExForAuthor = "[Aa][Uu][Tt][Hh][Oo][Rr]";
		String regExForAuthorTag = "[<]" + regExForAuthor + "[>].*" + "[</]"
				+ regExForAuthor + "[>]";
		if (content.matches(regExForAuthorTag)) {
			// Since it has matched the regular expression for author, the first
			// occurrence of > will mark the start of author name and org and
			// the start of closing tag will mark the end of the value
			String authorDetails = content.substring(content.indexOf(">") + 1,
					content.indexOf("</"));
			authorDetails = authorDetails.trim();
			String[] split = authorDetails.split(",");
			// For Author Name BY/By/by may or may not exist.
			// Regex to match 0 or more occurrences of BY/By/by
			String regExBy = "By|by|BY";
			String unfilteredAuthorName = split[0];
			String[] authorNameSplit = unfilteredAuthorName.split(regExBy);
			// To avoid ArrayIndexOutofBoundException, access the arrays using
			// length of the array as it's not mandatory
			// for By and Author Org to be present.
			// Also, it might happen that the document consists "By, Author Org"
			if (authorNameSplit.length > 0) {
				authorName = authorNameSplit[authorNameSplit.length - 1];
			}
			// Author Org may or may not exist, so check before executing
			if (split.length > 1) {
				authorOrg = split[1];
			}
		}
		docObj.setAuthor(authorName.trim());
		//docObj.setField(FieldNames.AUTHORORG, authorOrg.trim());
		/*
		 * System.out.println("Author Name ::" +
		 * docObj.getField(FieldNames.AUTHOR)[0]);
		 * System.out.println("Author Org ::" +
		 * docObj.getField(FieldNames.AUTHORORG)[0]);
		 */
	}

	/**
	 * Populate the category and file Id
	 * 
	 * @param document
	 * @param filename
	 */
	private static void populateFileIdCat(Document document, String filename) {

		String pattern = Pattern.quote(String.valueOf(File.separatorChar));
		String[] arrFolders = filename.split(pattern);
		if (arrFolders != null && arrFolders.length > 0) {
			int length = arrFolders.length;
			//document.setField(FieldNames.FILEID, arrFolders[length - 1]);
			List<String> categories = new ArrayList<String>();
			categories.add(arrFolders[length - 2]);
			document.setCategories(categories);
			// System.out.println("\nCategory ::" + arrFolders[length - 2]);
		}
	}

}
