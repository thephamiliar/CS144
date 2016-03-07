package edu.ucla.cs.cs144;


import java.io.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try {
	        // your codes here
	        String q = request.getParameter("q");
	        URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + q);
	        BufferedReader reader = null;
	    	StringBuilder stringBuilder;

	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Content-Type", "text/xml");
	    	
	    	connection.setReadTimeout(10*1000);
	    	connection.connect();
	    	
	    	reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      	stringBuilder = new StringBuilder();
	 
	      	String line = null;
	      	while ((line = reader.readLine()) != null)
	      	{
	        	stringBuilder.append(line + "\n");
	      	}
	      	String xml = stringBuilder.toString();

	      	reader.close();
	      	response.setContentType("text/xml");
	      	PrintWriter out = response.getWriter();
	      	out.print(xml);
     	}
    	catch (Exception e){
    		System.err.println("error");
    	}


    }
}
