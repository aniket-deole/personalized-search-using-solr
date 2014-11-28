package org.zeppelin.p3.personalization;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class User implements JSONAware {
	public String name;
	public Integer id;
	
	public User (Integer id, String name) {
		this.name = name;
		this.id = id;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		sb.append("\"" + JSONObject.escape("id") + "\"");
		sb.append(":");
		sb.append(id);

		sb.append(",");
		
		sb.append("\"" + JSONObject.escape("name") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(name) + "\"");

		sb.append("}");

		return sb.toString();
	}
	
}
