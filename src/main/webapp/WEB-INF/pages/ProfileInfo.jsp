<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Profile Info</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/ProfileInfo.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/Header.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/images/user.png">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
	<jsp:include page="Header.jsp" />
	<main>
		<div class="success-messages" id="success-messages">
			<h1 class="success-msg">
				<c:if test="${not empty successMessage}">
				${successMessage}
			</c:if>
				<c:remove var="successMessage" scope="session" />
			</h1>
		</div>
		<div class="error-messages" id="error-messages">
			<h1 class="error-msg">
				<c:if test="${not empty errorMessage}">
				${errorMessage}
			</c:if>
				<c:remove var="errorMessage" scope="session" />
			</h1>
		</div>
		<div class="profile-container">
			<div class="left-container">
				<ul class="side-menu">
					<li><a class="menu-item" data-target="info-section">Your
							Information</a></li>
					<li><a class="menu-item" data-target="edit-profile">Edit
							your profile</a></li>
					<li><a class="menu-item" data-target="update-password">Change
							your password</a></li>
				</ul>
			</div>
			<div class="right-container">
				<div class="information content-div" id="info-section"
					style="display: none;">
					<div class="info-head">
						<h2>Your Account Information</h2>
						<a class="menu-item" data-target="edit-profile"
							style="cursor: pointer;">Edit profile</a>
					</div>
					<div class="user-info">
						<div class="info-section">
							<h3>Profile Picture</h3>
							<div class="profile-image">
								<img
									src="${pageContext.request.contextPath}/resources/images/user/${user.imageUrl}"
									alt="Profile Picture">
							</div>
						</div>
						<div class="info-section">
							<h3>User Information</h3>
							<div class="info-inner top">
								<h4>USERNAME</h4>
								<p>${user.userName}</p>
							</div>
							<div class="info-inner">
								<h4>FULL NAME</h4>
								<p>${user.firstName} ${user.lastName}</p>
							</div>
						</div>
						<div class="info-section">
							<h3>User Contact</h3>
							<div class="info-inner top">
								<h4>EMAIL ADDRESS</h4>
								<p>${user.email}</p>
							</div>
							<div class="info-inner">
								<h4>PHONE NUMBER</h4>
								<p>+977 ${user.phone}</p>
							</div>

						</div>
						<div class="info-section">
							<h3>Other Information</h3>
							<div class="info-inner">
								<h4>REGISTRATION DATE</h4>
								<p>${user.registrationDate}</p>
							</div>
						</div>
					</div>
				</div>
				<div class="updatePassword content-div" id="update-password"
					style="display: none;">
					<h2 class="head">Update your Password</h2>
					<form class="form" id="UpdatePassword" action="UpdatePassword"
						method="post">
						<div class="single-section">
							<div class="inner-section">
								<label>Username</label>
								<c:set var="userNameError" value="${requestScope.userName}" />
								<div
									class="${empty userNameError ? 'normal-input' : 'error-input'}">
									<i class="fa fa-user-circle"></i> <input type="text"
										id="username" name="username" value="${user.userName}"
										readonly>
								</div>
								<p class="field-error">&nbsp;</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Old Password</label>
								<c:set var="oldPassError" value="${requestScope.oldPassword}" />
								<div
									class="${empty oldPassError ? 'normal-input' : 'error-input'}">
									<i class="fa fa-lock"></i> <input type="password"
										id="oldPassword" name="oldPassword"
										value="${param['oldPassword']}"
										placeholder="Enter your old password">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty oldPassError}">
											${oldPassError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>New Password</label>
								<c:set var="newPassError" value="${requestScope.newPassword}" />
								<div
									class="${empty newPassError? 'normal-input' : 'error-input'}">
									<i class="fa fa-lock"></i> <input type="password"
										id="newPassword" name="newPassword"
										value="${param['newPassword']}"
										placeholder="Enter a new password">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty newPassError}">
											${newPassError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Confirm Password</label>
								<c:set var="cpassError" value="${requestScope.cpassword}" />
								<div class="${empty cpassError? 'normal-input' : 'error-input'}">
									<i class="fa fa-lock"></i> <input type="password"
										id="cpassword" name="cpassword" value="${param['cpassword']}"
										placeholder="Confirm your new password">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty cpassError}">
											${cpassError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="actions">
							<a href="${pageContext.request.contextPath}/ProfileInfo"
								class="cancel">Cancel</a>
							<button class="submit" type="submit">Save Changes</button>
						</div>
					</form>
				</div>
				<div class="updateProfile content-div" id="edit-profile"
					style="display: none;">
					<h2 class="head">Edit your profile</h2>
					<form class="form" id="update-info" action="UpdateProfile"
						method="post" enctype="multipart/form-data">
						<div class="single-section">
							<div class="inner-section">
								<div class="username-head">
									<label>Username </label>
									<p class="username-error">(Username cannot be changed.)</p>
								</div>
								<div class="normal-input">
									<i class="fa fa-user-circle"></i> <input type="text"
										id="username" name="username" value="${user.userName}"
										readonly>
								</div>
								<p class="field-error">&nbsp;</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>First Name</label>
								<c:set var="firstNameError" value="${firstNameErrors}" />
								<div
									class=${empty firstNameError ? 'normal-input' : 'error-input'}>
									<i class="fa fa-user"></i> <input type="text" id="firstName"
										name="firstName" value="${user.firstName}"
										placeholder="Enter your first name">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty firstNameError}">
											${firstNameError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Last Name</label>
								<c:set var="lastNameError" value="${lastNameErrors}" />
								<div
									class="${empty lastNameError? 'normal-input' : 'error-input'}">
									<i class="fa fa-user"></i> <input type="text" id="lastName"
										name="lastName" value="${user.lastName}"
										placeholder="Enter your last name">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty lastNameError}">
											${lastNameError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Phone Number</label>
								<c:set var="phoneNumError" value="${phoneNumErrors}" />
								<div
									class="${empty phoneNumError ? 'normal-input' : 'error-input'}">
									<i class="fa fa-phone"></i> <input type="text" id="phoneNum"
										name="phoneNum" value="${user.phone}"
										placeholder="Enter your phone number">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty phoneNumError}">
											${phoneNumError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Email Address</label>
								<c:set var="emailError" value="${emailErrors}" />
								<div class="${empty emailError? 'normal-input' : 'error-input'}">
									<i class="fa fa-envelope"></i> <input type="email" id="email"
										name="email" value="${user.email}"
										placeholder="Enter your email">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty emailError}">
											${emailError}
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="single-section">
							<div class="inner-section">
								<label>Upload New Profile Picture</label>
								<c:set var="imageError" value="${imageErrors}" />
								<div class="${empty imageError? 'normal-input' : 'error-input'}">
									<i class="fa fa-upload"></i> <input type="file" id="newImage"
										name="newImage">
								</div>
								<p class="field-error">
									<c:choose>
										<c:when test="${not empty imageError}">
										${imageError}
									</c:when>
										<c:otherwise>
										&nbsp;
									</c:otherwise>
									</c:choose>
								</p>
							</div>
						</div>
						<div class="actions">
							<a href="${pageContext.request.contextPath}/ProfileInfo"
								class="cancel">Cancel</a>
							<button class="submit" type="submit">Save Changes</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</main>
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

	    const navLinks = document.querySelectorAll("a[data-target]");
	    const contentDivs = document.querySelectorAll(".content-div");

	    function showSection(targetId) {
	        contentDivs.forEach(div => {
	            div.style.display = div.id === targetId ? "block" : "none";
	        });

	        navLinks.forEach(link => {
	            navLinks.forEach(nl => nl.classList.remove("active-link"));
	            const activeLink = Array.from(navLinks).find(link => link.getAttribute("data-target") === targetId);
	            if (activeLink) {
	                activeLink.classList.add("active-link");
	            }
	        });

	        localStorage.setItem("activeSection", targetId);
	    }

	    navLinks.forEach(link => {
	        link.addEventListener("click", (e) => {
	            e.preventDefault();
	            const targetId = link.getAttribute("data-target");
	            showSection(targetId);
	        });
	    });

	    const activeSectionFromServer = "${activeSection}";
	    let initialSection = "info-section";

	    if (activeSectionFromServer) {
	        initialSection = activeSectionFromServer;
	    } else if (localStorage.getItem("activeSection")) {
	        initialSection = localStorage.getItem("activeSection");
	    }

	    showSection(initialSection);

	    navLinks.forEach(link => {
	        if (link.getAttribute("data-target") === initialSection) {
	            link.classList.add("active-link");
	        }
	    });
	});
	</script>
	<jsp:include page="Footer.jsp" />
</body>
</html>