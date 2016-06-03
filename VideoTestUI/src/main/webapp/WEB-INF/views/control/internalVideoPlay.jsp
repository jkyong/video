<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- video -->
<script
	src="${pageContext.request.contextPath}/resources/video/js/video.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/video/js/videojs-ie8.min.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/video/css/video-js.min.css"
	rel="stylesheet" type="text/css">

<script>
	$(document).ready(function() {
		$('#internalVideo').on('contextmenu', function(e) {
			e.preventDefault();
		});
	});
</script>
<div id="content" >
	<video id="internalVideo"
		class="video-js vjs-default-skin vjs-big-play-centered" controls
		preload="auto"  height="480"
		data-setup='{"example_option":true}' style="width: 100%;">
		<source src="${pageContext.request.contextPath }/play/internal/view/${id}"
			type='video/mp4'>
	</video>
</div>