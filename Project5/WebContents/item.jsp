<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <link href="css/menu.css" rel="stylesheet">
	<script src="item.js"></script>
	<script type="text/javascript" 
	    src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script>  

	<title>Item Lookup</title>
</head>

<body onload="init()"> 
	<div id="search">
        <form action="http://localhost:1448/eBay/item" method="get">
        	<div id="search-box">
            	<input type="text" name="id" class="form-control" placeholder="Search by item id">
            </div>
            <button type="submit" class="btn btn-primary" id="searchButton"><span class="glyphicon glyphicon-search"></span></button>
        </form>
    </div>
	<% Item item = (Item) request.getAttribute("message"); %>	
	<div id="header">
		<div class="userInfo">
			<div class="glyphicon glyphicon-user"></div>
			<p><% out.println(item.sellerId); %></p>
		</div>
		<div id="headerInfo">
			<h2 id="itemName">
				<% out.println(item.name); %>
			</h2>
			<p id="location">
				<% out.println("<span id=\"location\" data-location=\"" + item.location + "," + item.country + "\">" + item.location + "," + item.country + "</span>"); %>
				<c:forEach begin="1" end="${Integer.parse(item.sellerRating)/10000}" varStatus="loop">
					<span class="glyphicon glyphicon-star"></span>
				</c:forEach>
				<c:forEach begin="${Integer.parse(item.sellerRating)/10000+1}" end="5" varStatus="loop">
					<span class="glyphicon glyphicon-star-empty"></span>
				</c:forEach>
				<% out.println("<span>(" + item.sellerRating + ")</span>"); %>
			</p>
		</div>
	</div>

	<div id="content-wrapper">
		<div id="content">
			<b style="font-size: 1.25em;">About Item (<% out.println(item.itemId); %>)</b>
			<div id="description">
				<p><% out.println(item.description); %></p>
			</div>
			<hr>
			<div>
				<div class="infoType">
					Categories
				</div>
				<div class="info">
					 <table>
					 	<%
					 		for (int i = 0; i < item.categories.size(); i++) {
					 			if (i % 2 == 0) {
					 				out.println("<tr>");
					 				out.println("<td>" + item.categories.get(i) + "</td>");
					 			} else {
									out.println("<td>" + item.categories.get(i) + "</td>");
									out.println("</tr>");
					 			}
					 		}
					 	%>
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
							<td>First Bid: <% out.println(item.firstBid); %></td>
							<td>Buy Price: <% out.println(item.buyPrice); %></td>
						</tr>
						<tr>
							<td>Currently: <% out.println(item.currently); %></td>
							<td><% 
									if (item.buyPrice.length() >= 0) {
										out.println("<form action=\"https://localhost:8443/eBay/payment\" method=\"get\">");
										out.println("<button type=\"submit\" class=\"btn btn-primary\" id=\"payNow\">Pay Now</button>");
										out.println("</form>");
									}
								%>
							</td>
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
							<td>Started: <% out.println(item.started); %></td>
							<td>Ends: <% out.println(item.ends); %> </td>
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
							<td>Latitude: <% out.println("<span id=\"latitude\" data-latitude=\"" + item.latitude + "\">" + item.latitude + "</span>"); %></td>
							<td>Longitude: <% out.println("<span id=\"longitude\" data-longitude=\"" + item.longitude + "\">" + item.longitude + "</span>"); %></td>
						</tr>
						<tr>
							<td>Location: <% out.println("<span id=\"location\" data-location=\"" + item.location + "," + item.country + "\">" + item.location + "," + item.country + "</span>"); %> </td>
						</tr>
					</table> 
				</div>
			</div>
		</div>
	</div>
	<div id="bid-wrapper">
		<div id="bids">
			<b style="font-size: 1.25em;"><% out.println(item.numBids); %> Bids</b>
			<% 
				for (int i = 0; i < Integer.parseInt(item.numBids); i++) {
					Bid bid = item.bids.get(i);
					out.println("<hr>");
					out.println("<div class=\"userInfo\">");
					out.println("<div class=\"glyphicon glyphicon-user\"></div>");
					out.println("<p>" + bid.userId +"</p>");
					out.println("</div>");
					out.println("<div class=\"bidderInfo\">");
					out.println("<b style=\"font-size: 1.25em;\">" + bid.time + "</b>");
					out.println("<p>" + bid.amount + "</p>");
					out.println("<div class=\"bidderLocation\">" + bid.location + "," + bid.country + "</div>");
					out.println("<div class=\"bidderRating\">");
					for (int rating = 0; rating < Integer.parseInt(bid.rating)/10000; rating++) {
						out.println("<span class=\"glyphicon glyphicon-star\"></span>");
					}
					for (int rating = Integer.parseInt(bid.rating)/10000+1; rating < 5; rating++) {
						out.println("<span class=\"glyphicon glyphicon-star-empty\"></span>");
					}
					out.println("<span>(" + bid.rating + ")</span>");
					out.println("</div></div>");
				}
			%>
		</div>
	</div>
	<div id="map-wrapper">
		<div id="map_canvas"></div>
	</div>
</body>

</html>