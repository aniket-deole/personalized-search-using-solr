package org.zeppelin.p3.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BasicEvaluator extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 318188189790817924L;
	int count = 1;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set a cookie for the user, so that the counter does not increate
		// every time the user press refresh
		HttpSession session = request.getSession(true);
		String q = request.getHeader("q");
		String bq = null;
		// Set the session valid for 5 secs
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if (session.isNew()) {
			count++;
		}

		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);

		// q=modifyQuery(q);
		// q="title:sachin";
		bq = "category:sports";

		SolrQuery parameters = new SolrQuery();
		parameters.set("q", q);
		// parameters.set("bq", bq);

		try {
			QueryResponse q_response = solrServer.query(parameters);
			SolrDocumentList list = q_response.getResults();
			JSONObject obj = new JSONObject();
			obj.put("resultCount", list.size());
			JSONArray jsonResults = new JSONArray ();
			if (!list.isEmpty()) {
				obj.put("resultCount", list.size());
				for (int i = 0; i < list.size (); i++) {
					jsonResults.add(new QueryResult(list.get(i).getFieldValue("title").toString(), 
							list.get(i).getFieldValue("content").toString(), 
							list.get(i).getFieldValue("content").toString(), 
							3, 
							list.get(i).getFieldValue("category").toString(), 
							list.get(i).getFieldValue("source").toString()));
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
}
