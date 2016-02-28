<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/menu.css" rel="stylesheet">
    <link href="css/keyword.css" rel="stylesheet">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

	<!-- javascript -->
    <script src="suggest.js"></script>
    <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("inputQuery"), new StateSuggestions()); 
        }
    </script>
	<title>CS144: Keyword Search</title>
</head>

<body>
	 <div id="search">
        <form action="http://localhost:1448/eBay/search" method="get">
            <div id="search-box">
                <input id="inputQuery" type="text" name="q" class="form-control" placeholder="Search by keyword">
            </div>
            <input type="hidden" name="numResultsToSkip" value=0>
            <input type="hidden" name="numResultsToReturn" value=30>
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span></button>
        </form>
    </div>
	<div id="content-wrapper">
		<div id="content">
			<b style="font-size: 1.25em;">Search Results</b>
			<div id="results">
				<%
					SearchResult[] sr = (SearchResult[]) request.getAttribute("message");
					for (SearchResult s : sr){
						out.println("<a href=\"/eBay/item?id=" + s.getItemId() + "\">"+ s.getName() + "</a><br>");
					}
					int numSkipInt = (Integer) request.getAttribute("numSkip");
					int numReturnInt = (Integer) request.getAttribute("numReturn");
					if(numSkipInt != 0){
					 	int newSkip = numSkipInt - numReturnInt;
					 	out.println("<div id=\"previous\"><a href=\"/eBay/search?q=" + request.getAttribute("query") 
					 						+ "&numResultsToSkip=" + newSkip
					 						+ "&numResultsToReturn=" + request.getAttribute("numReturn")
					 						+ "\">Previous</a></div>");
					}
					if(numReturnInt == sr.length){
						int newSkip = numSkipInt + numReturnInt;
						out.println("<div id=\"next\"><a href=\"/eBay/search?q=" + request.getAttribute("query") 
											+ "&numResultsToSkip=" + newSkip
											+ "&numResultsToReturn=" + request.getAttribute("numReturn")
											+ "\">Next</a></div>");
					}
				%>
			</div>
		</div>
	</div>
</body>

</html>