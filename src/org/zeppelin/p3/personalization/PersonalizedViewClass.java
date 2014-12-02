package org.zeppelin.p3.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.zeppelin.p3.common.CommonConstants;
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
		Map<String,Integer> preferredCategories = new HashMap<String,Integer>();
		MySQLAccess dao = new MySQLAccess();
		Map<String, Integer> likeScoresAssignedByLoggedInUser = new HashMap<String, Integer>();
		ArrayList<PreferredSourceWithCheckValue> preferredSourcesWithCheckValue = new ArrayList<PreferredSourceWithCheckValue>();
		try {
			preferredCategories = dao.fetchPreferredCategoriesWithTheirLikingScores(userId);
			preferredSourcesWithCheckValue = dao
					.fetchPreferredSourcesWithCheckValue(userId);
			likeScoresAssignedByLoggedInUser = dao
					.fetchLikeScoreForAllDocuments(userId);
			//fetchPreferredCategoriesWithTheirLikingScores
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//TODO - remove temp Hard coding
	/*	preferredCategories.put("Business", 6);
		preferredCategories.put("Sports", 3);*/
		StringBuilder queryString=new StringBuilder();
		
		
		/**
		 * Reference - http://stackoverflow.com/questions/1066589/java-iterate-through-hashmap
		 */
		Iterator it = preferredCategories.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if((Integer)pairs.getValue()!=0){
	        	Double boostFactor = 1 + Math.log10((Integer)pairs.getValue());
                queryString.append("category:").append(pairs.getKey()).append(CommonConstants.CARROT).append(boostFactor);
		        queryString.append(CommonConstants.WHITESPACE);
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
	    //System.out.println(queryString);
	    
	  
	    
	    
	    try {
			List<UserLog> userLogs=dao.fetchLikeScoresAndClickCountsForAllDocuments(userId);
			if(userLogs!=null){
				for(UserLog userlog:userLogs){
					
					String docId=userlog.getDocID();
					//http://localhost:8080/solr-4.10.2/query?q=*:*&defType=edismax&id=123
					//http://localhost:8080/solr-4.10.2/query?q=id:123
					SolrQuery parameters = new SolrQuery();
					parameters.setRequestHandler("/query");
					parameters.set("q", "id:"+docId);
					//fetch the title of the docId
					QueryResponse q_response = solrServer.query(parameters);
					SolrDocumentList list = q_response.getResults();
					if (!list.isEmpty()) {
						QueryResult result = new QueryResult();
						if (list.get(0).getFieldValue("title") != null) {
							result.setTitle(list.get(0).getFieldValue("title")
									.toString());
						}
						
						Double bfLikeTitle = 0.0;
						Double bfClickTitle = 0.0;
						
						if(userlog.getLikingScore()!=0){
							bfLikeTitle = 1+Math.log10(userlog.getLikingScore());
						}
						
						if(userlog.getClickCount()!=0){
							bfClickTitle = 1+Math.log10(userlog.getClickCount());
						}
						
						//boost factor for the whole title
						Double bfTitle = bfLikeTitle + bfClickTitle;
						
						
						Double bfLikeTerms = 0.0;
						Double bfClickTerms = 0.0;
						
						if(userlog.getLikingScore()!=0){
							bfLikeTerms = 1+Math.log10(userlog.getLikingScore());
						}
						
						if(userlog.getClickCount()!=0){
							bfClickTerms = 1+Math.log10(userlog.getClickCount());
						}
						
						//boost factor for the whole title
						Double bfTerms = bfLikeTerms + bfClickTerms;
						
						if(result.getTitle()!=null){
							String s = result.getTitle();
							queryString.append("\""+s+"\"").append(CommonConstants.CARROT).append(bfTitle);
					        queryString.append(CommonConstants.WHITESPACE);
							s = s.replace("[", "").replace("]", "").replace(":", "");
							String [] arr = s.split(CommonConstants.WHITESPACE);
							if(arr!=null && arr.length>0){
								for(int i=0;i<arr.length;i++){
									queryString.append(arr[i]).append(CommonConstants.CARROT).append(bfTerms);
							        queryString.append(CommonConstants.WHITESPACE);
								}
							}
						}
					}
					
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    
	    System.out.println("\nQuery for personalised class "+queryString);
	    
		SolrQuery parameters = new SolrQuery();
		
		//preferred sources
  		for(PreferredSourceWithCheckValue prefSource:preferredSourcesWithCheckValue){
  			if(prefSource.getChecked()){
  				//parameters.add("bq", "source:"+ prefSource.getSource());
  				queryString.append("source:"+prefSource.getSource());
		        queryString.append(CommonConstants.WHITESPACE);
  			}
  		}
		parameters.set("q", queryString.toString());
		// parameters.set("sort", "published_date desc");
		parameters.set("defType", "edismax");
		
		parameters.set("start", 0);
		parameters.set("rows", 50);
		// Iterate over the preferred categories and apply them to query
		// boosters
/*		parameters.set("bq", preferredCategories
				.toArray(new String[preferredCategories.size()]));*/
		try {
			QueryResponse q_response = solrServer.query(parameters);
			SolrDocumentList list = q_response.getResults();
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
					if(list.get(i).getFieldValue("source")!=null){
						result.setSource(list.get(i).getFieldValue("source").toString());
					}
					if(list.get(i).getFieldValue("published_date")!=null){
						result.setPublishedDate(list.get(i).getFieldValue("published_date").toString());
					}
					if(list.get(i).getFieldValue("snippet")!=null){
						result.setSnippet(list.get(i).getFieldValue("snippet").toString());
					}
					if(list.get(i).getFieldValue("popularityScore")!=null){
						result.setPopularityScore((Integer)list.get(i).getFieldValue("popularityScore"));
					}
					
					jsonResults.add(result);
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
