<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Register.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/loginshortcut.png">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Register</title>
</head>
</head>
<body>
	<div class = "database-error" id = "database-error">
		<h1 class = "base-error"><c:if test = "${not empty errorMessage}">
			Server Under Maintenance. Please try later!
		</c:if></h1>
	</div>
	<main>
		<div class = "Register">
			<div class = "logo-section">
				<img src = "${pageContext.request.contextPath}/resources/images/text logo.png">
			</div>
			<div class = "content-section">
				<div class = "register-text">
					<h1>SIGN UP FOR </h1><h1><span>MEDZONE</span></h1>
				</div>
				<form class = "form" id = "register" action = "Register" method = "post">
					<div class = "double-section">
						<div class = "inner-section">
							<label>First Name</label>
							<c:set var = "firstNameError" value = "${requestScope.firstName}" />
							<div class = ${empty firstNameError ? 'normal-input' : 'error-input'}>
								<i class="fa fa-user"></i>
								<input type = "text" id = "first-name" name = "first-name" value = "${param['first-name']}" placeholder = "Enter your first name">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty firstNameError}">
										${firstNameError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
						<div class = "inner-section">
							<label>Last Name</label>
							<c:set var = "lastNameError" value = "${requestScope.lastName}"/>
							<div class = "${empty lastNameError? 'normal-input' : 'error-input'}">
								<i class="fa fa-user"></i>
								<input type = "text" id = "last-name" name = "last-name" value = "${param['last-name']}" placeholder = "Enter your last name">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty lastNameError}">
										${firstNameError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
					<div class = "double-section">
						<div class = "inner-section">
							<label>Username</label>
							<c:set var = "userNameError" value = "${requestScope.userName}"/>
							<div class = "${empty userNameError ? 'normal-input' : 'error-input'}">
								<i class="fa fa-user-circle"></i>
								<input type = "text" id = "username" name = "username" value = "${param['username']}" placeholder = "Enter an username">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty userNameError}">
										${userNameError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
						<div class = "inner-section">
							<label>Phone Number</label>
							<c:set var = "phoneNumError" value = "${requestScope.phoneNum}"/>
							<div class = "${empty phoneNumError ? 'normal-input' : 'error-input'}">
								<i class="fa fa-phone"></i>
								<input type = "text" id = "phone-num" name = "phone-num" value = "${param['phone-num']}" placeholder = "Enter your phone number">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty phoneNumError}">
										${phoneNumError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
					<div class = "double-section">
						<div class = "inner-section">
							<label>Password</label>
							<c:set var = "passwordError" value = "${requestScope.password}"/>
							<div class = "${empty passwordError? 'normal-input' : 'error-input'}">
								<i class="fa fa-lock"></i>
								<input type = "password" id = "password" name = "password" value = "${param['password']}" placeholder = "Enter a password">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty passwordError}">
										${passwordError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
						<div class = "inner-section">
							<label>Confirm Password</label>
							<c:set var = "cpassError" value = "${requestScope.cpassword}"/>
							<div class = "${empty cpassError? 'normal-input' : 'error-input'}">
								<i class="fa fa-lock"></i>
								<input type = "password" id = "cpassword" name = "cpassword" value = "${param['cpassword']}" placeholder = "Confirm your password">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty cpassError}">
										${cpassError}
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
							<label>Email Address</label>
							<c:set var = "emailError" value = "${requestScope.email}"/>
							<div class = "${empty emailError? 'normal-input' : 'error-input'}">
								<i class="fa fa-envelope"></i>
								<input type = "email" id = "email" name = "email" value = "${param['email']}" placeholder = "Enter your email">
							</div>
							<p class = "field-error">
								<c:choose>
									<c:when test = "${not empty email}">
										${emailError}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
					<div class = "actions">
						<a href = "${pageContext.request.contextPath}/Login" class = "cancel">Return</a>
						<button class = "submit" type = "submit">Sign Up</button>
					</div>
				</form>
			</div>
		</div>
	</main>
	<script>
		document.addEventListener("DOMContentLoaded", () => {
	        const dbError = document.getElementById("database-error");
	
	        if (dbError && dbError.innerText.trim().length > 0) {
	          dbError.classList.add("active");
	
	          setTimeout(() => {
	            dbError.classList.remove("active");
	          }, 5000);
	        }
	      });
	</script>
</body>
</html>