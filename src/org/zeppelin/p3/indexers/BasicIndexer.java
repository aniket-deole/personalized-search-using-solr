package org.zeppelin.p3.indexers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.zeppelin.p3.parser.RCVSolrDocumentGenerator;
import org.zeppelin.p3.parser.SolrDocumentGenerator;
import org.zeppelin.p3.parser.WikiSolrDocGenerator;

public class BasicIndexer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682874284247388356L;

	int count = 1;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain");
		String q = request.getHeader("q");
		PrintWriter out = response.getWriter();

		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);
		
		List<SolrInputDocument> solrDocuments = new ArrayList<SolrInputDocument>();
	 
        SolrDocumentGenerator generator = new SolrDocumentGenerator();
		List<SolrInputDocument> nytdocuments = generator
				.createSolrDocuments();
		
		solrDocuments.addAll(nytdocuments);
		
		/*WikiSolrDocGenerator wikiGenerator = new WikiSolrDocGenerator();
     	List<SolrInputDocument> wikiDocuments = wikiGenerator
				.createSolrDocuments();
	
     	solrDocuments.addAll(wikiDocuments);*/
		
        RCVSolrDocumentGenerator rcvGenerator = new RCVSolrDocumentGenerator();
		List<SolrInputDocument> solrDocumentsRCV = rcvGenerator
				.createSolrDocuments();
		
		solrDocuments.addAll(solrDocumentsRCV);
		
		int docCount = 0;
		System.out.println("Total no.of docs "+solrDocuments.size());
		for (SolrInputDocument solrInputDocument : solrDocuments) {
			try {
				if(!("").equals(solrInputDocument.getFieldValue("content"))){
					solrServer.add(solrInputDocument);
					docCount++;
					//System.out.println("Doc count "+docCount);
				}
				//solrServer.commit();
			} catch (SolrServerException e1) {
				e1.printStackTrace();
			}
		}
		try {
			System.out.println("Solr Server Commited.");
			solrServer.commit();
		} catch (SolrServerException e1) {
			e1.printStackTrace();
		}
		out.println(docCount + " Documents Indexed.");
		return;
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
}
