package com.gdn.venice.server.app.testrestdatasource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class TestRestDataSourceServlet
 */
public class TestRestDataSourceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestRestDataSourceServlet() {
        super();
    }
    
    String retVal = "<html>\n" +
    "<head>" +
    "<title>test</title>" +
    "</head>\n" +
    "<body onload=\"alert('test')\">" +
	"<p>TEST</p>\n" +
	"</body>\n" +
	"</html>";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.getOutputStream().println(retVal);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
					
		response.getOutputStream().println(retVal);
	}
	
}
