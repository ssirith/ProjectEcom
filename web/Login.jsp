<%-- 
    Document   : Login
    Created on : Nov 1, 2018, 10:51:32 PM
    Author     : SSirith
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="Login" method="post">
            <table id="example" class="table">
                <tr>
                    <td>Username :</td>
                    <td><input type="email" name="email" required/>  </td>
                </tr>
                <tr>
                    <td>Password :</td>
                    <td><input type="password" name="password" required/>  </td>
                </tr>
                <tr>
                    <td colspan="2"><p style="color:red">${message}</p></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Login"/></td>
                </tr>

            </table>
        </form>
    </body>
</html>
