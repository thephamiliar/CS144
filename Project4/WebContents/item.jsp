<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="style.css">

	<script src="item.js"></script>  
	<script type="text/javascript" 
	    src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script>  

	<title>Item Lookup</title>
</head>

<body onload="init()">
    <div style="text-align: center;">
        <form action="http://localhost:1448/eBay/item" method="get">
	       <h3>Item Search</h3>
	       <p>ItemID: <input type="text" name="id"></p>
         <input type="submit" value="Submit">
        </form>
    </div>
	<h1>
		<%
			Item item = (Item) request.getAttribute("message");
			out.println(item.name);
			//out.println(request.getAttribute("xml"));
		%>
	</h1>
	<% out.println("<div id=\"location\" data-location=\"" + item.location + "," + item.country + "\">" + item.location + "," + item.country + "</div>"); %>
	<% out.println("<div id=\"latitude\" data-latitude=\"" + item.latitude + "\">" + item.latitude + "</div>"); %>
	<% out.println("<div id=\"longitude\" data-longitude=\"" + item.longitude + "\">" + item.longitude + "</div>"); %>
	<div id="longitude"><% out.println(item.longitude); %> </div>
	<div id="map_canvas"> </div> 

</body>

</html>