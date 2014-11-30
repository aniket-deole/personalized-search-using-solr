package org.zeppelin.p3.personalization;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class UserLog implements JSONAware {

	private Integer userId;
	private String docID;
	private Integer likingScore;
	private Integer clickCount;

	public UserLog() {

	}

	/**
	 * @return the id
	 */
	public String getCategory() {
		return docID;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setCategory(String category) {
		this.docID = category;
	}

	/**
	 * @return the title
	 */
	public Integer getLikingScore() {
		return likingScore;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setLikingScore(Integer likingScore) {
		this.likingScore = likingScore;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"" + JSONObject.escape("userId") + "\"");
		sb.append(":");
		sb.append(userId);

		sb.append(",");

		sb.append("\"" + JSONObject.escape("docID") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(docID) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("likingScore") + "\"");
		sb.append(":");
		sb.append(likingScore);

		sb.append(",");

		sb.append("\"" + JSONObject.escape("clickCount") + "\"");
		sb.append(":");
		sb.append(clickCount);
		sb.append("}");

		return sb.toString();
	}

}
