<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>추가 정보 입력 - MY PET 동물병원</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/register_social.css">
    
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://npmcdn.com/flatpickr/dist/l10n/ko.js"></script> 
    </head>
<body>

	<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="login-container">
        <h2>추가 정보 입력 🐾</h2>
        <p>카카오 로그인에 성공했습니다! 원활한 서비스 이용을 위해 추가 정보를 입력해주세요.</p>

        <form action="<c:url value='/register_social_process'/>" method="post" id="socialForm">
            
            <input type="hidden" name="user_name" value="${userDTO.user_name}">
            <input type="hidden" name="user_email" value="${userDTO.user_email}">

            <div class="input-group">
                <input type="tel" name="user_phone" placeholder="휴대폰 번호 ('-' 없이 입력)" required>
            </div>
            
            <div class="input-group">
                <label for="birthday">생년월일</label>
                <input type="date" id="birthday" name="user_birthday" placeholder="날짜를 선택하세요..">
            </div>

            <div class="input-group gender-group">
                <label>성별</label>
                <input type="radio" id="male" name="user_gender" value="M">
                <label for="male">남성</label>
                <input type="radio" id="female" name="user_gender" value="F">
                <label for="female">여성</label>
            </div>

            <div class="input-group address-group">
                <input type="text" id="postcode" name="user_addr" placeholder="우편번호" readonly>
                <button type="button" onclick="execDaumPostcode()" class="addr-btn">주소 검색</button>
            </div>
            <div class="input-group">
                <input type="text" id="address" name="user_addr_detail" placeholder="상세 주소">
            </div>

            <button type="submit" class="submit-btn">가입 완료</button>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    // Daum Postcode API 스크립트
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                document.getElementById('postcode').value = data.zonecode; // 우편번호
                document.getElementById("address").value = data.address; // 기본 주소
                document.getElementById("address").focus(); // 상세 주소로 포커스 이동 (ID 오타 수정됨)
            }
        }).open();
    }
    
    // 🔻🔻🔻 2. [추가] Flatpickr (달력) 실행 🔻🔻🔻
    flatpickr("#birthday", {
        "locale": "ko",                // 한국어 설정
        dateFormat: "Y-m-d",         // DB에 YYYY-MM-DD 형식으로 전송
        allowInput: true,            // 직접 입력 허용 (선택)
        maxDate: "today"             // 오늘 이후 날짜는 선택 불가 (생일이므로)
    });
</script>

</body>
</html>