<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div>
	<footer>
		<div id = "footer">
			<div class = "top-side">
				<img src = "${pageContext.request.contextPath}/resources/images/instagram.png">
				<img src = "${pageContext.request.contextPath}/resources/images/facebook.png">
				<img src = "${pageContext.request.contextPath}/resources/images/whatsapp.png">
				<img src = "${pageContext.request.contextPath}/resources/images/youtube.png">
			</div>
			<div class = "middle-side">
				<a href = "${pageContext.request.contextPath}/Home">Home</a>
				<c:if test = "${role == 'admin'}">
					<a href = "${pageContext.request.contextPath}/Admin">Dashboard</a>
				</c:if>
				<c:if test = "${role == 'customer'}">
					<a href = "${pageContext.request.contextPath}/Browse">Database</a>
					<a href = "${pageContext.request.contextPath}/About">About</a>
					<a href = "${pageContext.request.contextPath}/Contact">Contact</a>
				</c:if>
			</div>
			<div class = "bottom-side">
				<h4>&copy; 2025 MEDZONE. All rights reserved.</h4>
			</div>	
		</div>
	</footer>
</div>