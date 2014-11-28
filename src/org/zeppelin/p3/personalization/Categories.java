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
 * Servlet implementation class Categories
 */
@WebServlet(urlPatterns = { "/Categories" })
public class Categories extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int count = 1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Categories() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set a cookie for the user, so that the counter does not increate
		// every time the user press refresh
		HttpSession session = request.getSession(true);
		// Set the session valid for 5 secs
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if (session.isNew()) {
			count++;
		}

		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);

		// Retrieve all the categories available
		ArrayList<String> categories = new ArrayList<String>();
		MySQLAccess dao = new MySQLAccess();
		try {
			categories = dao.fetchAllCategories();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject obj = new JSONObject();
		obj.put("resultCount", categories.size());
		JSONArray jsonResults = new JSONArray();
		if (!categories.isEmpty()) {
			// TODO - Show all results in a page view and asynchronously
			for (int i = 0; i < categories.size(); i++) {
				jsonResults.add(categories.get(i).toString());
			}
			obj.put("results", jsonResults);
		}
		out.println(obj);
		out.close();
		return;

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
