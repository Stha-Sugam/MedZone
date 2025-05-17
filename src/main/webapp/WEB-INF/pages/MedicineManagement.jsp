<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Medicine</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/MedicineManagement.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/Header.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/images/admindashboardshortcut.png">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;0,500;1,500;1,600;1,700;1,800;1,900&display=swap"
	rel="stylesheet">
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
		<div class="action-contain">
			<div
				class="return-container ${not empty medToUpdate ? 'hidden' : ''}">
				<a href="${pageContext.request.contextPath}/Admin">Return to
					Dashboard</a>
			</div>
			<div class="search-bar ${not empty medToUpdate ? 'hidden' : ''}">
			    <div class="inner-section">
			        <c:set var="searchError" value="${searchErrors}" />
			        <div class="${empty searchError ? 'normal-input' : 'error-input'}">
			            <form action="ManageMed" method="post" class="search-form">
			                <input type="text" id="search" name="search" value = "${searchedVal}"
			                    placeholder="${not empty searchError ? searchError : 'Search for medicine by id, name or brand'}">
			                <button type="submit">
			                    <i class="fa fa-search"></i>
			                </button>
			            </form>
			        </div>
			    </div>
			</div>
			<div class="add-container ${not empty medToUpdate ? 'hidden' : ''}">
				        <a class="med-add"
					href="${pageContext.request.contextPath}/AddMed">Add New
					Medicine</a>
			</div>
		</div>
		<div class="manage-container">
			<div class="table-container ${not empty medToUpdate ? 'hidden' : ''}"
				id="table-page">
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
						<c:choose>
							<c:when test = "${not empty searchedDetails}">
								<c:forEach var="medicine" items="${searchedDetails}">
									<tr>
										<td>${medicine.id}</td>
										<td>${medicine.name}</td>
										<td>${medicine.brand}</td>
										<td>${medicine.form}</td>
										<td>${medicine.strength}</td>
										<td>${medicine.usage}</td>
										<td>${medicine.addedDate}</td>
										<td class="med-actions">
											<form class="medUpdate" action="UpdateMed" method="get">
												<input type="hidden" name="medsId" value="${medicine.id}">
												<button type="submit" class="med-update"
													onclick="showUpdateForm()">Edit</button>
											</form>
											<form class="delete-form" action="DeleteMed" method="post">
												<input type="hidden" name="medsId" value="${medicine.id}">
												<button class="med-delete" type="submit">Delete</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="medicine" items="${medicineDetails}">
									<tr>
										<td>${medicine.id}</td>
										<td>${medicine.name}</td>
										<td>${medicine.brand}</td>
										<td>${medicine.form}</td>
										<td>${medicine.strength}</td>
										<td>${medicine.usage}</td>
										<td>${medicine.addedDate}</td>
										<td class="med-actions">
											<form class="medUpdate" action="UpdateMed" method="get">
												<input type="hidden" name="medsId" value="${medicine.id}">
												<button type="submit" class="med-update"
													onclick="showUpdateForm()">Edit</button>
											</form>
											<form class="delete-form" action="DeleteMed" method="post">
												<input type="hidden" name="medsId" value="${medicine.id}">
												<button class="med-delete" type="submit">Delete</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>

			<div class="form-container ${not empty medToUpdate ? 'show' : ''}"
				id="form-page">
				<h2 class="head">Update Medicine Information</h2>
				<form class="form" id="update-med" action="UpdateMed" method="post"
					enctype="multipart/form-data">
					<div class="single-section">
						<div class="inner-section">
							<div class="id-head">
								<label>Medicine ID</label>
								<p class="id-error">(Id cannot be changed.)</p>
							</div>
							<div class="normal-input">
								<input type="text" id="id" name="id" value="${medIdtoUpdate}"
									readonly>
							</div>
							<p class="field-error">&nbsp;</p>
						</div>
					</div>
					<div class="single-section">
						<div class="inner-section">
							<label>Medicine's Name</label>
							<c:set var="nameError" value="${errorName}" />
							<div class="${empty nameError ? 'normal-input' : 'error-input'}">
								<input type="text" id="name" name="name"
									value="${medToUpdate.name}">
							</div>
							<p class="field-error">
								<c:choose>
									<c:when test="${not empty nameError}">
										${nameError}
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
							<label>Brand</label>
							<c:set var="brandError" value="${errorBrand}" />
							<div class="${empty brandError ? 'normal-input' : 'error-input'}">
								<input type="text" id="brand" name="brand"
									value="${medToUpdate.brand}">
							</div>
							<p class="field-error">
								<c:choose>
									<c:when test="${not empty brandError}">
										${brandError}
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
							<label>Dosage Form</label>
							<c:set var="formError" value="${errorForm}" />
							<div class="${empty formError? 'normal-input' : 'error-input'}">
								<input type="text" id="form" name="form"
									value="${medToUpdate.form}">
							</div>
							<p class="field-error">
								<c:choose>
									<c:when test="${not empty formError}">
										${formError}
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
							<label>Medicine Strength</label>
							<c:set var="strengthError" value="${errorStrength}" />
							<div
								class="${empty strengthError ? 'normal-input' : 'error-input'}">
								<input type="text" id="strength" name="strength"
									value="${medToUpdate.strength}">
							</div>
							<p class="field-error">
								<c:choose>
									<c:when test="${not empty strengthError}">
										${strengthError}
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
							<label>Usage</label>
							<c:set var="usageError" value="${errorUsage}" />
							<div class="${empty usageError? 'normal-input' : 'error-input'}">
								<textarea id="usage" name="usage" rows="3" cols="50">${medToUpdate.usage}</textarea>
							</div>
							<p class="field-error">
								<c:choose>
									<c:when test="${not empty usageError}">
										${usageError}
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
							<label>Medicine Image</label>
							<c:set var="imageError" value="${errorImage}" />
							<div class="medImage-container">
								<img
									src="${pageContext.request.contextPath}/resources/images/medicine/${medToUpdate.imageUrl}">
							</div>
							<div class="${empty imageError ? 'normal-input' : 'error-input'}">
								<input type="file" id="image" name="image">
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
						<a href="${pageContext.request.contextPath}/ManageMed"
							class="cancel">Cancel</a>
						<button class="submit" type="submit">Update Medicine</button>
					</div>
				</form>
			</div>
		</div>
		<div class="delete-popup-overlay" id="deletePopupOverlay">
			<div class="delete-popup">
				<h3>Confirm Delete</h3>
				<p>Are you sure you want to delete this medicine information?</p>
				<div class="delete-actions">
					<button type="button" class="confirm-delete"
						onclick="document.querySelector('#deletePopupOverlay').dataset.confirmed = 'true'; submitDeleteForm();">Yes,
						Delete</button>
					<button type="button" class="cancel-delete"
						onclick="hideDeletePopup()">Cancel</button>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page="Footer.jsp" />
	<script>
		let currentDeleteForm = null;

		function showUpdateForm() {
			const formContainer = document.getElementById('form-page');
			const tableContainer = document.getElementById('table-page');
			const returnContainer = document.querySelector('.return-container');
			if (formContainer && tableContainer && returnContainer) {
				formContainer.classList.add('show');
				tableContainer.classList.add('hidden');
				returnContainer.classList.add('hidden');
			}
		}

		function showDeletePopup(deleteButton) {
			const popupOverlay = document.getElementById('deletePopupOverlay');
			popupOverlay.style.display = 'flex';
			currentDeleteForm = deleteButton.closest('.delete-form');

			popupOverlay.dataset.triggeringButton = deleteButton;
			popupOverlay.dataset.confirmed = 'false';
		}

		function hideDeletePopup() {
			const popupOverlay = document.getElementById('deletePopupOverlay');
			popupOverlay.style.display = 'none';
			currentDeleteForm = null;
			delete popupOverlay.dataset.triggeringButton;
			popupOverlay.dataset.confirmed = 'false';
		}

		function submitDeleteForm() {
			const popupOverlay = document.getElementById('deletePopupOverlay');
			if (popupOverlay.dataset.confirmed === 'true' && currentDeleteForm) {
				currentDeleteForm.submit();
			}
			hideDeletePopup();
		}


		document.addEventListener("DOMContentLoaded", function () {
			// DELETE popup setup
			const deleteForms = document.querySelectorAll('.delete-form');
			deleteForms.forEach(function(form) {
				const deleteButton = form.querySelector('.med-delete[type="submit"]');
				if (deleteButton) {
					deleteButton.addEventListener('click', function(event) {
						event.preventDefault();
						showDeletePopup(this);
					});
				}
			});

			// Show messages
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

			// Show update form if needed
			const formContainer = document.getElementById('form-page');
			const tableContainer = document.getElementById('table-page');
			const returnContainer = document.querySelector('.return-container');
			if ("${not empty medToUpdate}" === "true" && formContainer && tableContainer && returnContainer) {
				formContainer.classList.add('show');
				tableContainer.classList.add('hidden');
				returnContainer.classList.add('hidden');
			}
		});

	</script>
</body>
</html>