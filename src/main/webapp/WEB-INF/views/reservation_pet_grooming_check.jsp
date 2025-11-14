<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>미용 예약 조회 - MY PET 동물병원</title>

    <!-- 정적 리소스 -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/reservation.css">
</head>

<body>

<!-- 공통 Header 삽입 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main-container">
    <div class="check-form-container">
        <h2 class="section-title">미용 예약 조회</h2>
        <p class="sub-title">예약 시 입력하신 보호자 성함과 연락처를 입력해주세요.</p>

        <form action="<c:url value='/reservation_pet_grooming_reference'/>"
              method="post"
              class="check-form">

            <div class="form-group">
                <label for="ownerName">보호자 성함</label>
                <input type="text" id="ownerName" name="userName" placeholder="보호자 성함을 입력해주세요." required>
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <div class="phone-inputs">
                    <input type="tel" id="phone1" name="phone1" value="010" maxlength="3" required>
                    <span>-</span>
                    <input type="tel" id="phone2" name="phone2" maxlength="4" required>
                    <span>-</span>
                    <input type="tel" id="phone3" name="phone3" maxlength="4" required>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="submit-btn">조회하기</button>
            </div>

        </form>
    </div>
</main>

<!-- 공통 Footer 삽입 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
