<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="connection.Login" %>
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
		String gateway = request.getParameter("gateway");
		String validation = request.getParameter("validation_token");
		String alert = request.getParameter("alerts");
		String packetsize = request.getParameter("packetsize");
	
		if (username != null && password != null) {
			Login login = new Login();
			String[] params = login.auth(username, password);
			
			Cookie accessCookie = new Cookie("access_token", params[0]);
			accessCookie.setMaxAge(Integer.parseInt(params[2]));
			Cookie refreshCookie = new Cookie("refresh_token", params[1]);
			refreshCookie.setMaxAge(60*60*24);

			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
			response.setIntHeader("Refresh", 0);
		} else if (gateway != null && validation != null) {
			Login login = new Login();
			String data = login.search(validation, gateway, packetsize);
			if(data != "Null") {
				out.println(data);
			} else {
				out.println("Nothing found");
			} 

		} else if (validation != null && alert != null) {
			Login login = new Login();
			String result = login.getAlerts(validation);
			out.println("Alerts:<br>");
			out.println(result);
		}
 
		Cookie[] cookies = request.getCookies();
		Boolean tokenExists = false;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().compareTo("access_token") == 0) {
					tokenExists = true;
				}
			}
		}
		if (tokenExists == false) {
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
	<% } else { %>
		<form action="index.jsp" method="POST">
			<div class="imgcontainer">
				<img src="Copel.jpg" alt="Avatar" class="avatar">
			</div>

			<select name="packetsize">
				<option value='info2'>2 Bytes</option>
				<option value='info4'>4 Bytes</option>
				<option value='info8'>8 Bytes</option>
				<option value='info16'>16 Bytes</option>
				<option value='info32'>32 Bytes</option>
				<option value='info64'>64 Bytes</option>
				<option value='info128'>128 Bytes</option>
			</select>

			<div class="container">
				<label for="uname"><b>Gateway</b></label>
				<input type="text" placeholder="Enter Gateway" name="gateway" value="gateway_a" required>
				<input type="hidden" name="validation_token" value="${cookie['access_token'].getValue()}" />
				<button type="submit">Search</button>
			</div>
		</form>
		<form action="index.jsp" method="POST">
			<div class="container">
				<input type="hidden" name="alerts" value="getAlert" />
				<input type="hidden" name="validation_token" value="${cookie['access_token'].getValue()}" />
				<button type"submit">Get alerts</button>
			</div>
		</form>
	<% } %>
    </body>

</html>
