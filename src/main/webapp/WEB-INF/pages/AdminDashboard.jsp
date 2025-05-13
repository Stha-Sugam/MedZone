<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/AdminDashboard.css">
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
		<div class = "stat-container">
			<div class = "stat-section">
				<div class = "statistic">
					<p>Total Users</p>
					<h4>USERS COUNT: ${userCount}</h4>
					<a href = "${pageContext.request.contextPath}/ManageUser">Manage Users</a>
				</div>
				<div class = "statistic">
					<p>Total Medicine</p>
					<h4>MEDICINES COUNT: ${medCount}</h4>
					<a class = "manageMed" href = "${pageContext.request.contextPath}/ManageMed">Manage Medicines</a>
				</div>
				<div class = "statistic">
					<p>Pending Tickets</p>
					
					<a class = "manageMed" href = "${pageContext.request.contextPath}/ManageTickets">Manage Tickets</a>
				</div>
			</div>
			<div class = "stat-section">
				<div class = "statistic">
					<p>Recently Registered Users</p>
				</div>
				<div class = "statistic">
					<p>Recently Registered Medicines</p>
				</div>
				<div class = "statistic">
					<p>Recently created Tickets</p>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>