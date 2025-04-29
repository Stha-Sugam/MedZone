<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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
      transformation: scale(1.04);
    }
  }
</style>
<script>
	function clearFields() {
	    document.getElementById("login").reset();
	}
</script>
<body>
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
					<h1>Enter your <span>Credentials</span> to Continue</h1>
					<%
					    // Retrieve the error message from the request attribute
					    String error = (String) request.getAttribute("errorMessage");
					%>
					
					<% if (error != null && !error.trim().isEmpty()) { %>
					    <p class="errorMessage"><%= error %></p>
					<% } %>
					<label>Username</label>
					<div class = "input">
						<i class="fa fa-user"></i>
						<input placeholder = "Enter your username" type = "text"  id = "username" name = "username"/>
					</div>
					
					<label>Password</label>
					<div class = "input">
						<i class="fa fa-lock"></i>
						<input placeholder = "Enter your password" type = "password"  id = "password" name = "password">
					</div>
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
</body>
</html>