package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zeppelin.p3.db.MySQLAccess;

/**
 * Servlet implementation class PersonalizedViewClass
 */
@WebServlet(urlPatterns = { "/PreferredSources" })
public class PreferredSources extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int count = 1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PreferredSources() {
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
		MySQLAccess dao = new MySQLAccess();
		Map<String, Boolean> preferredSourcesWithCheckValue;
		try {
			preferredSourcesWithCheckValue = dao
					.fetchPreferredSourcesWithCheckValue(userId);

			JSONObject obj = new JSONObject();
			obj.put("resultCount", preferredSourcesWithCheckValue.size());
			JSONArray jsonResults = new JSONArray();
			if (!preferredSourcesWithCheckValue.isEmpty()) {
				obj.put("resultCount", preferredSourcesWithCheckValue.size());
				for (String preferredSource : preferredSourcesWithCheckValue
						.keySet()) {
					PreferredSourceWithCheckValue result = new PreferredSourceWithCheckValue();
					result.setUserId(userId);
					result.setSource(preferredSource);
					result.setChecked(preferredSourcesWithCheckValue
							.get(preferredSource));
					jsonResults.add(result);
				}
				obj.put("results", jsonResults);
			}

			out.println(obj);
			out.close();
			return;
		} catch (Exception e) {
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
