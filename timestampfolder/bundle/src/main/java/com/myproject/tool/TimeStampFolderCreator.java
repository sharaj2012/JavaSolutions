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


@SlingServlet(paths ="/bin/myproject/timestampfolder",methods = {"GET"},metatype = true)
public class TimeStampFolderCreator extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TimeStampFolderCreator.class);
	private ResourceResolver resolver;
	private Session session ;
	private PageManager pageManager ;

	String parentPath=null;

	@Override
	protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response)
	{
		log.info("i am inside the doGet method");

		resolver = request.getResourceResolver() ; 
		session = resolver.adaptTo(Session.class); 
		pageManager =resolver.adaptTo(PageManager.class);
		String rootPath = "/content/dam/news" ;
		parentPath = request.getParameter("contentPath");
		Node fileNode = null;
		Node monthNode = null;
		Node yearNode = null;
		Node dayNode = null;
		Page newsPage = pageManager.getPage(parentPath);

		log.info("parentPath::"+parentPath+"root path::"+rootPath);

		Iterator<Page> newsChildPageItr = newsPage.listChildren();
		while(newsChildPageItr.hasNext()) 
		{
			log.info("inside while loop");
			try { 
				Page newsChildPage =newsChildPageItr.next();
				String createdDate = "" ;
				ValueMap pageProperties =newsChildPage.getProperties();
				createdDate= pageProperties.get("jcr:created","");

				log.info("date::"+createdDate);

				String Date = createdDate.substring(0, 10);
				String[] values = Date.split("-");
				String year = values[0];
				String month = values[1] ;
				String day = values[2] ;

				log.info(year+"::"+month+"::"+day);

				if(!session.nodeExists("/content/dam/news/"+year)){

					log.info("inside folder creation if");

					yearNode = resolver.getResource("/content/dam/news/").adaptTo(Node.class);
					fileNode = yearNode.addNode(year,"sling:OrderedFolder");

					monthNode = fileNode.addNode(month, "sling:OrderedFolder");
					dayNode = monthNode.addNode(day, "sling:OrderedFolder");
					session.save();
				}
				else if(session.nodeExists("/content/dam/news/"+year))
				{
					if(!session.nodeExists("/content/dam/news/"+year+"/"+month))
					{
						monthNode = resolver.getResource("/content/dam/news/"+year).adaptTo(Node.class);
						fileNode = monthNode.addNode(month,"sling:OrderedFolder");
						dayNode = fileNode.addNode(day, "sling:OrderedFolder");
						session.save();
					}
					else if(session.nodeExists("/content/dam/news/"+year+"/"+month))
					{
						if(!session.nodeExists("/content/dam/news/"+year+"/"+month+"/"+day))
						{
							dayNode = resolver.getResource("/content/dam/news/"+year+"/"+month).adaptTo(Node.class);
							fileNode = dayNode.addNode(day,"sling:OrderedFolder");
							session.save();
						}
					}
				}
			}catch (Exception e) 
			{ log.error("Error in the while loop..",e); }

		} 
	}
}
