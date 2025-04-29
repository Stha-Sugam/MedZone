<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Contact.css">
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
		<h1 class = "heading">GET IN TOUCH WITH US</h1>
		<h3>Want to get in touch with us? We'd love to hear from you. Here's how you can reach us.</h3>
		<h4 class = "paragraph">At MedZone, we are dedicated to providing you with accurate and reliable medicine information.
		If you have any questions, feedback, or require assistance, please don't hesitate to reach out to our administrator.
		We value your input and will do our best to respond to your inquiries promptly.</h4>
		<br>
		<div class = "contact-num">
			<h2>Contact our Admin</h2>
			<h4>Email Address: administrator@medzone.com</h4>
			<h4>Landline: 01-4567653</h4>
			<h4>Contact Info: +977 9874567364</h4>
		</div>
		<br>
		<h2 class = "response">What you Get when asking your Question</h2>
		<ol>
			<li><h4>1. Less than 24-hour response to your question.</h4></li>
			<li><h4>2. Throughness and expertise of a Certified Exchange Specialist.</h4></li>
			<li><h4>3. Plan of action summarized in an email follow up.</h4></li>
		</ol>
		<div class = "contact-us">
			<h2 class = "form-heading">CONTACT US</h2>
			<form id = "contact" action = "Contact" method = "post">
				<div class = "subject">
					<label class = "">Subject</label>
					<input class = "" type = "text" name = "subject" id = "subject" placeholder = "Enter your subject of contact">
				</div>
				<div class = "message">
					<label class = "">Message</label>
					<textarea id="message" name="message" rows="8" cols="50" placeholder="Type your message here..."></textarea>
				</div>
				<div class = "actions">
					<button type = "submit">Submit</button>
				</div>
			</form>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>