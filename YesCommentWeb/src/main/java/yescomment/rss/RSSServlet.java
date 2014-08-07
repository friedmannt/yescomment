package yescomment.rss;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yescomment.recommended.RecommendationAscpect;
import yescomment.util.JSFUtil;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

@WebServlet(name = "RSSServlet", urlPatterns = "/rss/*")
public class RSSServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	RSSFeedCreator rssFeedCreator;
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FeedException {
		String urlBase=JSFUtil.getRSSLinkURLBase(request);

		final String recommentdationAspectParameter = request.getParameter("recommendation");
		final String articleParameter = request.getParameter("article");
		if (recommentdationAspectParameter==null && articleParameter==null) {
			response.setContentType("text/html");
		    PrintWriter writer = response.getWriter();
		    writer.println("<h1>No parameter specified</h1>");
		    writer.close();
		}
		else if (recommentdationAspectParameter!=null && articleParameter!=null) {
			response.setContentType("text/html");
		    PrintWriter writer = response.getWriter();
		    writer.println("<h1>Too many parameters specified</h1>");
		    writer.close();
		}
		else if (recommentdationAspectParameter!=null && articleParameter==null) {
			RecommendationAscpect recommendationAscpect = RecommendationAscpect.valueOf(recommentdationAspectParameter);
			final SyndFeed feed = rssFeedCreator.getRSSFeedOfRecommendationAspect(recommendationAscpect, urlBase);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/rss+xml");
			PrintWriter writer = response.getWriter();
			final SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, writer);
			writer.close();
		}
		else if (recommentdationAspectParameter==null && articleParameter!=null) {
			String articleId=articleParameter;
			final SyndFeed feed = rssFeedCreator.getRSSFeedOfArticlesComments(articleId,urlBase);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/rss+xml");
			PrintWriter writer = response.getWriter();
			final SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, writer);
			writer.close();
		}

		
	}

	
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (FeedException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (FeedException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public String getServletInfo() {
		return "Yescomnent RSS servlet";
	}

}