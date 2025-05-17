<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Ticket</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/TicketManagement.css">
	<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
	<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/hortcut.png">
	<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class = "success-messages" id = "success-messages">
			<h1 class = "success-msg">
			<c:if test = "${not empty successMessage}">
				${successMessage}
			</c:if>
			<c:remove var="successMessage" scope="session"/>
			</h1>
		</div>
		<div class = "error-messages" id = "error-messages">
			<h1 class = "error-msg">
			<c:if test = "${not empty errorMessage}">
				${errorMessage}
			</c:if>
			<c:remove var = "errorMessage" scope = "session"/>
			</h1>
		</div>
		<div class = "return-container">
			<a href = "${pageContext.request.contextPath}/Admin">Return to Dashboard</a>
		</div>
		<div class="table-container">
		    <table>
		        <thead>
		            <tr>
		                <th>Username</th>
		                <th>TicketId</th>
		                <th>subject</th>
		                <th>message</th>
		                <th>created Date</th>
		                <th>status</th>
		            </tr>
		        </thead>
		        <tbody>
		      		<c:forEach var = "ticket" items = "${TicketDetails}">
		           		<tr>
		                    <td>${ticket.username}</td>
		                    <td>${ticket.ticketId}</td>    
		                    <td>${ticket.subject}</td>
		                    <td>${ticket.message}</td>
		                    <td>${ticket.createdDate}</td>
		                    <td>
		                    	<form action="ManageTicket" method="post">
							        <input type="hidden" name="ticketId" value="${ticket.ticketId}" />
							        <select name="status" onchange="this.form.submit()">
							            <option value="Open" ${ticket.status == 'open' ? 'selected' : ''}>Open</option>
							            <option value="Closed" ${ticket.status == 'Closed' ? 'selected' : ''}>Closed</option>
							        </select>
							    </form>
		                    </td>
		                </tr>
		        	</c:forEach>
		        </tbody>
		    </table>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
	<script>
		document.addEventListener("DOMContentLoaded", () => {
		    const success = document.getElementById("success-messages");
		    if (success && success.innerText.trim().length > 0) {
		        success.classList.add("active");
		        setTimeout(() => success.classList.remove("active"), 5000);
		    }
	
		    const error = document.getElementById("error-messages");
		    if (error && error.innerText.trim().length > 0) {
		        error.classList.add("active");
		        setTimeout(() => error.classList.remove("active"), 5000);
		    }
		});
	</script>
</body>
</html>