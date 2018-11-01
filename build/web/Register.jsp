<%-- 
    Document   : Register
    Created on : Oct 25, 2018, 10:19:28 PM
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
        <h1>Hello World!</h1>
         <form action="Register" method="post">
                  <table class="table">
                      <tr>
                          <td>email: </td>
                          <td><input type="email"name="email"required/></td>
                      </tr>
                      <tr>
                          <td>password: </td>
                          <td><input type="password"name="password"required/></td>
                      </tr>
                       <tr>
                          <td>firstname: </td>
                          <td><input type="text"name="fname"required/></td>
                      </tr>
                       <tr>
                          <td>lastname: </td>
                          <td><input type="text"name="lname"required/></td>
                      </tr>
                       <tr>
                          <td>address: </td>
                          <td><input type="text"name="address"required/></td>
                      </tr>
                       <tr>
                          <td>tel: </td>
                          <td><input type="tel"name="tel"required/></td>
                      </tr>
                      <tr>
                          <td></td>
                          <td><input type="submit"value="Register"/></td>
                      </tr>
                      <tr>
                          <td colspan="2"><p style=""color:red>${message}</p></td>
                      </tr>
                  </table>
            </form>
    </body>
</html>
