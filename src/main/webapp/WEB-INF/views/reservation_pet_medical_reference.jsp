<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>진료 예약 조회 - MY PET 동물병원</title>

<!-- 정적 리소스 -->
<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/reservation.css">

</head>
<body>

<!-- 공통 Header -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main-container">
    <div class="check-form-container2">

        <h2 class="section-title">진료 예약 조회 결과</h2>

        <!-- 조회 결과 있을 때 -->
        <c:if test="${not empty reservationList}">
            <div class="result-container">
                <h3 class="result-title">총 ${reservationList.size()}건의 예약 내역이 조회되었습니다.</h3>

                <table class="result-table">
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>보호자</th>
                            <th>반려동물</th>
                            <th>예약 일시</th>
                            <th>상태</th>
                            <th>관리</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="res" items="${reservationList}" varStatus="status">
                            <tr>
                                <td>${status.count}</td>
                                <td>${res.user_name}</td>
                                <td>${res.pet_name}</td>
                                <td>
                                    <fmt:formatDate 
                                        value="${res.res_date}" 
                                        pattern="yyyy 년 MM월 dd일 HH:mm" />
                                </td>
                                <td>
                                    <span class="status status-completed">${res.res_status}</span>
                                </td>
                                <td class="actions">
                                    <button class="btn btn-sm">변경</button>
                                    <button class="btn btn-sm btn-danger">취소</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- 조회 결과 없을 때 -->
        <c:if test="${not empty errorMessage}">
            <div class="error-container">
                <p class="error-message">${errorMessage}</p>
                <button class="btn" onclick="history.back()">다시 조회하기</button>
            </div>
        </c:if>

    </div>
</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
