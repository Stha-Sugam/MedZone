<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		<div class = "image-contain">
			<img src = "${pageContext.request.contextPath}/resources/images/contactpagebanner.png">
		</div>
		<div class = "container-content">
			<div class = "admin-contact">
				<div class = "contact-num">
					<h2>Contact our Admin</h2>
					<h4>Email Address: administrator@medzone.com</h4>
					<h4>Landline: 01-4567653</h4>
					<h4>Contact Info: +977 9874567364</h4>
				</div>
				<br>
				<div class = "reponse-get">
					<h2 class = "response">What you Get when asking your Question</h2>
					<ol>
						<li><h4>1. Less than 24-hour response to your question.</h4></li>
						<li><h4>2. Throughness and expertise of a Certified Exchange Specialist.</h4></li>
						<li><h4>3. Plan of action summarized in an email follow up.</h4></li>
					</ol>
				</div>
			</div>
			<form id = "contact" action = "Contact" method = "post" class = "form">
				<h2 class = "form-heading">CREATE A TICKET</h2>
				<div class = "single-section">
					<div class = "inner-section">
						<label>Subject</label>
						<c:set var = "subjectError" value = "${requestScope.errorSubject}"/>
						<div class = "${empty subjectError ? 'normal-input' : 'error-input'}">
							<input type = "text" id = "subject" name = "subject" value="${subjectValue}" placeholder = "Enter the subject of the ticket">
						</div>
						<p class = "field-error">
							<c:choose>
								<c:when test = "${not empty subjectError}">
									${subjectError}
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>
						</p>
					</div>
				</div>
				<div class = "single-section">
					<div class = "inner-section">
						<label>Message</label>
						<c:set var = "messageError" value = "${requestScope.messageError}"/>
						<div class = "${empty messageError? 'normal-input' : 'error-input'}">
							<textarea id = "message" name = "message" rows="3" cols="50" placeholder = "Type your message here....">${messageValue}</textarea>
						</div>
						<p class = "field-error">
							<c:choose>
								<c:when test = "${not empty messageError}">
									${messageError}
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>
						</p>
					</div>
				</div>
				<div class = "actions">
					<button class = "submit" type = "submit">Submit</button>
				</div>
			</form>
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