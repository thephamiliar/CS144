package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.*;
import org.w3c.dom.*; 
import java.io.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String id = request.getParameter("id");
        String xml = AuctionSearchClient.getXMLDataForItemId(id);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder;  
	    Document document = null;
	    try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse( new InputSource( new StringReader( xml ) ) );  
	        Item item = MyParser.processDoc(document);
	        request.setAttribute("message", item);
	        //request.setAttribute("xml", xml);
	        RequestDispatcher rd = getServletContext().getRequestDispatcher("/item.jsp");
	        rd.forward(request,response);

		} catch (Exception e) {  
	        e.printStackTrace();  
	    } 
	    //String parsedName = document.getElementsByTagName("Name").item(0).getTextContent();

    }
}
