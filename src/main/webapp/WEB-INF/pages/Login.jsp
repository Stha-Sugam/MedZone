<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Login.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/loginshortcut.png">
	<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<style>
  @media (max-width: 1000px) {
    .left-side img {
      content: url('${pageContext.request.contextPath}/resources/images/text logo.png');
      height: 13vh;
    }
  }
</style>
<body>
	<div class = "database-error" id = "database-error">
		<h1 class = "base-error">
		<c:if test = "${not empty errorMessage}">
			Server Under Maintenance. Try later!
		</c:if></h1>
	</div>
	<div class = "success-register" id = "success-register">
		<h1 class = "success-msg">
		<c:if test = "${not empty successRegister}">
			Account has been Created Successfully.
		</c:if>
		<c:remove var="successRegister" scope="session" />
		</h1>
	</div>
	<main>
		<div class = "left-side">
			<img src = "${pageContext.request.contextPath}/resources/images/text logo with tagline.png">
		</div>
		<div class = "right-side">
			<div class = "upper">
				<h1>SIGN IN TO <span>MEDZONE</span></h1>
			</div>
			<div class = "lower">
				<form class = "form" id = "login" action = "Login" method = "post">
					<c:set var = "errorMessage" value = "${requestScope.errorMessage}"/>
					<h1>Enter your <span>Credentials</span> to Continue</h1>
					
					<label>Username</label>
					<c:set var = "userNameError" value = "${requestScope.userName}" />
					<div id = "userNameInput" class = ${empty userNameError ? 'normal-input' : 'error-input'}>
						<i class="fa fa-user"></i>
						<input placeholder = "Enter your username" value = "${param['username']}" type = "text"  id = "username" name = "username"/>
					</div>
					<p class = "field-error" id = "userNameErrorMsgs">
						<c:choose>
							<c:when test = "${not empty userNameError}">
								${userNameError}
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</p>
					<label>Password</label>
					<c:set var = "passwordError" value = "${requestScope.password}"/>
					<div id = "passwordInput" class = "${empty passwordError? 'normal-input' : 'error-input'}">
						<i class="fa fa-lock"></i>
						<input type = "password" id = "password" name = "password" placeholder = "Enter a password">
					</div>
					<p class = "field-error" id = "passwordErrorMsgs">
						<c:choose>
							<c:when test = "${not empty passwordError}">
								${passwordError}
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</p>
					<div class = "forgot-text">
						<a class = "forgot" href = "#">
							Forgot password?
						</a>
					</div>
					<div class = "actions">
						<button class = "clear" type = "button" onclick = "clearFields()">CLEAR</button>
						<button class = "login" type = "submit">LOGIN</button>
					</div>
					<div class = "register-text">
						<a  class = "register" href = "${pageContext.request.contextPath}/Register">
							Create an account
						</a>
					</div>
				</form>
			</div>
		</div>
	</main>
	<script>
		function clearFields() {
			// resetting the values of the input fields
		    document.getElementById("username").value = "";
		    document.getElementById("password").value = "";
		    
		    // changing the classes of inputs to normal to remove red color from the border
		    document.getElementById("userNameInput").className = "normal-input";
		    document.getElementById("passwordInput").className = "normal-input";
		    
		    // changing the values of the errors to &nbsp; to remove the error messages
		    document.getElementById("userNameErrorMsgs").innerHTML = "&nbsp;";
		    document.getElementById("passwordErrorMsgs").innerHTML = "&nbsp;";
		}
		document.addEventListener("DOMContentLoaded", () => {
	        const dbError = document.getElementById("database-error");

	        if (dbError && dbError.innerText.trim().length > 0) {
	          dbError.classList.add("active");

	          setTimeout(() => {
	            dbError.classList.remove("active");
	          }, 5000);
	        }
	      });
		document.addEventListener("DOMContentLoaded", () => {
	        const dbError = document.getElementById("success-register");

	        if (dbError && dbError.innerText.trim().length > 0) {
	          dbError.classList.add("active");

	          setTimeout(() => {
	            dbError.classList.remove("active");
	          }, 10000);
	        }
	      });
	</script>
</body>
</html>