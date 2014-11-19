package org.zeppelin.p3.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

public class SolrDocumentGenerator {

	
	/**
	 * Creates and returns the Solr Document
	 * @return
	 */
	public List<SolrInputDocument> createSolrDocuments(){
		List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
		SaxParserNytHandler handler = new SaxParserNytHandler();
		//TODO- Pass multiple documents
		Document doc=handler.parseDocument("H:\\projects\\nyt.xml");
		SolrInputDocument solrDoc = new SolrInputDocument();
		solrDoc.addField("content", doc.getContent());
		solrDoc.addField("title", doc.getTitle());
		if(doc.getCategories()!=null){
			for(String cat:doc.getCategories()){
				solrDoc.addField("category", cat);
			}
		}
		//add the doc to the list
		solrInputDocuments.add(solrDoc);
		return solrInputDocuments;
	}
}
