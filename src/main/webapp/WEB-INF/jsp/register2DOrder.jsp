<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
  
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's 2D Maze Order Registration</title>
	</head>
	<body>
		<h1>Tim's 2D Maze Order Registration</h1>
		<f:form method="post" action="save" modelAttribute="order" onsubmit="disableButton()">
			<f:hidden path="orderID"></f:hidden>
			<p>CREATION/EDIT DATE SET AUTOMATICALLY:<f:radiobutton path="orderDate" value="<%=new SimpleDateFormat(\"MM-dd, yyyy @ hh:mm:ss a, zzzz\").format(new java.util.Date())%>" checked="checked" disabled="disabled"></f:radiobutton></p>
			<p>Recipient (email address):<f:input path="orderEmailAddress" type="text" required="true"></f:input></p>
			<p>Quantity:<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="1" checked="checked" required="true"></f:radiobutton> 1
				<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="10" required="true"></f:radiobutton> 10
				<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="100" required="true"></f:radiobutton> 100
			</p>
			<p>
				Size:
				<f:select path="size.sizeID" required="true">    
					<option value="">Select Size:</option>
					<c:forEach items="${sizes}" var="size">
						<option value="${size.getSizeID()}" ${order.getSize().getSizeID() == size.getSizeID() ? 'selected="selected"' : ''}>${size.getSizeAlias()}</option>
					</c:forEach>
				</f:select>
			</p>
			<p>
				Theme:
				<f:select path="theme.themeID" required="true">    
					<option value="">Select Theme:</option>
					<c:forEach items="${themes}" var="theme">
						<option value="${theme.getThemeID()}" ${order.getTheme().getThemeID() == theme.getThemeID() ? 'selected="selected"' : ''}>${theme.getThemeAlias()}</option>
					</c:forEach>
				</f:select>
			</p>
			<f:hidden path="solvedImages"></f:hidden>
			<f:hidden path="unsolvedImages"></f:hidden>
			<f:hidden path="fullfilled"></f:hidden>
			<p><Button id="btnSubmit" type="Submit">Submit</Button> | <a href="../orders2D/">Cancel</a></p>
		</f:form>
		<script>
			function disableButton() {
				document.getElementById('btnSubmit').disabled = true;
			}
		</script>
	</body>
</html>