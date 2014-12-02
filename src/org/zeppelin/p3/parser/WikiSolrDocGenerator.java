package org.zeppelin.p3.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

public class WikiSolrDocGenerator {

	/**
	 * Creates and returns the Solr Document
	 * 
	 * @return
	 */
	public List<SolrInputDocument> createSolrDocuments() {
		// TODO- Remove the hard-coding
		String fileName = "H:\\projects\\Wikinews\\wikinews.xml";

		List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
		WikiNewsSaxParsers wnsp = new  WikiNewsSaxParsers();
		List<Document> documents = wnsp.parseDocument(fileName);
		int count = 1;
		for (Document doc : documents) {
				SolrInputDocument solrDoc = new SolrInputDocument();
				solrDoc.addField("id", "WIKI"+count);
				solrDoc.addField("name", doc.getContent());
				solrDoc.addField("title", doc.getTitle());
				solrDoc.addField("source", doc.getSource());
				solrDoc.addField("published_date", doc.getPublishedDate());
				solrDoc.addField("content", doc.getContent());
				solrDoc.addField("place", doc.getPlace());
				solrDoc.addField("snippet", doc.getSnippet());
				solrDoc.addField("popularityScore", 0);
				
				if (doc.getCategories() != null) {
					for (String category : doc.getCategories()) {
						solrDoc.addField("category", category, 1.0f);
					}
				}
				// add the doc to the list
				solrInputDocuments.add(solrDoc);
				count++;

			}
			System.out.println(count);
		System.out.println(count + "WIKI documents parsed.");
		return solrInputDocuments;
	}
}
