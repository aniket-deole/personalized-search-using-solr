package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zeppelin.p3.common.CommonConstants;
import org.zeppelin.p3.db.MySQLAccess;
import org.zeppelin.p3.query.QueryResult;

/**
 * Servlet implementation class PersonalizedViewClass
 */
@WebServlet(urlPatterns = { "/RecommendedNews" })
public class RecommendedNews extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -707978683711984462L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		PrintWriter out = response.getWriter();

		String urlString = "http://localhost:8080/solr-4.10.2";
		SolrServer solrServer = new HttpSolrServer(urlString);

		// Fetch the Logged In userId from the session
		Integer userId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");

		// Retrieve preferred categories for the given user id
		Map<String, Integer> preferredCategories = new HashMap<String, Integer>();
		Map<String, Boolean> preferredSourcesWithCheckValue = new HashMap<String, Boolean>();
		MySQLAccess dao = new MySQLAccess();

		try {
			preferredCategories = dao
					.fetchPreferredCategoriesWithTheirLikingScores(userId);
			preferredSourcesWithCheckValue = dao
					.fetchPreferredSourcesWithCheckValue(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder queryString = new StringBuilder();

		/**
		 * Reference -
		 * http://stackoverflow.com/questions/1066589/java-iterate-through
		 * -hashmap
		 */
		Iterator it = preferredCategories.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if ((Integer) pairs.getValue() != 0) {
				queryString.append("category:").append(pairs.getKey())
						.append(CommonConstants.CARROT)
						.append(pairs.getValue());
				queryString.append(CommonConstants.WHITESPACE);
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		// Remove the documents already liked by the user

		Set<String> userDocSet = new HashSet<String>();

		try {
			List<UserLog> userLogs = dao
					.fetchLikeScoresAndClickCountsForAllDocuments(userId);
			if (userLogs != null) {
				for (UserLog userlog : userLogs) {

					String docId = userlog.getDocID();
					userDocSet.add(docId);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// create the solr query
		// http://localhost:8080/solr-4.10.2/select?q=*:*&fq=popularityScore:[1%20TO%20*]&sort=popularityScore%20desc
		SolrQuery parameters = new SolrQuery();
		parameters.setRequestHandler("/select");
		parameters.set("q", queryString.toString());

		parameters.set("fq", "popularityScore:[1 TO *]");
		parameters.set("sort", "popularityScore desc");

		System.out.println("\nRecommended News Query:: " + parameters);

		try {
			QueryResponse q_response = solrServer.query(parameters);
			SolrDocumentList list = q_response.getResults();
			JSONObject obj = new JSONObject();
			obj.put("resultCount", list.size());
			JSONArray jsonResults = new JSONArray();
			if (!list.isEmpty()) {
				obj.put("resultCount", list.size());
				for (int i = 0; i < list.size(); i++) {

					QueryResult result = new QueryResult();
					if (list.get(i).getFieldValue("id") != null) {
						result.setId(list.get(i).getFieldValue("id").toString());
					}
					if (list.get(i).getFieldValue("title") != null) {
						result.setTitle(list.get(i).getFieldValue("title")
								.toString());
					}
					if (list.get(i).getFieldValue("content") != null) {
						result.setContent(list.get(i).getFieldValue("content")
								.toString());
					}
					if (list.get(i).getFieldValue("category") != null) {
						result.setCategory(list.get(i)
								.getFieldValue("category").toString());
					}
					if (list.get(i).getFieldValue("source") != null) {
						result.setSource(list.get(i).getFieldValue("source")
								.toString());
					}
					if (list.get(i).getFieldValue("published_date") != null) {
						result.setPublishedDate(list.get(i)
								.getFieldValue("published_date").toString());
					}
					if (list.get(i).getFieldValue("snippet") != null) {
						result.setSnippet(list.get(i).getFieldValue("snippet")
								.toString());
					}
					if (list.get(i).getFieldValue("popularityScore") != null) {
						result.setPopularityScore((Integer) list.get(i)
								.getFieldValue("popularityScore"));
					}
					if (!userDocSet.contains(result.getId())) {
						jsonResults.add(result);
					}
				}

				obj.put("results", jsonResults);
			}
			out.println(obj);
			out.close();
			return;
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
