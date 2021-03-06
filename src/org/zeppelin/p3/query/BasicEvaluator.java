package org.zeppelin.p3.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zeppelin.p3.common.CommonUtil;
import org.zeppelin.p3.db.MySQLAccess;
import org.zeppelin.p3.personalization.PreferredSourceWithCheckValue;

public class BasicEvaluator extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 318188189790817924L;
	int count = 1;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String q = request.getHeader("q");
		String bq = null;

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);
		// q=modifyQuery(q);
		// q="title:sachin";
		// bq = "category:sports";

		// Fetch the Logged In userId from the session
		Integer userId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		Map<String, Integer> likeScoresAssignedByLoggedInUser = new HashMap<String, Integer>();
		MySQLAccess dao = new MySQLAccess();
		try {
			likeScoresAssignedByLoggedInUser = dao
					.fetchLikeScoreForAllDocuments(userId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SolrQuery query = CommonUtil.createPersonalisedQuery(q, userId);

		System.out.println("\nQuery in BasicEvaluator::: " + query);
		// }
		// parameters.set("bq", bq);

		// parameters = morelikethisQuery(q);

		// spellcheck(q);

		try {
			QueryResponse q_response = solrServer.query(query);
			SolrDocumentList list = q_response.getResults();
			Map<String, Map<String, List<String>>> h = q_response
					.getHighlighting();
			JSONObject obj = new JSONObject();
			obj.put("resultCount", list.size());
			JSONArray jsonResults = new JSONArray();
			if (!list.isEmpty()) {
				obj.put("resultCount", list.size());
				for (int i = 0; i < list.size(); i++) {
					String docId = list.get(i).getFieldValue("id").toString();
					Integer rating = likeScoresAssignedByLoggedInUser
							.get(docId);
					if (rating == null) {
						rating = 0;
					}
					QueryResult result = new QueryResult();

					result.setId(docId);
					result.setRating(rating);
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
					if (list.get(i).getFieldValue("source") != null) {
						result.setSource(list.get(i).getFieldValue("source")
								.toString());
					}
					if (list.get(i).getFieldValue("published_date") != null) {
						result.setPublishedDate(list.get(i)
								.getFieldValue("published_date").toString());
					}

					@SuppressWarnings("unused")
					Map<String, List<String>> sn = h.get(docId);
					List<String> sni = sn.get("content");
					StringBuilder snip = new StringBuilder();
					if (sni != null && !sni.isEmpty()) {
						for (String s : sni) {
							snip.append(s);
						}
						result.setSnippet(snip.toString());
					} else {
						if (list.get(i).getFieldValue("snippet") != null) {
							result.setSnippet(list.get(i)
									.getFieldValue("snippet").toString());
						}
					}
					if (list.get(i).getFieldValue("popularityScore") != null) {
						result.setPopularityScore((Integer) list.get(i)
								.getFieldValue("popularityScore"));
					}
					if (list.get(i).getFieldValue("source") != null) {
						result.setSource(list.get(i).getFieldValue("source")
								.toString());
					}

					jsonResults.add(result);
				}
				obj.put("results", jsonResults);
			}

			else {
				JSONArray spellCorrections = getSpellCorrections(q);
				obj.put("spellCorrections", spellCorrections);
			}
			out.println(obj);
			out.close();
			return;
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	};

	@Override
	public void init() throws ServletException {
	}

	public void destroy() {
		super.destroy();

	}

	String modifyQuery(String q) {
		String modifiedQuery = q + "OR (category:sports)^2.0";
		return modifiedQuery;
	}

	SolrQuery morelikethisQuery(String q) {

		// http://localhost:8983/solr/select?q=apache&mlt=true&mlt.fl=manu,cat&mlt.mindf=1&mlt.mintf=1&fl=id,score
		SolrQuery parameters = new SolrQuery();
		parameters.set("q", q);
		parameters.set("defType", "edismax");
		parameters.set("mlt.fl", "title,content,category");
		parameters.set("mlt", "true");
		// parameters.set("mlt.mindf", "1");
		// parameters.set("mlt.mintf", "1");
		return parameters;
	}

	/**
	 * Returns the array of suggested spellings
	 * 
	 * @param q
	 * @return
	 */
	JSONArray getSpellCorrections(String q) {
		String urlString = "http://localhost:8080/solr-4.10.2";
		SolrServer solrServer = new HttpSolrServer(urlString);
		// http://localhost:8080/solr-4.10.2/suggest?q=ac
		// http://localhost:8080/solr-4.10.2/spell?q=delll%20ultrashar&spellcheck=true&spellcheck.collate=true&spellcheck.build=true
		// http://localhost:8080/solr-4.10.2/select?q=*:*&fq=popularityScore:[1%20TO%20*]&sort=popularityScore%20desc
		SolrQuery parameters = new SolrQuery();
		parameters.setRequestHandler("/spell");
		parameters.set("q", q);
		parameters.set("defType", "edismax");
		parameters.set("spellcheck", "true");
		parameters.set("spellcheck.collate", "true");
		parameters.set("spellcheck.build", "true");
		QueryResponse q_response;
		JSONArray spellCorrections = new JSONArray();
		try {
			q_response = solrServer.query(parameters);
			SpellCheckResponse spellCheckResp = q_response
					.getSpellCheckResponse();
			List<Suggestion> list = spellCheckResp.getSuggestions();
			if (!list.isEmpty()) {
				Suggestion suggestion = list.get(0);
				List alternatives = suggestion.getAlternatives();
				if (!alternatives.isEmpty()) {
					for (Object alternative : alternatives) {
						SpellCorrection spellCorrection = new SpellCorrection();
						spellCorrection.setCorrection(alternative.toString());
						spellCorrections.add(spellCorrection);
					}
				}
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spellCorrections;
	}
}