<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/about shortcut.png">
<title>About</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/About.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class = "about-container">
			<div class = "about-head">
				<div class = "about-image">
					<img class = "img" src = "${pageContext.request.contextPath}/resources/images/text logo with tagline.png">
				</div>
				<div class = "head-heading">
					<h1>ABOUT MEDZONE</h1>
					<h3>Welcome to MedZone, your trusted resource for comprehensive and easily accessible medicine information.</h3>
				</div>
			</div>
			<br>
			<h2>Our Mission</h2>
			<h3>At MedZone, our mission is to empower individuals with the knowledge they need to understand their medications 
			and make informed decisions about their health. We strive to provide clear, concise, and reliable information 
			about a wide range of medicines, helping you navigate the complexities of health care.</h3>
			<br>
			<h2>Our Story</h2>
			<h3>MedZone was founded with the belief that everyone deserves access to accurate and understandable information about 
			the medicines they use. Recognizing the challenges individuals face in finding reliable details about their prescriptions 
			and over-the-counter medications, we set out to create a user-friendly platform that simplifies this process.</h3>
			<br>
			<h2>Why Choose MedZone?</h2>
			<ol>
				<li><h3><b>1. Easy to Understand:</b> we present complex medical information in a clear and straightforward manner, making it accessible to everyone.</h3></li>
				<li><h3><b>2. Comprehensive Database:</b> We aim to provide information on a wide variety of medicines, constantly expanding our database to meet your needs.</h3></li>
				<li><h3><b>3. User Ratings and Feedbacks:</b> Our rating system allows you to see what other users have experienced with different
				medications, providing valuable insights.</h3></li>
			</ol>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>