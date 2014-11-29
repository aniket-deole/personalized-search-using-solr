package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zeppelin.p3.db.MySQLAccess;
import org.zeppelin.p3.query.QueryResult;

/**
 * Servlet implementation class PersonalizedViewClass
 */
@WebServlet(urlPatterns = { "/PersonalizedViewClass" })
public class PersonalizedViewClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int count = 1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersonalizedViewClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		PrintWriter out = response.getWriter();

		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);
		// Fetch the Logged In userId from the session
		Integer userId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		// Retrieve preferred categories for the given user id
		ArrayList<String> preferredCategories = new ArrayList<String>();
		MySQLAccess dao = new MySQLAccess();
		try {
			preferredCategories = dao.fetchPreferredCategories(userId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SolrQuery parameters = new SolrQuery();
		parameters.set("q", "*:*");
		// parameters.set("sort", "published_date desc");
		parameters.set("defType", "edismax");
		// Iterate over the preferred categories and apply them to query
		// boosters
		parameters.set("bq", preferredCategories
				.toArray(new String[preferredCategories.size()]));
		try {
			QueryResponse q_response = solrServer.query(parameters);
			SolrDocumentList list = q_response.getResults();
			JSONObject obj = new JSONObject();
			obj.put("resultCount", list.size());
			JSONArray jsonResults = new JSONArray();
			if (!list.isEmpty()) {
				obj.put("resultCount", list.size());
				// TODO - Show all results in a page view and asynchronously
				for (int i = 0; i < 20; i++) {
					if (i == list.size()) {
						break;
					}
					jsonResults
							.add(new QueryResult(list.get(i)
									.getFieldValue("id").toString(), list
									.get(i).getFieldValue("title").toString(),
									list.get(i).getFieldValue("content")
											.toString(), list.get(i)
											.getFieldValue("content")
											.toString(), 3, list.get(i)
											.getFieldValue("category")
											.toString(),
									list.get(i).getFieldValue("source")
											.toString(), list.get(i)
											.getFieldValue("published_date")
											.toString()));
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
