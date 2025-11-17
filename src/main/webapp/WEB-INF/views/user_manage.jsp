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
<%-- 공통 헤더 (상단 메뉴, 로고 등) 포함 --%>

<main id="user-manage-main">
    <div class="floating-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <%-- 좌측 플로팅 네비게이션 메뉴 --%>
            <a href="<c:url value='/user_manage'/>" class="menu-link active" id="menu-user">회원정보 관리</a>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link" id="menu-veterinary">진료예약 관리</a>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link" id="menu-grooming">미용예약 관리</a>
        </nav>
    </div>

    <section id="user-list-section">
        <%-- 회원 목록을 표시하는 섹션 --%>
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
                <%-- 모델 객체 'UserList'의 각 항목을 반복하여 테이블 행 생성 --%>
                <tr class="list-row">
                    <td class="user-no">${list.user_no}</td>
                    <td class="user-id">${list.user_id}</td>
                    <td class="user-name">${list.user_name}</td>
                    <td class="user-phone">${list.user_phone}</td>
                    <td class="user-status">${list.user_status}</td>
                    <td class="user-view">
                            <%-- 회원 상세 정보 링크 --%>
                        <a href="<c:url value="/user_detail">
                        <c:param name="user_no" value="${list.user_no}"/>
                        <%-- 🌟 상세 보기 후 목록 복귀를 위해 현재 페이징/검색 조건 정보 전달 🌟 --%>
                        <c:param name="pageNum" value="${pageMaker.cri.pageNum}"/>
                        <c:param name="amount" value="${pageMaker.cri.amount}"/>
                        </c:url>">상세정보</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <nav class="pagination-container">
            <ul class="pagination-list">
                <%-- 이전 페이지 링크 --%>
                <c:if test="${pageMaker.prev}">
                    <li class="pagination-item prev paginate_button">
                        <a class="pagination-link"
                           href="user_manage?pageNum=${pageMaker.startPage - 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">이전</a>
                    </li>
                </c:if>

                <%-- 페이지 번호 링크 반복 출력 --%>
                <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                    <li class="pagination-item page-num paginate_button
                <c:out value='${pageMaker.cri.pageNum == num ? "active" : ""}'/>">
                        <a class="pagination-link"
                           href="user_manage?pageNum=<c:out value='${num}'/>&amount=<c:out value='${pageMaker.cri.amount}'/>">${num}</a>
                    </li>
                </c:forEach>

                <%-- 다음 페이지 링크 --%>
                <c:if test="${pageMaker.next}">
                    <li class="pagination-item next paginate_button">
                        <a class="pagination-link"
                           href="user_manage?pageNum=${pageMaker.endPage + 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">다음</a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
<%-- 공통 푸터 포함 --%>
</body>
</html>