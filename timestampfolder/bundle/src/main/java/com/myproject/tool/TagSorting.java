package com.myproject.tool;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


@SlingServlet(paths ="/bin/myproject/TagSorting",methods = {"GET"},metatype = true)
public class TagSorting extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TimeStampFolderCreator.class);
	private ResourceResolver resolver;
	private Session session ;
	private PageManager pageManager ;
	private Resource resource;
	private Tag[] tags;

	String parentPath=null;

	@Override
	protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response)
	{
		log.info("i am inside the doGet method");

		resolver = request.getResourceResolver() ; 
		session = resolver.adaptTo(Session.class); 
		pageManager =resolver.adaptTo(PageManager.class);
		//String rootPath = "/content/dam/news" ;
		parentPath = request.getParameter("contentPath");
		Page newsPage = pageManager.getPage(parentPath);

		log.info("parentPath::"+parentPath);

		Iterator<Page> newsChildPageItr = newsPage.listChildren();
		while(newsChildPageItr.hasNext()) 
		{
			log.info("inside while loop");
			try { 

				Page newsChildPage =newsChildPageItr.next();
				log.info("page is::"+newsChildPage.getPath());
				resource = resolver.getResource(newsChildPage.getPath()+"/jcr:content");

				TagManager tagManager = resolver.adaptTo(TagManager.class);
				tags = tagManager.getTags(resource);



			}catch (Exception e) 
			{ log.error("Error in the while loop..",e); }

		} 
		
		for(int i=0;i<tags.length;i++)
		{
			String tagName = tags[i].getName();
			log.info("tag::"+tagName);
		}

	/*	public List<Tag> getTags() {

			TagManager tagMgr = request.getResourceResolver().adaptTo(TagManager.class);

			Resource resource = (Resource) request.getAttribute("result");

			log.debug("Loading tags from {}@{}", new String[] { resource.getPath(), property });
			List<Tag> tags = new ArrayList<Tag>();
			String[] values = resource.getValueMap().get(property, String[].class);
			if (values != null) {
				for (String value : values) {
					tags.add(tagMgr.resolve(value));
				}
			}
			log.debug("Loaded {} tags", tags.size());

			return tags;

		} */

	}

}
