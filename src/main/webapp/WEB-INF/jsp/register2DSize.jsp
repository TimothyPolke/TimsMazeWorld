<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's Size Registration (2D)</title>
	</head>
	<body>
		<h1>Tim's Size Registration (2D)</h1>
		<f:form method="post" action="save" modelAttribute="size" onsubmit="disableButton()">
			<f:hidden path="sizeID"></f:hidden>
			<p>Size Name:<f:input path="sizeAlias" type="text" required="true"></f:input></p>
			<p>CREATION/EDIT DATE SET AUTOMATICALLY:<f:radiobutton path="sizeDate" value="<%=new SimpleDateFormat(\"MM-dd, yyyy @ hh:mm:ss a, zzzz\").format(new java.util.Date())%>" checked="checked" disabled="disabled"></f:radiobutton></p>
			<p><input type="radio" name="toggle" value="default" checked="checked" onclick="toggleElements(this.value)"></input><label for="default">DEFAULT</label><input type="radio" name="toggle" value="custom" onclick="toggleElements(this.value)"></input><label for="custom">CUSTOM</label></p>
			<div id="customcontrols" hidden="true">
				<p>
					Number of Columns (x value):
					<f:select path="columnCount" required="true">
						<c:forEach begin="5" end="100" step="1" var="columnCount">
							<option value="${columnCount}" ${columnCount == 10 ? 'selected="selected"' : ''}>${columnCount}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Number of Rows (y value):
					<f:select path="rowCount" required="true">
						<c:forEach begin="5" end="100" step="1" var="rowCount">
							<option value="${rowCount}" ${rowCount == 10 ? 'selected="selected"' : ''}>${rowCount}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Thickness of Walls:
					<f:select path="wallSize" required="true">
						<c:forEach begin="1" end="5" step="1" var="wallSize">
							<option value="${wallSize}" ${wallSize == 1 ? 'selected="selected"' : ''}>${wallSize}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Thickness of Cells:
					<f:select path="cellSize" required="true">
						<c:forEach begin="5" end="25" step="1" var="cellSize">
							<option value="${cellSize}" ${cellSize == 10 ? 'selected="selected"' : ''}>${cellSize}</option>
						</c:forEach>
					</f:select>
				</p>
			</div>	
			<p><Button id="btnSubmit" type="Submit">Submit</Button> | <a href="..">Cancel</a></p>
		</f:form>
		<script>
			function disableButton() {
				document.getElementById('btnSubmit').disabled = true;
			}
			function toggleElements(value){
				if (value == "default") {
					document.getElementById('customcontrols').style.display = "none";
				} 
				else if (value=="custom") {
					document.getElementById('customcontrols').style.display = "block";
				}
			}
		</script>
	</body>
</html>