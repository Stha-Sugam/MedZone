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
					<li class = "items dashboard"><a href="${pageContext.request.contextPath}/Admin">Dashboard</a></li>
					<li class = "items database"><a href="${pageContext.request.contextPath}/Browse">Browse</a></li>
					<li class = "items about"><a href="${pageContext.request.contextPath}/About">About</a></li>
					<li class = "items contact"><a href="${pageContext.request.contextPath}/Contact">Contact</a></li>
				</ul>
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
					<li class = "side-items home"><a href="${pageContext.request.contextPath}/Home">Home</a></li>
					<li class = "side-items dashboard"><a href="${pageContext.request.contextPath}/Admin">Dashboard</a></li>
					<li class = "side-items database"><a href="${pageContext.request.contextPath}/Home">Browse</a></li>
					<li class = "side-items about"><a href="${pageContext.request.contextPath}/About">About</a></li>
					<li class = "side-items contact"><a href="${pageContext.request.contextPath}/Contact">Contact</a></li>
					<li class = "side-items logout">
						<form action = "Logout" id = "Logout" method = "post">
							<button type = "submit">Log Out</button>
						</form>
					</li>
				</ul>
			</div>
	</header>
</div>
<script>
  document.addEventListener("DOMContentLoaded", () => {
    const hamburger = document.getElementById("hamburger");
    const sidebar = document.getElementById("sidebar");

    hamburger.addEventListener("click", () => {
      hamburger.classList.toggle("active");
      sidebar.classList.toggle("active");
    });
  });
</script>