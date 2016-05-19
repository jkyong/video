<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>PartDB Guest Page</title>
<!-- jquery -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

<!-- video -->
<script src="${pageContext.request.contextPath}/resources/video/js/video.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/video/js/videojs-ie8.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/video/css/video-js.min.css" rel="stylesheet" type="text/css">
	
<script>
$(document).ready(function() {
	$('#video').on('contextmenu', function(e) {
		e.preventDefault();
	});
});
</script>
</head>
<body>
	<div id="content">
		<video id="video"
			class="video-js vjs-default-skin vjs-big-play-centered" controls
			preload="auto" width="640" height="360"
			data-setup='{"example_option":true}'> <source
			src="${pageContext.request.contextPath }/play/video/<c:out value="${uri}"/>/<c:out value="${external}"/>" type='video/mp4'></video>
	</div>
</body>
</html>