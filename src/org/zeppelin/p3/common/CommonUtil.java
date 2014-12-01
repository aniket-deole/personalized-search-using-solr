package org.zeppelin.p3.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.zeppelin.p3.db.MySQLAccess;
import org.zeppelin.p3.personalization.UserLog;
import org.zeppelin.p3.query.QueryResult;

public class CommonUtil {
	
	public static SolrQuery createPersonalisedQuery(String q,Integer userId){
		   // Retrieve preferred categories for the given user id
			Map<String,Integer> preferredCategories = new HashMap<String,Integer>();
			
			MySQLAccess dao = new MySQLAccess();
			
			try {
				preferredCategories = dao.fetchPreferredCategoriesWithTheirLikingScores(userId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*//TODO - remove temp Hard coding
			preferredCategories.put("Business", 6);
			preferredCategories.put("Sports", 3);*/
			
			SolrQuery query = new SolrQuery();
			
			query.set("q", q);
			query.set("defType", "edismax");
			/**
			 * Reference - http://stackoverflow.com/questions/1066589/java-iterate-through-hashmap
			 */
			Iterator it = preferredCategories.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        String boostTerm = "category:"+pairs.getKey();
		        query.add("bq", boostTerm);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    

		    
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
						String urlString = "http://localhost:8080/solr-4.10.2/";
						SolrServer solrServer = new HttpSolrServer(urlString);
						QueryResponse q_response = solrServer.query(parameters);
						SolrDocumentList list = q_response.getResults();
						if (!list.isEmpty()) {
							QueryResult result = new QueryResult();
							if (list.get(0).getFieldValue("title") != null) {
								result.setTitle(list.get(0).getFieldValue("title")
										.toString());
							}
							Integer boostFactor = userlog.getLikingScore()*2 + userlog.getClickCount();
							if(result.getTitle()!=null){
								
								String s = result.getTitle();
								s = s.replace("[", "").replace("]", "").replace(":", "");
								String [] arr = s.split(CommonConstants.WHITESPACE);
								if(arr!=null && arr.length>0){
									for(int i=0;i<arr.length;i++){
										String boostTerm = arr[i] +CommonConstants.CARROT+boostFactor;
								        query.add("bq", boostTerm);
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
		    
		    return query;
	}

}
