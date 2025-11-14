<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원 관리</title>
    <link rel="stylesheet" href="<c:url value='/css/user_manage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <script src="<c:url value='/js/jquery.js'/>"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main id="user-manage-main">
    <div class="floating-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <a href="<c:url value='/user_manage'/>" class="menu-link active" id="menu-user">회원정보 관리</a>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link" id="menu-veterinary">진료예약 관리</a>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link" id="menu-grooming">미용예약 관리</a>
        </nav>
    </div>

    <section id="user-list-section">
        <h2 class="section-title">회원정보관리</h2>
        <hr class="section-divider">

        <table id="list-table" class="list-table">
            <thead>
            <tr>
                <th class="col-no">번호</th>
                <th class="col-id">아이디</th>
                <th class="col-name">이름</th>
                <th class="col-phone">전화번호</th>
                <th class="col-status">활동상태</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="list" items="${UserList}">
                <tr class="list-row">
                    <td class="user-no">${list.user_no}</td>
                    <td class="user-id">${list.user_id}</td>
                    <td class="user-name">${list.user_name}</td>
                    <td class="user-phone">${list.user_phone}</td>
                    <td class="user-status">${list.user_status}</td>
                    <td class="user-view"><a href="<c:out value='user_detail?user_no=${list.user_no}'/>">상세정보</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>