package org.zeppelin.p3.personalization;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class PreferredSourceWithCheckValue implements JSONAware {

	private Integer userId;
	private String source;
	private Boolean checked;

	public PreferredSourceWithCheckValue() {

	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"" + JSONObject.escape("userId") + "\"");
		sb.append(":");
		sb.append(userId);

		sb.append(",");

		sb.append("\"" + JSONObject.escape("name") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(source) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("check") + "\"");
		sb.append(":");
		sb.append(checked);

		sb.append("}");

		return sb.toString();
	}

}
