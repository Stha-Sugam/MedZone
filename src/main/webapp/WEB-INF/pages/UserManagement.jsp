<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Users</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/UserManagement.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/admindashboardshortcut.png">
	<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class = "return-container">
			<a href = "${pageContext.request.contextPath}/Admin">Return to Dashboard</a>
		</div>
		<div class="table-container">
		    <table>
		        <thead>
		            <tr>
		                <th>Username</th>
		                <th>Full Name</th>
		                <th>Phone No.</th>
		                <th>Email</th>
		                <th>Registration Date</th>
		                <th>Actions</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var = "user" items = "${usersDetails}">
		                <tr>
		                    <td>${user.userName}</td>
		                    <td>${user.firstName} ${user.lastName}</td>    
		                    <td>${user.phone}</td>
		                    <td>${user.email}</td>
		                    <td>${user.registrationDate}</td>
		                    <td> 
		                        <form action = "ManageUser" method = "post">
		                            <button type = "submit">Remove</button>
		                        </form>
		                    </td>
		                </tr>
		            </c:forEach>
		        </tbody>
		    </table>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>