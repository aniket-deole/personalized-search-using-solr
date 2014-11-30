package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.zeppelin.p3.db.MySQLAccess;

/**
 * Servlet implementation class ClickRegisterer
 */
@WebServlet("/ClickRegisterer")
public class ClickRegisterer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClickRegisterer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docId = request.getHeader("docId");
		String popularityS = request.getHeader("popularityScore");
		MySQLAccess dao = new MySQLAccess();
		// Fetch the Logged In userId from the session
		Integer loggedInUserId = (Integer) request.getSession().getAttribute(
				"loggedInUserId");
		try {
			dao.updateClickCountForDocument(loggedInUserId, docId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);
		
		Integer popularityScore = Integer.parseInt(popularityS);
		popularityScore = popularityScore+1;
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("id",docId);
		//solrInputDocument.addField("popularityScore",popularityScore);
		Map<String, Integer> popularityMap = new HashMap<String, Integer>();
		popularityMap.put("inc", 1);
		solrInputDocument.addField("popularityScore", popularityMap);
		try {
			UpdateResponse updateResponse = solrServer
					.add(solrInputDocument);
			solrServer.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		UpdateResponse updateResponse = solrServer
				.add(solrInputDocument);
		docCount++;
		System.out.println(docId);*/


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
