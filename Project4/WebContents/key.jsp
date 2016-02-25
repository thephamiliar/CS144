<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
</head>

<body>
    <div style="text-align: center;">
        <form action="http://localhost:1448/eBay/search" method="get">
	       <h3>Keyword Search</h3>
	       <p>Search: <input type="text" name="q"></p>
           <input type="hidden" name="numResultsToSkip" value=0>
           <input type="hidden" name="numResultsToReturn" value=30>
         <input type="submit" value="Submit">
        </form>
    </div>
<%
SearchResult[] sr = (SearchResult[]) request.getAttribute("message");
for (SearchResult s : sr){
	out.println("<a href=\"/eBay/item?id=" + s.getItemId() + "\">"+ s.getName() + "</a><br>");
}
	int numSkipInt = (Integer) request.getAttribute("numSkip");
	int numReturnInt = (Integer) request.getAttribute("numReturn");
	if(numSkipInt != 0){
	 	int newSkip = numSkipInt - numReturnInt;
	 	out.println("<a href=\"/eBay/search?q=" + request.getAttribute("query") 
	 						+ "&numResultsToSkip=" + newSkip
	 						+ "&numResultsToReturn=" + request.getAttribute("numReturn")
	 						+ "\">Previous</a>");
	}
	if(numReturnInt == sr.length){
		int newSkip = numSkipInt + numReturnInt;
		out.println("<a href=\"/eBay/search?q=" + request.getAttribute("query") 
							+ "&numResultsToSkip=" + newSkip
							+ "&numResultsToReturn=" + request.getAttribute("numReturn")
							+ "\">Next</a>");
	}
%>
</body>

</html>