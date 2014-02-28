package yescomment.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yescomment.crawler.CrawlerSingletonLocal;

@WebServlet(name = "CrawlerStatusServlet", urlPatterns = {"/crawler"})
@ServletSecurity(
@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL,
    rolesAllowed = {"yescommentadmin"}))
public class CrawlerStatusServlet  extends HttpServlet{

	@EJB
	CrawlerSingletonLocal crawlerSingleton;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String commandParameter=req.getParameter("command");
		if (commandParameter!=null&&commandParameter.equals("start")) {
			crawlerSingleton.startCrawler();
		}
		if (commandParameter!=null&&commandParameter.equals("stop")) {
			crawlerSingleton.stopCrawler();
		}
		boolean isCrawlerRunning = crawlerSingleton.isCrawlerRunning();
		
		resp.setContentType("text/html");
		resp.setBufferSize(2048);
        PrintWriter out = resp.getWriter();

        // then write the data of the response
        out.println("<html><head><title>Crawler status servlet</title></head><body>");
       
        
        if (isCrawlerRunning) {
        	out.println("<h2>Crawler is running</h2>");	
        }
        else {
        	out.println("<h2>Crawler is not running</h2>");
        }
        out.println("<h4>Usage:set param command=start or command=stop or no param</h4>");
        out.println("</body></html>");
        out.close();
		
	}

	

}
