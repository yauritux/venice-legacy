package com.gdn.venice.server.presenter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileDownloadPresenterServlet
 */
public class FileDownloadPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int BUFSIZE = 1024;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownloadPresenterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		
		if (filename.contains("-none-")) {
			return;
		}
		
        String shortFileName = filename.substring(filename.lastIndexOf("/")+1, filename.length());
		
        File file = new File(filename);
        int length   = 0;
        ServletOutputStream op = response.getOutputStream();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);

        //  Set the response and go!
        response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
        response.setContentLength( (int)file.length() );
        

        response.setHeader( "Content-Disposition", "attachment; filename=\"" + shortFileName + "\"" );

        //  Stream to the requester.
        byte[] bbuf = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        while ((in != null) && ((length = in.read(bbuf)) != -1))
        {
            op.write(bbuf,0,length);
        }

        in.close();
        op.flush();
        op.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
