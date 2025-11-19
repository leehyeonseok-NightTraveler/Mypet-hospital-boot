<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>진료 예약 - MY PET 동물병원</title>

<link rel="stylesheet" href="/css/reservation.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

</head>
<body>

<!-- 공통 Header -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main-container">
    <div class="reservation-form-container">

        <!-- 안내문 -->
        <section class="reservation-section">
            <h2 class="section-title">진료 예약</h2>

            <div class="notice-box">
                <h3 class="notice-title">진료 예약 유의사항</h3>
                <ul>
                    <li>당일은 진료예약이 되지 않습니다.</li>
                    <li>예약 신청 후 24시간(평일 기준) 이후 확정 여부를 확인해주세요.</li>
                    <li>진료를 받지 못하실 경우 미리 예약 변경/취소를 부탁드립니다.</li>
                    <li>진료 당일 변경·취소는 반드시 고객센터로 연락 바랍니다.</li>
                </ul>
                <a href="tel:055-123-4567" class="tel-button">055.123.4567</a>
            </div>
        </section>

        <!-- 예약 정보 -->
        <section class="reservation-section">
            <h2 class="section-title">예약 정보 입력</h2>

            <div class="info-box">
                <p>추가 요구사항은 요청사항에 적어주세요.</p>
                <p>입력하신 정보를 기준으로 상담 시간이 안내됩니다.</p>
            </div>

            <form action="<c:url value='/reservation/medical/create'/>"
                  method="post" class="info-form">

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
                    <label for="service_item">진료 항목</label>
                    <select id="service_item" name="service_item">
                        <option value="">진료 항목 선택</option>
                        <option value="기본 검진">기본 검진</option>
                        <option value="예방 접종">예방 접종</option>
                        <option value="내과 진료">내과 진료</option>
                        <option value="외과/정형외과">외과/정형외과</option>
                        <option value="피부과 진료">피부과 진료</option>
                        <option value="치과 진료">치과 진료</option>
                        <option value="중성화 수술">중성화 수술</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="res_date">진료일시</label>
                    <input type="text" id="datepicker" name="res_date"
                           placeholder="예약 날짜를 선택해주세요.">
                </div>

                <div class="form-group">
                    <label for="memo">주요 증상 및 요청사항</label>
                    <textarea id="memo" name="memo" rows="5"
                              placeholder="아이의 증상 및 요청사항을 작성해주세요."></textarea>
                </div>

                <p class="consult-time">
                    상담시간 : 평일 09:00 ~ 17:00 / 토요일 09:00 ~ 12:00 (일·공휴일 제외)
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
