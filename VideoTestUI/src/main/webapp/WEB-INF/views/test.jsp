<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script src="//code.jquery.com/jquery-1.11.2.min.js"></script>


<%-- <script
	src="${pageContext.request.contextPath}/resources/testfolder/jquery.js"
	type="text/javascript"></script> --%>
<script
	src="${pageContext.request.contextPath}/resources/testfolder/jquery-ui.custom.js"
	type="text/javascript"></script>

<link
	href="${pageContext.request.contextPath}/resources/testfolder/ui.fancytree.css"
	rel="stylesheet" type="text/css">
<script
	src="${pageContext.request.contextPath}/resources/testfolder/jquery.fancytree.js"
	type="text/javascript"></script>

<link
	href="${pageContext.request.contextPath}/resources/testfolder/prettify.css"
	rel="stylesheet" type="text/css">
<script
	src="${pageContext.request.contextPath}/resources/testfolder/prettify.js"
	type="text/javascript"></script>

<link
	href="${pageContext.request.contextPath}/resources/testfolder/sample.css"
	rel="stylesheet" type="text/css">
<%-- <script
	src="${pageContext.request.contextPath}/resources/testfolder/sample.js"
	type="text/javascript"></script> --%>

<style type="text/css">
/*	span.fancytree-node span.fancytree-icon {
		background-image: url("skin-custom/bug.png");
		background-position: 0 0;
	}*/
span.fancytree-node.no span.fancytree-icon {
	background-position: 0 0;
	background-image: url("resources/skin-custom/skin-custom/exclamation.png");
}

span.fancytree-node.yes span.fancytree-icon {
	background-position: 0 0;
	background-image: url("resources/skin-custom/skin-custom/accept.png");
}

span.fancytree-node.link span.fancytree-icon {
	background-position: 0 0;
	background-image: url("resources/skin-custom/skin-custom/arrow_right.png");
}

span.fancytree-node.answer span.fancytree-icon {
	background-position: 0 0;
	background-image: url("resources/skin-custom/skin-custom/emoticon_smile.png");
}
</style>

	<script type="text/javascript">
		$(function(){
			// using default options
			$("#tree").fancytree({
				source: {url: "resources/ajax-tree-products.json"}
			});
		});
	</script>

	<!-- Start_Exclude: This block is not part of the sample code -->
	<script type="text/javascript">
		$(function(){
			addSampleButton({
				label: "Products",
				code: function(){ 
					$("#tree").fancytree("getTree").reload({url: "resources/ajax-tree-products.json"});
				}
			});
			addSampleButton({
				label: "Files",
				code: function(){
					$("#tree").fancytree("getTree").reload({url: "resources/ajax-tree-fs.json"});
				}
			});
		});
	</script>
	
	<script>
	$.ajax ({
		url: "db",
		type: "GET",
		dataType: "text",
		success: function(data) {
			alert(data);
			var obj = $.parseJSON(data);
			 
			/* alert(obj[0][""]); */
			$('#test').fancytree({
				source: {url: "db"}
			});
		}
		
	})
	
	</script>
</head>

<body>
	<h1>Example: Default</h1>
	<div class="description">
		This tree uses default options.<br> It is initialized from a
		hidden &lt;ul> element on this page.
	</div>
	<div>
		<label for="skinswitcher">Skin:</label> <select id="skinswitcher"></select>
	</div>
	<div id="tree"></div>

	<!-- Start_Exclude: This block is not part of the sample code -->
	<hr>
	<p id="sampleButtons"></p>
	<p class="sample-links  no_code">
		<a class="hideInsideFS" href="https://github.com/mar10/fancytree">jquery.fancytree.js
			project home</a> <a class="hideOutsideFS" href="#">Link to this page</a>
		<a class="hideInsideFS" href="index.html">Example Browser</a> <a
			href="#" id="codeExample">View source code</a>
	</p>
	<pre id="sourceCode" class="prettyprint" style="display: none"></pre>
	<!-- End_Exclude -->
	<div id="test"></div>
</body>
</html>