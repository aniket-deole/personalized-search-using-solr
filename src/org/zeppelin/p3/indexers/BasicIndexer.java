package org.zeppelin.p3.indexers;

import java.io.IOException;
import java.io.PrintWriter;

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
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class BasicIndexer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682874284247388356L;

	int count = 1;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set a cookie for the user, so that the counter does not increate
		// every time the user press refresh
		HttpSession session = request.getSession(true);
		// Set the session valid for 5 secs
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		String q = request.getHeader ("q");
		PrintWriter out = response.getWriter();
		if (session.isNew()) {
			count++;
		}
		
		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);
		
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", count + 1);
		document.addField("name", "Gouda cheese wheel");
		document.addField("price", "49.99");
		try {
			UpdateResponse updateResponse = solrServer.add(document);
		} catch (SolrServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document = new SolrInputDocument();
		document.addField("id", "12333");
		document.addField("name", "Gouda chutiya cheese wheel");
		document.addField("price", "8723");
		try {
			UpdateResponse updateResponse = solrServer.add(document);
		} catch (SolrServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			solrServer.commit();
		} catch (SolrServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		out.println("2 Documents Indexed.");
		return;		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet (req, resp);
	};

	@Override
	public void init() throws ServletException {
	}

	public void destroy() {
		super.destroy();

	}
}