<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's 2D Maze Orders</title>
	</head>
	<body>
		<p><a href="..">Tim's Maze World</a></p>
		<h1>Tim's 2D Maze Orders</h1>
		<p><a href="add">Add New Order</a> ~ <a href="themes/">View Available Themes</a> | <a href="sizes/">View Available Sizes</a></p>
		<table width="50%" border="1" cellspacing="1" cellpadding="10">
			<tr>
				<th>Order ID</th>
				<th>Order Creation/Edit Date</th>
				<th>Order Email Address</th>
				<th>Order Quantity</th>
				<th>Theme ID</th>
				<th>Theme Name</th>
				<th>Size ID</th> 
				<th>Size Name</th>
				<th>Puzzle(s)</th>
				<th>Solution(s)</th>
				<th>Deliver</th>
				<th>Completed</th>
			</tr>
			<c:forEach var="vorder" items="${listorders}">
				<c:catch>
					<tr>
						<td>${vorder.getOrderID()}</td>
						<td>${vorder.getOrderDate()}</td>
						<td>${vorder.getOrderEmailAddress()}</td>
						<td>${vorder.getOrderQuantity()}</td>
						<td>${vorder.getTheme().getThemeID()}</td>
						<td>${vorder.getTheme().getThemeAlias()}</td>
						<td>${vorder.getSize().getSizeID()}</td>
						<td>${vorder.getSize().getSizeAlias()}</td>	
						<td><a href="downloadPuzzles/${vorder.getOrderID()}">Download Zip</a></td>
						<td><a href="downloadSolutions/${vorder.getOrderID()}">Download Zip</a></td>
						<td><a href="send/${vorder.getOrderID()}">deliver</a></td>
						<td>${vorder.isFullfilled()}</td>
					</tr>	
				</c:catch>
			</c:forEach>
		</table>
		
	</body>
</html>