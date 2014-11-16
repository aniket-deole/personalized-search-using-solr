/**
 * 
 */
package org.zeppelin.p3.parser;

import java.util.HashMap;

/**
 * @author nikhillo
 * Wrapper class that holds {@link FieldNames} to value mapping
 */
public class Document {
	//Sample implementation - you can change this if you like
	private HashMap<FieldNames, String[]> map;
	
	/**
	 * Default constructor
	 */
	public Document() {
		map = new HashMap<FieldNames, String[]>();
	}
	
	/**
	 * store the length of the file
	 */
	private Long length;
	/**
	 * stores the result snippet for the document
	 */
	private String resultSnippet;
	
	/**
	 * Method to set the field value for the given {@link FieldNames} field
	 * @param fn : The {@link FieldNames} to be set
	 * @param o : The value to be set to
	 */
	public void setField(FieldNames fn, String... o) {
		map.put(fn, o);
	}
	
	/**
	 * Method to get the field value for a given {@link FieldNames} field
	 * @param fn : The field name to query
	 * @return The associated value, null if not found
	 */
	public String[] getField(FieldNames fn) {
		return map.get(fn);
	}
	
	/**
	 * @return the length
	 */
	public Long getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(Long length) {
		this.length = length;
	}

	public String getResultSnippet() {
		return resultSnippet;
	}

	public void setResultSnippet(String resultSnippet) {
		this.resultSnippet = resultSnippet;
	}

	public String toString(){
		return new StringBuilder().append("FILEID ::").append(map.get(FieldNames.FILEID))
				.append("CATEGORY ::").append(map.get(FieldNames.CATEGORY))
				.append("TITLE ::").append(map.get(FieldNames.TITLE))
				.append("AUTHOR ::").append(map.get(FieldNames.AUTHOR))
				.append("AUTHORORG ::").append(map.get(FieldNames.AUTHORORG))
				.append("PLACE ::").append(map.get(FieldNames.PLACE))
				.append("NEWSDATE ::").append(map.get(FieldNames.NEWSDATE))
				.append("CONTENT ::").append(map.get(FieldNames.CONTENT))
				.toString();
		
	}
	
}
