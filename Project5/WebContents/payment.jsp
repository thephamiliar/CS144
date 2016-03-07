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

	<script src="item.js"></script>
	<script type="text/javascript" 
	    src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script>  

	<title>Credit Card Input Page</title>
</head>

<body onload="init()">
	<% Item item = (Item) request.getAttribute("message"); %>	
	<div id="content-wrapper">
		<div id="content">
			<div>
				<div class="infoType">
					Payment
				</div>
				<div class="info">
					 <table>
						<tr>
							<td>ItemID: <% out.println(item.itemId); %></td>
							<td>Item Name: <% out.println(item.name); %></td>
							<td>Buy Price: <% out.println(item.buyPrice); %></td>
							<td>
								<form action="">
									<input id="paymentQuery" type="text" name="q" class="form-control" placeholder="Credit Card Number">
									<button type="submit" class="btn btn-primary" id="payNow">Pay Now</button>
								</form>
							</td>
						</tr>
					</table> 
				</div>
			</div>
		</div>
	</div>
</body>

</html>