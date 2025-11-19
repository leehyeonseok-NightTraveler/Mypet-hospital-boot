<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>미용 예약 - MY PET 동물병원</title>

    <!-- 정적 리소스 -->
    <link rel="stylesheet" href="/css/reservation.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
</head>

<body>

<!-- 공통 Header -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main-container">
    <div class="reservation-form-container">

        <!-- 안내 문구 -->
        <section class="reservation-section">
            <h2 class="section-title">미용 예약</h2>
            <div class="notice-box">
                <h3 class="notice-title">미용 예약 유의사항</h3>
                <ul>
                    <li>당일 미용 예약은 전화 문의만 가능합니다.</li>
                    <li>털 엉킴이 심할 경우 추가 요금이 발생할 수 있습니다.</li>
                    <li>예약 시간보다 15분 이상 늦을 경우 예약이 자동 취소될 수 있습니다.</li>
                    <li>접종이 완료된 반려동물만 예약 가능합니다.</li>
                </ul>
                <a href="tel:055-123-4567" class="tel-button">055.123.4567</a>
            </div>
        </section>

        <!-- 정보 입력 -->
        <section class="reservation-section">
            <h2 class="section-title">예약 정보 입력</h2>

            <div class="info-box">
                <p>원하시는 스타일이 있다면 요청 사항에 자세히 작성해주세요.</p>
            </div>

            <form action="<c:url value='/reservation/grooming/create'/>"
                  method="post"
                  class="info-form">

                <div class="form-group">
                    <label for="userName">보호자 성함</label>
                    <input type="text" id="userName" name="user_name"
                           value="${loginUser.user_name}" readonly>
                </div>

                <div class="form-group">
                    <label for="pet_no">예약할 반려동물</label>
                    <select id="pet_no" name="pet_no" required>
                        <option value="">-- 선택 --</option>
                        <c:forEach var="pet" items="${petList}">
                            <option value="${pet.pet_no}">
                                ${pet.pet_name} (${pet.pet_breed})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="phone">연락처</label>
                    <div class="phone-inputs">
                        <input type="tel" id="phone1" name="phone1" value="010" maxlength="3">
                        <span>-</span>
                        <input type="tel" id="phone2" name="phone2" maxlength="4">
                        <span>-</span>
                        <input type="tel" id="phone3" name="phone3" maxlength="4">
                    </div>
                </div>

                <div class="form-group">
                    <label for="service_item">미용 스타일</label>
                    <select id="service_item" name="service_item">
                        <option value="">미용 스타일 선택</option>
                        <option value="전체 미용 (클리핑)">전체 미용 (클리핑)</option>
                        <option value="전체 미용 (가위컷)">전체 미용 (가위컷)</option>
                        <option value="목욕 및 부분 미용">목욕 및 부분 미용</option>
                        <option value="스파 / 마사지">스파 / 마사지</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="res_date">미용일시</label>
                    <input type="text" id="datepicker" name="res_date" placeholder="예약 날짜를 선택해주세요.">
                </div>

                <div class="form-group">
                    <label for="memo">특이사항 및 요청사항</label>
                    <textarea id="memo" name="memo" rows="5"></textarea>
                </div>

                <p class="consult-time">
                    상담시간 : 평일 09:00 ~ 17:00 / 토요일 09:00 ~ 12:00
                </p>

                <div class="form-actions">
                    <button type="submit" class="submit-btn">예약하기</button>
                </div>
            </form>
        </section>

    </div>
</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    flatpickr("#datepicker", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        minDate: "today",
        time_24hr: true,
        minTime: "09:00",
        maxTime: "17:00"
    });
</script>

</body>
</html>
