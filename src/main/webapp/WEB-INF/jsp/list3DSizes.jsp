<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's Sizes (3D)</title>
	</head>
	<body>
		<p><a href="..">Tim's 3D Maze Orders</a></p>
		<h1>Tim's Sizes (3D)</h1>
		<p><a href="add">Add New Size</a></p>
		<table width="50%" border="1" cellspacing="1" cellpadding="10">
			<tr>
				<th>Size ID</th>
				<th>Size Name</th>
				<th>Size Creation/Edit Date</th>
				<th>Number of Columns</th> 
				<th>Number of Rows</th> 
				<th>Number of Layers</th> 
				<th>Thickness of Walls</th>
				<th>Thickness of Cells</th>
			</tr>
			<c:forEach var="vsize" items="${listSizes}">
				<tr>
					<td>${vsize.getSizeID()}</td>
					<td>${vsize.getSizeAlias()}</td>
					<td>${vsize.getSizeDate()}</td>
					<td>${vsize.getColumnCount()}</td>
					<td>${vsize.getRowCount()}</td>
					<td>${vsize.getLayerCount()}</td>
					<td>${vsize.getWallSize()}</td>
					<td>${vsize.getCellSize()}</td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>