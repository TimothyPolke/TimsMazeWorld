<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %> 

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's Themes (2D)</title>
	</head>
	<body>
		<p><a href="..">Tim's 2D Maze Orders</a></p>
		<h1>Tim's Themes (2D)</h1>
		<p><a href="add">Add New Theme</a></p>
		<table width="50%" border="1" cellspacing="1" cellpadding="10">
			<tr>
				<th>Theme ID</th>
				<th>Theme Name</th>
				<th>Theme Creation/Edit Date</th>	
				<th>Wall Color</th>
				<th>Background Color</th>
				<th>Highlight Color</th>
			</tr>
			<c:forEach var="vtheme" items="${listThemes}">
				<tr>
					<td>${vtheme.getThemeID()}</td>
					<td>${vtheme.getThemeAlias()}</td>
					<td>${vtheme.getThemeDate()}</td>
					<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vtheme.getForeground()}"></div></center></td>
					<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vtheme.getBackground()}"></div></center></td>
					<td><center><div style="width:50px; height:50px; border:1px solid black; background-color:${vtheme.getHighlight()}"></div></center></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>