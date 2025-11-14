<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원 관리</title>
    <link rel="stylesheet" href="<c:url value='/css/manage.css'/>"/>
    <script src="<c:url value='/js/jquery.js'/>"></script>
</head>
<body>
<main>
    <div class="floating-wrapper">
        <div class="floating-menu">
            <a href="<c:url value='/user_manage'/>" >회원정보 관리</a>
            <a href="<c:url value='/veterinary_manage'/>">진료예약 관리</a>
            <a href="<c:url value='/grooming_manage'/>" class="active">미용예약 관리</a>
        </div>
    </div>

    <div>
        <h2>미용예약관리</h2>
        <hr>
    </div>

    <div>
        <table>
            <tr>
                <th>번호</th>
                <th></th>
                <th>번호</th>
                <th>번호</th>
            </tr>
        </table>
    </div>
</main>
</body>
</html>
