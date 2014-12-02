package org.zeppelin.p3.parser;

import java.util.List;

public class WikiParserRunner {
	public static void main (String[] args) {
		WikiNewsSaxParsers wnsp = new  WikiNewsSaxParsers();
		List<Document> d1 = wnsp.parseDocument("/Users/aniket/Downloads/enwikinews-20131030-pages-articles.xml");
		System.out.println(d1.size ());
	}
}
	