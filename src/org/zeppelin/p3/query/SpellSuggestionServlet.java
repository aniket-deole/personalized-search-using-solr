package org.zeppelin.p3.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SpellSuggestionServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	};
	
	//http://localhost:8080/solr-4.10.2/suggest?q=ac
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		String q = request.getHeader("q");
		PrintWriter out = response.getWriter();
		String urlString = "http://localhost:8080/solr-4.10.2";
		SolrServer solrServer = new HttpSolrServer(urlString);
		SolrQuery parameters = new SolrQuery();
		parameters.setRequestHandler("/suggest");
		parameters.set("q", q);
		QueryResponse q_response;
		JSONArray suggestions = new JSONArray();
		try {
			q_response = solrServer.query(parameters);
			SpellCheckResponse spellCheckResp = q_response.getSpellCheckResponse();
			List<Suggestion> list=spellCheckResp.getSuggestions();
			if(!list.isEmpty()){
				Suggestion suggestion=list.get(0);
				List alternatives=suggestion.getAlternatives();
				if (!alternatives.isEmpty()) {
					for(Object alternative:alternatives){
						SpellCorrection spellCorrection = new SpellCorrection();
						spellCorrection.setCorrection(alternative.toString());
						suggestions.add(spellCorrection);
					}
				}
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("suggestions", suggestions);
	}
}
