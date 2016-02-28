<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/menu.css" rel="stylesheet">
	<script src="item.js"></script>  
	<script type="text/javascript" 
	    src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script>  

	<title>Item Lookup</title>
</head>

<body onload="init()">
	<% Item item = (Item) request.getAttribute("message"); %>
	<div id="header">
		<div class="userInfo">
			<div class="glyphicon glyphicon-user"></div>
			<p><% out.println(item.sellerId); %></p>
		</div>
		<div id="headerInfo">
			<h2 id="itemName"><% out.println(item.name); %></h2>
			<p id="location">
				<% out.println("<span id=\"location\" data-location=\"" + item.location + "," + item.country + "\">" + item.location + ", " + item.country + "</span>"); %>
				<c:forEach begin="0" end="${Integer.parse(item.sellerRating)/1000}" varStatus="loop">
			        <span class="glyphicon glyphicon-star"></span>
				</c:forEach>
				<c:forEach begin="Integer.parse(item.sellerRating/1000)" end="${5}" varStatus="loop">
					<span class="glyphicon glyphicon-star-empty"></span>
				</c:forEach>
				<% out.println("<span>(" + item.sellerRating + ")</span>") %>
			</p>
		</div>
	</div>

	<div id="content-wrapper">
		<div id="content">
			<b style="font-size: 1.25em;">About Item <% out.println(item.itemId) %></b>
			<div id="description">
				<p><% out.println(item.description) %></p>
			</div>
			<hr>
			<div>
				<div class="infoType">
					Categories
				</div>
				<div class="info">
					 <table>
					 	<c:forEach var="i" begin="0" end="${fn:length(item.categories)}">
					 		<% 
					 			if (i % 2 == 0) {
						    		out.println("<tr>");     
						        	out.println("<td>" + item.categories[i] + "</td>"); 
					 			} else {    
						        	out.println("<td>" + item.categories[i] + "</td>");
						        	out.println("</tr>"); 
					 			}
					 		%>
						</c:forEach>
					</table> 
				</div>
			</div>
			<hr>
			<div>
				<div class="infoType">
					Prices
				</div>
				<div class="info">
					 <table>
						<tr>
							<td>Buy Price: <% out.println(item.buyPrice) %></td>
							<td>Currently: <% out.println(item.currently) %></td>
						</tr>
						<tr>
							<td>First Bid: <% out.println(item.firstBid) %></td>
						</tr>
					</table> 
				</div>
			</div>
			<hr>
			<div>
				<div class="infoType">
					Availability
				</div>
				<div class="info">
					 <table>
						<tr>
							<td>Started: <% out.println(item.started) %></td>
							<td>Ends: <% out.println(item.ends) %></td>
						</tr>
					</table> 
				</div>
			</div>
			<hr>
			<div>
				<div class="infoType">
					Location
				</div>
				<div class="info">
					 <table>
						<tr>
							<td>Latitude: <% out.println("<span id=\"latitude\" data-latitude=\"" + item.latitude + "\"></span>"); %></td>
							<td>Longitude: <% out.println("<span id=\"longitude\" data-longitude=\"" + item.longitude + "\"></span>"); %></td>
						</tr>
						<tr>
							<td>Location: <% out.println(item.location + ", " + item.country) %></td>
						</tr>
					</table> 
				</div>
			</div>
		</div>
	</div>
	<div id="bid-wrapper">
		<div id="bids">
			<b style="font-size: 1.25em;"><% out.println(item.numBids) %> Bids</b>
			<c:forEach items="${item.bids}" var="bid">
				<hr>
				<div class="userInfo">
					<div class="glyphicon glyphicon-user"></div>
					<p>${bid.userId}</p>
				</div>
				<div class="bidderInfo">
					<b style="font-size: 1.25em;">${bid.time}</b>
					<p>${bid.amount}</p>
					<div class="bidderLocation">
						${bid.location}, ${bid.country}
					</div>
					<div class="bidderRating">
						<c:forEach begin="0" end="${Integer.parse(bid.rating)/1000}" varStatus="loop">
					        <span class="glyphicon glyphicon-star"></span>
						</c:forEach>
						<c:forEach begin="Integer.parse(bid.rating/1000)" end="${5}" varStatus="loop">
							<span class="glyphicon glyphicon-star-empty"></span>
						</c:forEach>
						<% out.println("<span>(" + bid.rating + ")</span>") %>
					</div>
			    </div>
			</c:forEach>
		</div>
	</div>
	<div id="map-wrapper">
		<div id="map_canvas"></div>
	</div>
	<div id="search">
        <form action="http://localhost:1448/eBay/item" method="get">
            <input type="text" name="id" class="form-control" placeholder="Search by item id">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span></button>
        </form>
    </div>
</body>

</html>