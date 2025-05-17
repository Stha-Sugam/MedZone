<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name = "author" content = "Sugam Shrestha">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Browse</title>
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Browse.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Header.css">
<link rel = "stylesheet" type = "text/css"
	href = "${pageContext.request.contextPath}/CSS/Footer.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/database shortcut.png">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page = "Header.jsp"/>
	<main>
		<div class="error-messages" id="error-messages">
		    <h1 class="error-msg">
		        <c:if test="${not empty errorMessage}">
		            ${errorMessage}
		        </c:if>
		        <c:remove var="errorMessage" scope="request" />
		    </h1>
		</div>
		<div class = "browse-container">
			<div class="search-bar">
			    <div class="inner-section">
			        <c:set var="searchError" value="${searchErrors}" />
			        <div class="${empty searchError ? 'normal-input' : 'error-input'}">
			            <form action="Browse" method="post" class="search-form">
			                <input type="text" id="search" name="search" value = "${searchedVal}"
			                    placeholder="${not empty searchError ? searchError : 'Search for medicine by id, name or brand'}">
			                <button type="submit">
			                    <i class="fa fa-search"></i>
			                </button>
			            </form>
			        </div>
			    </div>
			</div>
			<div class = "products">
				<c:choose>
					<c:when test = "${not empty searchedDetails}">
						<c:forEach var = "med" items = "${searchedDetails}">
							<div class = "product">
								<div class = "product-image">
								<img src = "${pageContext.request.contextPath}/resources/images/medicine/${med.imageUrl}">
								</div>
								<h2>ID: ${med.id}</h2>
								<h3>Name: ${med.name}</h3>
								<form class = "browse" action = "Browse" method = "post">
									<input type = "hidden" name = "medId" value = "${med.id}">
									<button class = "submit-btn" onclick = "hideProducts()" type = "submit">SEE MORE</button>
								</form>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:forEach var = "med" items = "${medicineDetails}">
							<div class = "product">
								<div class = "product-image">
								<img src = "${pageContext.request.contextPath}/resources/images/medicine/${med.imageUrl}">
								</div>
								<h2>ID: ${med.id}</h2>
								<h3>Name: ${med.name}</h3>
								<form class = "browse" action = "Browse" method = "post">
									<input type = "hidden" name = "medId" value = "${med.id}">
									<button class = "submit-btn" onclick = "hideProducts()" type = "submit">SEE MORE</button>
								</form>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
			<div class = "product-card">
				<div class = "card-image">
					<img src = "${pageContext.request.contextPath}/resources/images/medicine/${med.imageUrl}">
				</div>
				<div class = "product-content">
					<h1><span>ID:</span> ${med.id}</h1>
					<div class = "details">
						<h2><span>Name:</span> ${med.name}</h2>
						<h2><span>Brand:</span> ${med.brand}</h2>
						<h2><span>Dosage Form:</span> ${med.form}</h2>
						<h2><span>Strength:</span> ${med.strength}</h2>
						<h2><span>Usage:</span> ${med.usage}</h2>
					</div>
				</div>
				<div class = "return">
					<a href = "${pageContext.request.contextPath}/Browse"><i class = "fa fa-xmark"></i></a>
				</div>
			</div>
		</div>
	</main>
	<jsp:include page = "Footer.jsp"/>
	<script>
	    window.addEventListener("DOMContentLoaded", function () {
	        const medExists = ${med != null ? "true" : "false"};
	        const error = document.getElementById("error-messages");
	        
	        if (error && error.innerText.trim().length > 0) {
	            error.classList.add("active");
	            setTimeout(() => error.classList.remove("active"), 5000);
	        }
	
	        if (medExists) {
	            const productsDiv = document.querySelector('.products');
	            const productCardDiv = document.querySelector('.product-card');
	            const searchBar = document.querySelector('.search-bar');
	
	            if (productsDiv) productsDiv.style.display = 'none';
	            if (searchBar) searchBar.style.display = 'none';
	            if (productCardDiv) productCardDiv.style.display = 'flex';	            
	        }
	    });
	</script>
</body>
</html>