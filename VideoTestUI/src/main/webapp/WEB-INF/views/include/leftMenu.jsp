<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<script
	src="${pageContext.request.contextPath}/resources/fancytree/js/treeJs/treeObject.js"
	type="text/javascript"></script>

<script>
	$(document).ready(function() {

		treeObject.Init();

	});
</script>


<!-- 	<div class="container"> -->
<!-- <h2>Collapsible Panel</h2> -->
<!-- panel group -->
<div class="panel-group">


	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a data-toggle="collapse" href="#collapse1">My Projects</a>
			</h4>
		</div>
		<div id="collapse1" class="panel-collapse collapse">
			<div class="panel-body"
				style="background-color: black; height: 100px; background-image: url('./resources/icons.gif');"></div>
			<div class="panel-footer" style="background-color: blue;">Panel
				Footer</div>
		</div>
	</div>


	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<a data-toggle="collapse" href="#collapse2">Explorer</a>
			</h4>
		</div>
		<div id="collapse2" class="panel-collapse collapse">
			<!-- <div class="panel-body" style="background-color: black;">aaaaaaaa</div> -->
			<div class="panel-body" style="height: 100%;">
				<div id="treeItem" style="height: 100%;"></div>
			</div>
		</div>

	</div>
	<!-- panel group end -->

	<!-- </div> -->
	<!-- div class container end -->
</div>
<!-- left grid end -->
<!-- col-xs-3 div end -->
<div id='echoSelection1'></div>
