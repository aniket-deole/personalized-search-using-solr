package org.zeppelin.p3.common;

import java.util.ArrayList;
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
import org.zeppelin.p3.personalization.PreferredSourceWithCheckValue;
import org.zeppelin.p3.personalization.UserLog;
import org.zeppelin.p3.query.QueryResult;

public class CommonUtil {

	public static SolrQuery createPersonalisedQuery(String q, Integer userId) {
		// Retrieve preferred categories for the given user id
		Map<String, Integer> preferredCategories = new HashMap<String, Integer>();
		ArrayList<PreferredSourceWithCheckValue> preferredSourcesWithCheckValue = new ArrayList<PreferredSourceWithCheckValue>();
		MySQLAccess dao = new MySQLAccess();

		try {
			preferredCategories = dao
					.fetchPreferredCategoriesWithTheirLikingScores(userId);

			preferredSourcesWithCheckValue = dao
					.fetchPreferredSourcesWithCheckValue(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SolrQuery query = new SolrQuery();

		query.set("q", q);
		query.set("defType", "edismax");
		// &hl=true&hl.fl=snippet
		/**
		 * Reference -
		 * http://stackoverflow.com/questions/1066589/java-iterate-through
		 * -hashmap
		 */
		Iterator it = preferredCategories.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String boostTerm = "category:" + pairs.getKey();
			if((Integer)pairs.getValue()!=0){
				Double bf = 1+Math.log10((Integer)pairs.getValue());
				query.add("bq", boostTerm+CommonConstants.CARROT+bf);
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		//preferred sources
		for(PreferredSourceWithCheckValue prefSource:preferredSourcesWithCheckValue){
			if(prefSource.getChecked()){
				query.add("bq", "source:"+ prefSource.getSource());
			}
		}
		
		String urlString = "http://localhost:8080/solr-4.10.2/";
		SolrServer solrServer = new HttpSolrServer(urlString);

		try {
			List<UserLog> userLogs = dao
					.fetchLikeScoresAndClickCountsForAllDocuments(userId);
			if (userLogs != null) {
				for (UserLog userlog : userLogs) {
					String docId = userlog.getDocID();
					// http://localhost:8080/solr-4.10.2/query?q=*:*&defType=edismax&id=123
					// http://localhost:8080/solr-4.10.2/query?q=id:123
					SolrQuery parameters = new SolrQuery();
					parameters.setRequestHandler("/query");
					parameters.set("q", "id:" + docId);
					// fetch the title of the docId
					QueryResponse q_response = solrServer.query(parameters);
					SolrDocumentList list = q_response.getResults();
					QueryResult result = new QueryResult();
					if (list.get(0).getFieldValue("title") != null) {
						result.setTitle(list.get(0).getFieldValue("title")
								.toString());
					}

					Double bfLikeTitle = 0.0;
					Double bfClickTitle = 0.0;

					if (userlog.getLikingScore() != 0) {
						bfLikeTitle = 1 + Math.log10(userlog.getLikingScore());
					}

					if (userlog.getClickCount() != 0) {
						bfClickTitle = 1 + Math.log10(userlog.getClickCount());
					}

					// boost factor for the whole title
					Double bfTitle = bfLikeTitle + bfClickTitle;

					Double bfLikeTerms = 0.0;
					Double bfClickTerms = 0.0;

					if (userlog.getLikingScore() != 0) {
						bfLikeTerms = 1 + Math.log10(userlog.getLikingScore());
					}

					if (userlog.getClickCount() != 0) {
						bfClickTerms = 1 + Math.log10(userlog.getClickCount());
					}

					// boost factor for the whole title
					Double bfTerms = bfLikeTerms + bfClickTerms;

					if (result.getTitle() != null) {
						String s = result.getTitle();
						String boostTitle = s + CommonConstants.CARROT
								+ bfTitle;
						query.add("bq", boostTitle);
						s = s.replace("[", "").replace("]", "").replace(":", "");
						String[] arr = s.split(CommonConstants.WHITESPACE);
						if (arr != null && arr.length > 0) {
							for (int i = 0; i < arr.length; i++) {
								String boostTerm = arr[i]
										+ CommonConstants.CARROT + bfTerms;
								query.add("bq", boostTerm);
							}
						}
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		query.set("hl", "true");
		query.set("hl.fl", "content");
		return query;
	}

}
