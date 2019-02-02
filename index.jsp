<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
pageEncoding="ISO-8859-1" import="com.Teste"%>
<!DOCTYPE html>
<html lang="en">
    <head>
	<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
        <title>Login</title>
        <link href="favicon.ico" rel="icon" type="image/x-icon" />
        <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
	<link href="styles.css" rel="stylesheet">
    </head>

    <body>

	<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		out.println("Username: " + username);
		out.println("Password: " + password);
		Teste teste = new Teste();
		String texto = teste.printar();
		out.println(texto);
	%>

	<form action="index.jsp" method="POST">
		<div class="imgcontainer">
			<img src="Copel.jpg" alt="Avatar" class="avatar">
		</div>

		<div class="container">
			<label for="uname"><b>Username</b></label>
			<input type="text" placeholder="Enter Username" name="username" required>

			<label for="psw"><b>Password</b></label>
			<input type="password" placeholder="Enter Password" name="password" required>

			<button type="submit">Login</button>
		</div>
	</form>
    </body>

</html>
