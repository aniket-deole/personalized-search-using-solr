package org.zeppelin.p3.query;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class QueryResult implements JSONAware {

	private String id;
	private String title;
	private String content;
	private String snippet;
	private Integer rating;
	private String category;
	private String source;
	private Integer popularityScore;

	public QueryResult() {

	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		if (title != null && !title.isEmpty() && title.startsWith("[")
				&& title.endsWith("]")) {
			title = title.substring(1, title.length() - 1);
		}
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		if (content != null && !content.isEmpty() && content.startsWith("[")
				&& content.endsWith("]")) {
			content = content.substring(1, content.length() - 1);
		}
		this.content = content;
	}

	/**
	 * @return the snippet
	 */
	public String getSnippet() {
		return snippet;
	}

	/**
	 * @param snippet
	 *            the snippet to set
	 */
	public void setSnippet(String snippet) {
		if (snippet != null && !snippet.isEmpty() && snippet.startsWith("[")
				&& snippet.endsWith("]")) {
			snippet = snippet.substring(1, snippet.length() - 1);
		}
		this.snippet = snippet;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		if (category != null && !category.isEmpty() && category.startsWith("[")
				&& category.endsWith("]")) {
			category = category.substring(1, category.length() - 1);
		}
		this.category = category;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		if (source != null && !source.isEmpty() && source.startsWith("[")
				&& source.endsWith("]")) {
			source = source.substring(1, source.length() - 1);
		}
		this.source = source;
	}

	/**
	 * @return the publishedDate
	 */
	public String getPublishedDate() {
		return publishedDate;
	}

	/**
	 * @param publishedDate
	 *            the publishedDate to set
	 */
	public void setPublishedDate(String publishedDate) {
		if (publishedDate != null && !publishedDate.isEmpty()
				&& publishedDate.startsWith("[") && publishedDate.endsWith("]")) {
			publishedDate = publishedDate.substring(1,
					publishedDate.length() - 1);
		}
		this.publishedDate = publishedDate;
	}

	private String publishedDate;

	/**
	 * @return the popularityScore
	 */
	public Integer getPopularityScore() {
		return popularityScore;
	}

	/**
	 * @param popularityScore
	 *            the popularityScore to set
	 */
	public void setPopularityScore(Integer popularityScore) {
		this.popularityScore = popularityScore;
	}

	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"" + JSONObject.escape("id") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(id) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("title") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(title) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("content") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(content) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("snippet") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(snippet) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("rating") + "\"");
		sb.append(":");
		sb.append(rating);

		sb.append(",");

		sb.append("\"" + JSONObject.escape("category") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(category) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("source") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(source) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("publishedDate") + "\"");
		sb.append(":");
		sb.append("\"" + JSONObject.escape(publishedDate) + "\"");

		sb.append(",");

		sb.append("\"" + JSONObject.escape("popularityScore") + "\"");
		sb.append(":");
		sb.append(popularityScore);

		sb.append("}");

		return sb.toString();
	}

}
