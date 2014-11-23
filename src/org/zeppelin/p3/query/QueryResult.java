package org.zeppelin.p3.query;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class QueryResult implements JSONAware{

	private String title;
	private String content;
	private String snippet;
	private Integer rating;
	private String category;
	private String source;
	
	public QueryResult (String title, String content, String snippet,
			Integer rating, String category, String source) {
		this.title = title;
		this.content = content;
		this.snippet = snippet;
		this.rating = rating;
		this.category = category;
		this.source = source;
	}
	
	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer ();
		sb.append ("{");

		sb.append ("\"" + JSONObject.escape("title") + "\"");
		sb.append (":");
		sb.append ("\"" + JSONObject.escape(title) + "\"");
		
		sb.append (",");
		
		sb.append ("\"" + JSONObject.escape("content") + "\"");
		sb.append (":");
		sb.append ("\"" + JSONObject.escape(content) + "\"");
		
		sb.append (",");
		
		sb.append ("\"" + JSONObject.escape("snippet") + "\"");
		sb.append (":");
		sb.append ("\"" + JSONObject.escape(snippet) + "\"");
		
		sb.append (",");
		
		sb.append ("\"" + JSONObject.escape("rating") + "\"");
		sb.append (":");
		sb.append (rating);
		
		sb.append (",");
		
		sb.append ("\"" + JSONObject.escape("category") + "\"");
		sb.append (":");
		sb.append ("\"" + JSONObject.escape(category) + "\"");
		
		sb.append (",");
		
		sb.append ("\"" + JSONObject.escape("source") + "\"");
		sb.append (":");
		sb.append ("\"" + JSONObject.escape(source) + "\"");

		sb.append ("}");
		
		return sb.toString();
	}

}
