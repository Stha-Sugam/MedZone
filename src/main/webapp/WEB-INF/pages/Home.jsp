<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Home.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/home shortcut.png">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class = "main-content">
			<div class = "left-side">
				<img src = "${pageContext.request.contextPath}/resources/images/homephoto.png">
			</div>
			<div class = "right-side">
				<h1 class = "wlcmsg">Welcome to <span class = "logoname">MEDZONE</span></h1>
				<h3 class = "paragraph">Discover reliable and up-to-date information about medicines at your fingertips. 
				Whether you're looking for dosage details, side effects, or general usage, MedZone makes it easy to find what you need. 
				Start your search now and make informed health choices with confidence.</h3>
				<c:choose>
					<c:when test = "${role ==  'admin'}">
						<a class = "redirect" href = "${pageContext.request.contextPath}/Admin">Start Managing Medicines</a>
					</c:when>
					<c:otherwise>
						<a class = "redirect" href = "${pageContext.request.contextPath}/Browse">Start Browsing Medicines</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<div class = "service-container">
			<h2 class = "services">Our Services</h2>
			<div class = "cardscontainer">
				<div class = "card">
					<img class = "card-img" src = "${pageContext.request.contextPath}/resources/images/medicine search.png">
					<h2>Medicine Search</h2>
					<h4>Instantly search for any medicine by name and access details such as usage, dosage, side effects, and precautions.</h4>
				</div>
				<div class = "card">
					<img class = "card-img" src = "${pageContext.request.contextPath}/resources/images/medicine encyclopedia.png">
					<h2>Medicine Encyclopedia</h2>
					<h4>Browse an extensive and well-organized database of medicines with detailed insights, including uses and more - all in one place.</h4>
				</div>
				<div class = "card">
					<img class = "card-img" src = "${pageContext.request.contextPath}/resources/images/smart suggestions.png">
					<h2>Smart Suggestions</h2>
					<h4>This gives your site a more educational and resourceful vibe, perfect if you want to offer in-depth information beyond just search.</h4>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>