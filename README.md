ub535p3
=======
News Personalized Using Solr. <br/>
This project was developed as part of Project 3 in the Information Retrieval Course. <br/>
UI/UX inspired by Google's Material Design Philosophy.<br/>
<img src="http://i57.tinypic.com/24q7611.png"> </img>
Demo Link: https://www.dropbox.com/s/95gui1bbf4w8wqg/TeamZeppelinMovie.mov?dl=0
Steps to setup dev environment: <br/>
We assume you are using Eclipse as the IDE. <br/>
1. Import solr war from dist folder of solr archive downloaded from solr's website.<br/>
2. Import ub535p3 as an eclipse Project.<br/>
3. Copy the following files to ub535p3/WebContent/WEB-INF/lib<br/>
  	From dist<br/>
		  solr-4.10.2/dist/solr-solrj-4.10.2.jar<br/>
	  From dist/solrj-lib<br/>
		  solr-4.10.2/dist/solrj-lib/commons-io-2.3.jar<br/>
		  solr-4.10.2/dist/solrj-lib/httpclient-4.3.1.jar<br/>
		  solr-4.10.2/dist/solrj-lib/httpcore-4.3.jar<br/>
		  solr-4.10.2/dist/solrj-lib/httpmime-4.3.1.jar<br/>
		  solr-4.10.2/dist/solrj-lib/noggit-0.5.jar<br/>
		  solr-4.10.2/dist/solrj-lib/wstx-asl-3.2.7.jar<br/>
	  	solr-4.10.2/dist/solrj-lib/zookeeper-3.4.6.jar<br/>
4. Copy the /example/solr/collection1 folder to /solr_config folder in your Web Project.<br/>
5. Configure both projects to be hosted on the same Tomcat server on port 8080.<br/>
6. Change Run Configuration and add the following at the end of VM Arguments:<br/>
	  -Dsolr.solr.home=".../ub535p3/solr_config" (Absolute path of the solr_config folder inside your project)<br/>
7. Set corpus paths in RCVSolrDocumentGenerator.java, SolrDocumentGenerator.java, WikiSolrDocGenerator.java.<br/>
8. Start the server.<br/>
9. Click on the INDEX button on the FrontPage & wait for the text "Solr Server Commited" on the console.<br/>
10. Now you are free to use the application and browse around.<br/>
