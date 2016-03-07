package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            // your codes here
        	String parsedQ = "";
            int parsedNumSkip = 0;
            int parsedNumReturn = 0;
        	parsedQ = request.getParameter("q");
            parsedNumSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
            parsedNumReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
            if(parsedNumReturn == 0)
                parsedNumReturn = 100000;
        	SearchResult[] sr = AuctionSearchClient.basicSearch(parsedQ, parsedNumSkip, parsedNumReturn);
            request.setAttribute("query", parsedQ);
            request.setAttribute("numSkip", parsedNumSkip);
            request.setAttribute("numReturn", parsedNumReturn);
            request.setAttribute("message", sr);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/key.jsp");
            rd.forward(request,response);
        }
        catch (Exception e){
            System.err.println("error");
        }
    }
}
