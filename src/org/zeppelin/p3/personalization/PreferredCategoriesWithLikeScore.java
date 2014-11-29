package org.zeppelin.p3.personalization;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class PreferredCategoriesWithLikeScore implements JSONAware {
	
	private String category;
	private Integer likingScore;

	public PreferredCategoriesWithLikeScore() {

	}

	/**
	 * @return the id
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setCategory(String category) {
		this.category = category;
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

	public PreferredCategoriesWithLikeScore(String category, Integer likingScore) {
		this.category = category;
		this.likingScore = likingScore;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"" + JSONObject.escape("category") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(category) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("likingScore") + "\"");
		sb.append(":");
		sb.append(likingScore);

		sb.append("}");

		return sb.toString();
	}
}
