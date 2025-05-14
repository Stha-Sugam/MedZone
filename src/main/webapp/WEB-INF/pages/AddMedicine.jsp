<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Add Medicine</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/AddMedicine.css">
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
		<h2 class = "head">Add new medicine</h2>
		<form class = "form" id = "add-med" action = "AddMed" method = "post">
			<div class = "single-section">
				<div class = "inner-section">
					<label>Medicine Id</label>
					<c:set var = "idError" value = "${requestScope.errorId}" />
					<div class = ${empty idError ? 'normal-input' : 'error-input'}>
						<input type = "text" id = "id" name = "id" placeholder = "Enter the medicine's id">
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty idError}">
								${idError}
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
					<label>Medicine's Name</label>
					<c:set var = "nameError" value = "${requestScope.errorName}" />
					<div class = ${empty nameError ? 'normal-input' : 'error-input'}>
						<input type = "text" id = "name" name = "name" placeholder = "Enter the medicine's name">
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty nameError}">
								${nameError}
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
					<label>Brand</label>
					<c:set var = "brandError" value = "${requestScope.errorBrand}" />
					<div class = ${empty brandError ? 'normal-input' : 'error-input'}>
						<input type = "text" id = "brand" name = "brand" placeholder = "Enter the brand of the medicine">
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty brandError}">
								${brandError}
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
					<label>Dosage Form</label>
					<c:set var = "formError" value = "${requestScope.errorForm}"/>
					<div class = "${empty formError? 'normal-input' : 'error-input'}">
						<input type = "text" id = "form" name = "form" placeholder = "Enter the form of the medicine">
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty formError}">
								${formError}
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
					<label>Medicine Strength</label>
					<c:set var = "strengthError" value = "${requestScope.errorStrength}"/>
					<div class = "${empty strengthError ? 'normal-input' : 'error-input'}">
						<input type = "text" id = "strength" name = "strength" placeholder = "Enter the medicine strength(in mg)">
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty strengthError}">
								${strengthError}
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
					<label>Usage</label>
					<c:set var = "usageError" value = "${requestScope.errorUsage}"/>
					<div class = "${empty usageError? 'normal-input' : 'error-input'}">
						<textarea id = "usage" name = "usage" rows="3" cols="50" placeholder = "Enter the usage of the medicine"></textarea>
					</div>
					<p class = "field-error">
						<c:choose>
							<c:when test = "${not empty usageError}">
								${usageError}
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</p>
				</div>
			</div>
			<div class = "actions">
				<a href = "${pageContext.request.contextPath}/ManageMed" class = "cancel">Cancel</a>
				<button class = "submit" type = "submit">Add Medicine</button>
			</div>
		</form>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>