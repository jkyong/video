<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Bootstrap 101 Template</title>

<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-theme.min.css"	rel="stylesheet">

<!-- jquery -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

<!-- fancytree -->
<script	src="${pageContext.request.contextPath}/resources/fancytree/js/jquery.js" type="text/javascript"></script>
<script	src="${pageContext.request.contextPath}/resources/fancytree/js/jquery-ui.custom.js"	type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/resources/fancytree/css/ui.fancytree.css" rel="stylesheet" type="text/css">
<script	src="${pageContext.request.contextPath}/resources/fancytree/js/jquery.fancytree.js"	type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/resources/fancytree/css/prettify.css" rel="stylesheet">
<script	src="${pageContext.request.contextPath}/resources/fancytree/js/prettify.js"	type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/resources/fancytree/css/sample.css" rel="stylesheet" type="text/css">

<script	src="${pageContext.request.contextPath}/resources/fancytree/js/jquery.fancytree.edit.js" type="text/javascript"></script>

<!-- upload -->
<link href="${pageContext.request.contextPath}/resources/upload/css/uploadfile.css"	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/resources/upload/css/uploadfile.custom.css" rel="stylesheet" type="text/css">
<script	src="${pageContext.request.contextPath}/resources/upload/js/jquery.uploadfile.js" type="text/javascript"></script>
<script	src="${pageContext.request.contextPath}/resources/upload/js/jquery.uploadfile.min.js" type="text/javascript"></script>

<!-- validate -->
<script	src="http://jqueryvalidation.org/files/dist/jquery.validate.min.js"></script>
<script	src="http://jqueryvalidation.org/files/dist/additional-methods.min.js"></script>


<style type="text/css">
	body { overflow-x: hidden; padding:15px 25px; }
</style>

</head>


<body>
	<div class="row">

		<div class="col-xs-12">
			<jsp:include page="include/header.jsp" />
		</div>
		<div class="col-xs-3">
			<jsp:include page="include/leftMenu.jsp" />
		</div>
		<div class="col-xs-9">
			<div class="row">
				<div class="col-xs-12">
					<jsp:include page="include/itemMenu.jsp" />
				</div>
				<div class="col-xs-12" style="height: 600px;">
					<jsp:include page="include/fileList.jsp" />
				</div>
			</div>

		</div>
	</div>



</body>
</html>
