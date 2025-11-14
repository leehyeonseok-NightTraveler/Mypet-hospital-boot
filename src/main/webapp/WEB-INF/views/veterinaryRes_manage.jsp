<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>진료예약 관리</title>
    <link rel="stylesheet" href="<c:url value='/css/veterinaryRes_manage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <script src="<c:url value='/js/jquery.js'/>"></script>
    <script>
        function openCancelModal(resNo) {
            document.getElementById('cancelResNo').value = resNo;
            document.getElementById('cancelModal').style.display = 'block';
            document.getElementById('modalOverlay').style.display = 'block';
        }

        function closeCancelModal() {
            document.getElementById('cancelModal').style.display = 'none';
            document.getElementById('modalOverlay').style.display = 'none';
        }
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main id="res-manage-main">
    <div class="floating-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <a href="<c:url value='/user_manage'/>" class="menu-link" id="menu-user">회원정보 관리</a>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link active" id="menu-veterinary">진료예약 관리</a>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link" id="menu-grooming">미용예약 관리</a>
        </nav>
    </div>

    <section id="res-list-section">
        <h2 class="section-title">진료예약관리</h2>
        <hr class="section-divider">

        <table id="reservation-list-table">
            <thead>
            <tr>
                <th>예약번호</th>
                <th>회원번호</th>
                <th>회원이름</th>
                <th>동물번호</th>
                <th>동물이름</th>
                <th>전화번호</th>
                <th>예약날짜</th>
                <th>예약상태</th>
                <th>처리</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ResList" items="${VeterinaryResList}">
                <tr>
                    <td>${ResList.res_no}</td>
                    <td>${ResList.user_no}</td>
                    <td>${ResList.user_name}</td>
                    <td>${ResList.pet_no}</td>
                    <td>${ResList.pet_name}(${ResList.pet_breed})</td>
                    <td>${ResList.user_phone}</td>
                    <td><fmt:formatDate value="${ResList.res_date}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>${ResList.res_status}</td>
                    <td>
                        <c:choose>
                            <c:when test="${ResList.res_status eq '예약완료'}">
                                <form method="post" action="veterinaryResProcess" style="display:inline;">
                                    <input type="hidden" name="res_no" value="${ResList.res_no}" />
                                    <button type="submit">예약확정</button>
                                </form>
                                <button type="button" onclick="openCancelModal('${ResList.res_no}')">예약취소</button>
                            </c:when>
                            <c:otherwise>
                                <span>-</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>

    <!-- 모달 배경 -->
    <div id="modalOverlay"></div>

    <!-- 예약취소 모달창 -->
    <div id="cancelModal">
        <form method="post" action="cancelReservation">
            <input type="hidden" name="res_no" id="cancelResNo" />
            <label for="cancel_reason">취소 사유:</label><br>
            <textarea name="cancel_reason" id="cancel_reason" rows="4" cols="40" required></textarea><br><br>
            <button type="submit" class="btn-cancel-submit">확인</button>
            <button type="button" class="btn-cancel-close" onclick="closeCancelModal()">닫기</button>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>