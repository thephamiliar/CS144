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

	<title>Confirmation Page</title>
</head>

<body>
	<div id="content-wrapper">
		<div id="content">
			<div>
				<div class="infoType">
					Payment
				</div>
				<div class="info">
					 <table>
						<tr>
							<td>ItemID: <% out.println(request.getAttribute("ItemID")); %></td>
							<td>Item Name: <% out.println(request.getAttribute("ItemName")); %></td>
							<td>Buy Price: <% out.println(request.getAttribute("Buy_Price")); %></td>
							<td>Credit Card: <% out.println(request.getAttribute("CreditCard")); %></td>
							<td>Time: <% out.println((request.getAttribute("TimeStamp")); %></td>
						</tr>
					</table> 
				</div>
			</div>
		</div>
	</div>
</body>

</html>