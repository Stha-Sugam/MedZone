<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		<h1 class = "wel">Admin Dashbaord</h1>
		<h2 class = "wel down">Welcome, <span>${username}</span></h2>
		<div class = "stat-container">
			<div class = "stat-section">
				<div class = "statistic">
					<p class = "stat-head">Total Users</p>
					<h4>TOTAL USERS: ${userCount}</h4>
					<a href = "${pageContext.request.contextPath}/ManageUser">Manage Users</a>
				</div>
				<div class = "statistic">
					<p class = "stat-head">Total Medicine</p>
					<h4>TOTAL MEDICINES: ${medCount}</h4>
					<a class = "manageMed" href = "${pageContext.request.contextPath}/ManageMed">Manage Medicines</a>
				</div>
				<div class = "statistic">
					<p class = "stat-head">Open Tickets</p>
					<h4>TOTAL TICKETS: ${ticketCount}</h4>
					<h4>OPEN TICKETS: ${OpenTicketCount}</h4>
					<a class = "manageMed" href = "${pageContext.request.contextPath}/ManageTicket">Manage Tickets</a>
				</div>
			</div>
			<div class = "stat-section">
				<div class = "statistic middle">
					<p class = "stat-head">Most Active User</p>
					<h4>Username: ${mostActive}</h4>
				</div>
				<div class = "statistic middle">
					<p class = "stat-head">Most Viewed Medicine</p>
					<h4>Most Viewed: ${mostViewed}</h4>
				</div>
				<div class = "statistic middle">
					<p class = "stat-head">Tickets Created</p>
					<h4>Last Week: ${lastSeven}</h4>
				</div>
			</div>
			<div class = "stat-section">
				<div class = "statistic down">
					<p class = "stat-head">Recently Registered Users</p>
					<table>
						<tr>
							<th>Username</th>
							<th>FullName</th>
						</tr>
						<c:forEach var = "recentUser" items = "${recentUsers}">
							<tr>
								<td><p class = "stat-data">${recentUser.userName}</p></td>
								<td><p class = "stat-data">${recentUser.firstName} ${recentUser.lastName}</p></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class = "statistic down">
					<p class = "stat-head">Recently Registered Medicines</p>
					<table>
						<tr>
							<th>Medicine Id</th>
							<th>Name</th>
						</tr>
						<c:forEach var = "recentMed" items = "${recentMeds}">
							<tr>
								<td><p class = "stat-data">${recentMed.id}</p></td>
								<td><p class = "stat-data">${recentMed.name}</p></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class = "statistic down">
					<p class = "stat-head">Recently created Tickets</p>
					<table>
						<tr>
							<th>Username</th>
							<th>Ticket Id</th>
							<th>status</th>
						</tr>
						<c:forEach var = "recentsTickets" items = "${recentTickets}">
							<tr>
								<td><p class = "stat-data">${recentsTickets.username}</p></td>
								<td><p class = "stat-data">${recentsTickets.ticketId}</p></td>
								<td><p class = "stat-data">${recentsTickets.status}</p></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>