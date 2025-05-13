<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Medicine</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/MedicineManagement.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/admindashboardshortcut.png">
	<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
<style>
    .hidden {
        display: none !important;
    }
</style>
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class = "return-container">
			<a href = "${pageContext.request.contextPath}/Admin">Return</a>
		</div>
		<div class = "manage-container">
			<div class="table-container" id = "table-page" class="${not empty medToUpdate ? 'hidden' : ''}">
			    <table>
			        <thead>
			            <tr>
			            	<th>Id</th>
			                <th>Name</th>
			                <th>Brand</th>
			                <th>Dosage Form</th>
			                <th>Strength</th>
			                <th>Usage</th>
			                <th>Added Date</th>
			                <th>Actions</th>
			            </tr>
			        </thead>
			        <tbody>
			            <c:forEach var = "medicine" items = "${medicineDetails}">
			                <tr>
			                	<td>${medicine.id}</td>
			                    <td>${medicine.name}</td>
			                    <td>${medicine.brand}</td>
			                    <td>${medicine.form}</td>
			                    <td>${medicine.strength}</td>
			                    <td>${medicine.usage}</td>
			                    <td>${medicine.addedDate}</td>
			                    <td class = "med-actions">
			                        <form action = "UpdateMed" method = "get">
									    <input type = "hidden" name = "medsId" value = "${medicine.id}">
									    <button type="submit" class="med-update">Update</button>
									</form>
			                        <form action = "DeleteMed" method = "post">
			                            <input type = "hidden" name = "medsId" value = "${medicine.id}">
			                            <button class = "med-delete" type = "submit">Delete</button>
			                        </form>
			                    </td>
			                </tr>
			            </c:forEach>
			        </tbody>
			    </table>
			</div>

			<div class = "form-container" id = "form-page" class="${empty medToUpdate ? 'hidden' : ''}">
				<h2 class = "head">Update Medicine Information</h2>
				<form class = "form" id = "update-med" action = "UpdateMed" method = "post">
					<div class = "single-section">
						<div class = "inner-section">
							<div class = "id-head">
								<label>Medicine ID</label>
								<p class = "id-error">(Id cannot be changed.)</p>
							</div>
							<div class = "normal-input">
								<input type = "text" id = "id" name = "id" value = "${medToUpdate.id}" readonly>
							</div>
							<p class = "field-error"> &nbsp; </p>
						</div>
					</div>
					<div class = "single-section">
						<div class = "inner-section">
							<label>Medicine's Name</label>
							<c:set var = "nameError" value = "${requestScope.errorName}" />
							<div class = "${empty nameError ? 'normal-input' : 'error-input'}">
								<input type = "text" id = "name" name = "name" value = "${medToUpdate.name}">
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
							<div class = "${empty brandError ? 'normal-input' : 'error-input'}">
								<input type = "text" id = "brand" name = "brand" value = "${medToUpdate.brand}">
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
								<input type = "text" id = "form" name = "form" value = "${medToUpdate.form}">
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
								<input type = "text" id = "strength" name = "strength" value = "${medToUpdate.strength}">
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
								<textarea id = "usage" name = "usage" rows="8" cols="50">${medToUpdate.usage}</textarea>
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
						<button class = "submit" type = "submit">Update Medicine</button>
					</div>
				</form>
			</div>
		</div>

		<div class = "add-container">
        	<a class = "med-add" href = "${pageContext.request.contextPath}/AddMed">Add Medicine</a>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
</body>
</html>