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
		<p><a href="add">Add New Order</a></p>
		<table width="50%" border="1" cellspacing="1" cellpadding="10">
			<tr>
				<th>Order ID</th>
				<th>Order Creation/Edit Date</th>
				<th>Order Email Address</th>
				<th>Order Quantity</th>
				<th>Number of Columns (X AXIS)</th>
				<th>Number of Rows (Y AXIS)</th> 
				<th>Thickness of Walls</th>
				<th>Thickness of Cells</th>
				<th>Wall Color</th>
				<th>Background Color</th>
				<th>Highlight Color</th>
				<th>Puzzle(s)</th>
				<th>Solution(s)</th>
				<th>Action</th>
				<th>Status</th>
			</tr>
			<c:forEach var="vorder" items="${listorders}">
				<c:catch>
					<tr>
						<td>${vorder.getOrderID()}</td>
						<td>${vorder.getOrderDate()}</td>
						<td>${vorder.getOrderEmailAddress()}</td>
						<td>${vorder.getOrderQuantity()}</td>
						<td>${vorder.getColumnCount()}</td>
						<td>${vorder.getRowCount()}</td>
						<td>${vorder.getWallSize()}</td>
						<td>${vorder.getCellSize()}</td>
						<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vorder.getForeground()}"></div></center></td>
						<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vorder.getBackground()}"></div></center></td>
						<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vorder.getHighlight()}"></div></center></td>
						<td><a href="downloadPuzzles/${vorder.getOrderID()}">Download Zip</a></td>
						<td><a href="downloadSolutions/${vorder.getOrderID()}">Download Zip</a></td>
						<td><a href="send/${vorder.getOrderID()}" onclick="handleLinkClick(event,${vorder.isFullfilled()})">SEND</a> | <a href="delete/${vorder.getOrderID()}">DELETE</a></td>
						<td>
							<c:choose>
								<c:when test="${vorder.isFullfilled() == true}">DELIVERED</c:when>
								<c:when test="${vorder.isFullfilled() == false}">NOT DELIVERED</c:when>
							</c:choose>
						</td>
					</tr>	
				</c:catch>
			</c:forEach>
		</table>
		<script>
			function handleLinkClick(event,order){
				if (order === true){
					event.preventDefault();
					alert("This order has already been delivered!");
				}
			}
		</script>
	</body>
</html>