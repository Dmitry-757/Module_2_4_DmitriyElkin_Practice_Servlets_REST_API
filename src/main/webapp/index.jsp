<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Home task. Servlets + REST API" %>
</h1>

<br/>
<a href="${pageContext.request.contextPath}/StartServlet">StartServlet</a>
<br>
<br>
<a href="${pageContext.request.contextPath}/RESTServlet">RESTServlet</a>

<form action="/fs/api/v1/files" method="post" enctype="multipart/form-data">
    Select File to Upload:<input type="file" name="fileName">
    <br>
    <input type="submit" value="Upload">
</form>


</body>
</html>