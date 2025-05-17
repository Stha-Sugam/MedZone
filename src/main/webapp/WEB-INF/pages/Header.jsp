<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<style>
	@media(max-width: 900px){
		.logo img{
			content: url("${pageContext.request.contextPath}/resources/images/text logo.png");
			width: 100%;
			height: 100%;
		}
	}
</style>
<div>
	<div class = "user-bar" id = "user-bar">
		<div class = "profile-bar">
			<div class = "profile-summary">
				<c:if test = "${role == 'customer'}">
					<i class = "fa fa-user"></i>
				</c:if>	
				<c:if test = "${role == 'admin'}">
					<i class = "fa fa-user-gear"></i>
				</c:if>
			    <div class = "name">
			        <h4>${username}</h4>
			    </div>
			</div>
		    <a href = "${pageContext.request.contextPath}/ProfileInfo" class = "to-account">
				<i class = "fa fa-user fa-user-edit"></i>
			</a>
		</div>
	</div>
	<header>
		<div id="header">
			<div class="hamburger" id="hamburger">
				<span></span>
				<span></span>
				<span></span>
			</div>
			<div class = "logo-container">
				<a class="logo" href="${pageContext.request.contextPath}/Home">
					<img src="${pageContext.request.contextPath}/resources/images/logo.png">
				</a>
			</div>
			
			<div class="menu-container" id="menu-container">
				<ul class="items-container">
					<li class = "items home"><a href="${pageContext.request.contextPath}/Home">Home</a></li>
					<c:choose>
						<c:when test = "${role == 'admin'}">
							<li class = "items dashboard"><a href="${pageContext.request.contextPath}/Admin">Dashboard</a></li>
						</c:when>
						<c:otherwise>
							<li class = "items database"><a href="${pageContext.request.contextPath}/Browse">Browse</a></li>
							<li class = "items about"><a href="${pageContext.request.contextPath}/About">About</a></li>
							<li class = "items contact"><a href="${pageContext.request.contextPath}/Contact">Contact</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
				<div class="profile" id="profile-icon">
					<img id="user-image" src="${pageContext.request.contextPath}/resources/images/user/${userImage}" alt="Profile Image">
					<i id="toggle-icon" class="fa fa-xmark" style="display: none;"></i>
				</div>
				<ul class="log-container">
					<li class = "log">
						<form id = "Logout" action = "Logout" method = "post">
							<button type = "submit">Log Out</button>
						</form>
					</li>
				</ul>
				</div>
			</div>
			
			<div class="sidebar" id="sidebar">
					<ul class = "side-container">
					<li class = "side-items home"><a href = "${pageContext.request.contextPath}/Home">Home</a></li>
					<c:choose>
						<c:when test = "${role == 'admin'}">
							<li class = "side-items dashboard"><a href="${pageContext.request.contextPath}/Admin">Dashboard</a></li>
						</c:when>
						<c:otherwise>
							<li class = "side-items database"><a href="${pageContext.request.contextPath}/Browse">Browse</a></li>
							<li class = "side-items about"><a href="${pageContext.request.contextPath}/About">About</a></li>
							<li class = "side-items contact"><a href="${pageContext.request.contextPath}/Contact">Contact</a></li>
						</c:otherwise>
					</c:choose>
					<li class = "side-items home">
						<a href = "${pageContext.request.contextPath}/ProfileInfo">
							Profile
						</a>
					</li>
					<li class = "side-items logout">
						<form action = "Logout" id = "side-Logout" method = "post">
							<button type = "submit">Log Out</button>
						</form>
					</li>
				</ul>
			</div>
	</header>
</div>
<script>
	document.addEventListener("DOMContentLoaded", function () {
		const hamburger = document.getElementById("hamburger");
		const sidebar = document.getElementById("sidebar");
		hamburger.addEventListener("click", () => {
			hamburger.classList.toggle("active");
			sidebar.classList.toggle("active");
		});
		
		const profileIconDiv = document.getElementById('profile-icon');
		const userBar = document.getElementById('user-bar');
		const userImage = document.getElementById('user-image');
		const toggleIcon = document.getElementById('toggle-icon');
		
		profileIconDiv.addEventListener('click', () => {
			userBar.classList.toggle('active');
			
			if (userBar.classList.contains('active')) {
				userImage.style.display = 'none';
				toggleIcon.style.display = 'inline';
			} else {
				userImage.style.display = 'inline';
				toggleIcon.style.display = 'none';
			}
		});
	});
</script>
