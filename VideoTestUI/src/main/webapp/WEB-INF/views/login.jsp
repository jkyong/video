<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>PartDB Video Server Login</title>

<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-theme.min.css"	rel="stylesheet">

<!-- jquery -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #eee;
}

.form-signin {
	max-width: 330px;
	padding: 15px;
	margin: 0 auto;
}

.form-signin .form-signin-heading, .form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin .checkbox {
	font-weight: normal;
}

.form-signin .form-control {
	position: relative;
	height: auto;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 10px;
	font-size: 16px;
}

.form-signin .form-control:focus {
	z-index: 2;
}

.form-signin input[type="email"] {
	margin-bottom: -1px;
	border-bottom-right-radius: 0;
	border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
	margin-bottom: 10px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}
</style>

</head>


<body>
	<div class="container">

		<form class="form-signin" role="form" action="${pageContext.request.contextPath }/j_spring_security_check" method="POST">
			<h2 class="form-signin-heading">Log in</h2>			
			<input type="text" id="inputEmail" name='j_username' class="form-control" placeholder="Email address" required autofocus> 
			<input type="password" id="inputPassword" name='j_password' class="form-control" placeholder="Password" required>
						
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				Log in
			</button>
			
			<div class="btn btn-md btn-default btn-block">
				<a href="register">Register this site</a>
			</div>
		</form>
	</div>
	<!-- /container -->

</body>
</html>
