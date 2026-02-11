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
		<f:form method="post" action="save" modelAttribute="order" onsubmit="disableButton()" onreset="resetCustomControls()">
			<f:hidden path="orderID"></f:hidden>
			<p>CREATION/EDIT DATE SET AUTOMATICALLY:<f:radiobutton path="orderDate" value="<%=new SimpleDateFormat(\"MM-dd, yyyy @ hh:mm:ss a, zzzz\").format(new java.util.Date())%>" checked="checked" disabled="disabled"></f:radiobutton></p>
			<p>Recipient (email address):<f:input path="orderEmailAddress" type="text" required="true"></f:input></p>
			<p>Quantity:<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="1" checked="checked" required="true"></f:radiobutton> 1
				<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="10" required="true"></f:radiobutton> 10
				<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="100" required="true"></f:radiobutton> 100
				<br/>
				<f:radiobutton path="orderQuantity" name="quantity" value="1000" required="true"></f:radiobutton> 1000
			</p>
			<p>Sizes: <input type="radio" name="togglesizes" value="defaultsizes" checked="checked" onclick="toggleSizes(this.value)"></input><label for="defaultsize">DEFAULT</label>   <input type="radio" name="togglesizes" value="customsizes" onclick="toggleSizes(this.value)"></input><label for="customsize">CUSTOM</label></p>
			<div id="customsizecontrols" hidden="true">
				<p>
					Number of Columns (X AXIS):
					<f:select path="columnCount"  required="true">
						<c:forEach begin="5" end="10" step="1" var="columnCount">
							<option value="${columnCount}" ${columnCount == 10 ? 'selected="selected"' : ''}>${columnCount}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Number of Rows (Y AXIS):
					<f:select path="rowCount"  required="true">
						<c:forEach begin="5" end="10" step="1" var="rowCount">
							<option value="${rowCount}" ${rowCount == 10 ? 'selected="selected"' : ''}>${rowCount}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Thickness of Walls (PIXELS):
					<f:select path="wallSize" required="true">
						<c:forEach begin="1" end="5" step="1" var="wallSize">
							<option value="${wallSize}" ${wallSize == 1 ? 'selected="selected"' : ''}>${wallSize}</option>
						</c:forEach>
					</f:select>
				</p>
				<p>
					Thickness of Cells (PIXELS):
					<f:select path="cellSize" required="true">
						<c:forEach begin="5" end="25" step="1" var="cellSize">
							<option value="${cellSize}" ${cellSize == 10 ? 'selected="selected"' : ''}>${cellSize}</option>
						</c:forEach>
					</f:select>
				</p>
			</div>
			<p>Colors: <input type="radio" name="togglecolors" value="defaultcolors" checked="checked" onclick="toggleColors(this.value)"></input><label for="defaultcolors">DEFAULT</label>   <input type="radio" name="togglecolors" value="customcolors" onclick="toggleColors(this.value)"></input><label for="customcolors">CUSTOM</label></p>
			<div id="customcolorcontrols" hidden="true">
				<p>Wall Color:<f:input path="foreground" type="color" value="#000000" required="true"></f:input></p>
				<p>Background Color:<f:input path="background" type="color" value="#ffffff" required="true"></f:input></p>
				<p>Highlight Color:<f:input path="highlight" type="color" value="#ffff00" required="true"></f:input></p>
			</div>
			<f:hidden path="solvedImages"></f:hidden>
			<f:hidden path="unsolvedImages"></f:hidden>
			<f:hidden path="fullfilled"></f:hidden>
			<p><Button id="btnSubmit" type="Submit">Submit</Button> | <Button type="Reset">Reset</Button> | <a href="../orders2D/">Cancel</a></p>
		</f:form>
		<script>
			function disableButton() {
				document.getElementById('btnSubmit').disabled = true;
			}
			function toggleSizes(value){
				if (value == "defaultsizes") {
					document.getElementById('customsizecontrols').style.display = "none";
				} 
				else if (value == "customsizes") {
					document.getElementById('customsizecontrols').style.display = "block";
				}
			}
			function toggleColors(value){
				if (value == "defaultcolors") {
					document.getElementById('customcolorcontrols').style.display = "none";
				} 
				else if (value == "customcolors") {
					document.getElementById('customcolorcontrols').style.display = "block";
				}
			}
			function resetCustomControls(){
				document.getElementById('customsizecontrols').style.display = "none";
				document.getElementById('customcolorcontrols').style.display = "none";
			}
		</script>
	</body>
</html>