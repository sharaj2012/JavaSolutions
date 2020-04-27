package com.myproject.tool;


import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


@SlingServlet(paths ="/bin/myproject/timestampfolder",
methods = {"GET"},metatype = true)
public class TimeStampFolderCreator extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TimeStampFolderCreator.class);
	private ResourceResolver resolver;
	private Session session ;
	private PageManager pageManager ;

	String parentPath=null;
	
	public void goGet(SlingHttpServletRequest request,SlingHttpServletResponse response)
	{
		log.info("i am inside the doGet method");

		resolver = request.getResourceResolver() ; 
		session = resolver.adaptTo(Session.class); 
		pageManager =resolver.adaptTo(PageManager.class);
		String rootPath = "/content/dam/news" ;
		parentPath = request.getParameter("contentPath");
		Node fileNode=null;
		Page newsPage = pageManager.getPage(parentPath);
		
		log.info("parentPath::"+parentPath+"root path::"+rootPath);
		
/*	    for( ch = 'a' ; ch <= 'z' ; ch++ ){

	        Node damNode=slingRequest.getResourceResolver().getResource("/content/dam/wbl/country/").adaptTo(Node.class);
	        fileNode = damNode.addNode(String.valueOf(ch ), "sling:OrderedFolder");
	        session.save();

	 } */

		Iterator<Page> newsChildPageItr = newsPage.listChildren();
		while(newsChildPageItr.hasNext()) 
		{
			log.info("inside while loop");
			try { 
				Page newsChildPage =newsChildPageItr.next();
				String createdDate = "2020-04-24T10:16:10.334+05:30" ;
				ValueMap pageProperties =newsChildPage.getProperties();
				createdDate= pageProperties.get("jcr:created","");
				
				log.info("date::"+createdDate);
				
			String Date = createdDate.substring(0, 10);
			String[] values = Date.split("-");
			String year = values[0];
			String month = values[1] ;
			String day = values[0] ;
			
			log.info(year+"::"+month+"::"+day);
			
            if(!session.nodeExists("/content/dam/news/"+year)){
            	
            	log.info("inside folder creation if");
            	
            	Node yearNode = resolver.getResource("/content/dam/news/").adaptTo(Node.class);
    	        fileNode = yearNode.addNode(year,"sling:OrderedFolder");
    	        
    	        Node monthNode = fileNode.addNode(month, "sling:OrderedFolder");
    	        Node dayNode = monthNode.addNode(day, "sling:OrderedFolder");
    	        session.save();

   			/*	Node damCharNode=slingRequest.getResourceResolver().getResource("/content/dam/wbl/country/"+firstChar).adaptTo(Node.class);

                damCharNode.addNode(countryName, "sling:OrderedFolder");
                session.save();
                out.println(countryName +"::::folder created"+i+"<br>");*/
			}
			

			}catch (Exception e) 
			{ log.error("Error in the while loop..",e); }

		} 
	}
}
