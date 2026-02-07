<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Tim's Theme Registration (2D)</title>
	</head>
	<body>
		<h1>Tim's Theme Registration (2D)</h1>
		<f:form method="post" action="save" modelAttribute="theme" onsubmit="disableButton()" onreset="document.getElementById('customcontrols').style.display = 'none'">
			<f:hidden path="themeID"></f:hidden>
			<p>Theme Name:<f:input path="themeAlias" type="text" required="true"></f:input></p>
			<p>CREATION/EDIT DATE SET AUTOMATICALLY:<f:radiobutton path="themeDate" value="<%=new SimpleDateFormat(\"MM-dd, yyyy @ hh:mm:ss a, zzzz\").format(new java.util.Date())%>" checked="checked" disabled="disabled"></f:radiobutton></p>
			<p><input type="radio" name="toggle" value="default" checked="checked" onclick="toggleElements(this.value)"></input><label for="default">DEFAULT</label><input type="radio" name="toggle" value="custom" onclick="toggleElements(this.value)"></input><label for="custom">CUSTOM</label></p>
			<div id="customcontrols" hidden="true">
				<p>Wall Color:<f:input path="foreground" type="color" value="#000000" required="true"></f:input></p>
				<p>Background Color:<f:input path="background" type="color" value="#ffffff" required="true"></f:input></p>
				<p>Highlight Color:<f:input path="highlight" type="color" value="#ffff00" required="true"></f:input></p>
			</div>
			<p><Button id="btnSubmit" type="Submit">Submit</Button> | <Button type="Reset">Reset</Button> | <a href="..">Cancel</a></p>
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