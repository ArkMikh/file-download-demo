<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
</head>
<body>

<h3>
    <br>
    <ul>
        <c:forEach var="file" items="${files}">
            <br>
            <a href="/file/${file.name}">${file.name}</a>
        </c:forEach>
    </ul>
</h3>
</body>
</html>
