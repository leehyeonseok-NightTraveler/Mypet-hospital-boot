<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - 우리 MYPET 동물병원</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/register.css">
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>

<!-- 공통 Header + Floating Icons -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="register-container">
        <h2>보호자 정보 등록 🐾</h2>
        <p>우리 MYPET 동물병원의 가족이 되어주세요!</p>

        <form action="registerProcess" method="post" enctype="multipart/form-data">
            
            <div class="input-group">
                <label for="user_id">아이디</label>
                <input type="text" id="user_id" name="user_id" required>
            </div>

            <div class="input-group">
                <label for="user_pwd">비밀번호</label>
                <input type="password" id="user_pwd" name="user_pwd" required>
            </div>

            <div class="input-group">
                <label for="user_name">보호자 성함</label>
                <input type="text" id="user_name" name="user_name" required>
            </div>

            <div class="input-group">
                <label>성별</label>
                <div class="gender-options">
                    <label><input type="radio" name="user_gender" value="M"> 남성</label>
                    <label><input type="radio" name="user_gender" value="F"> 여성</label>
                    <label><input type="radio" name="user_gender" value="O"> 선택안함</label>
                </div>
            </div>

            <div class="input-group">
                <label for="user_birthday">생년월일</label>
                <input type="date" id="user_birthday" name="user_birthday">
            </div>

            <div class="input-group">
                <label for="user_phone">휴대폰 번호</label>
                <input type="tel" id="user_phone" name="user_phone" required>
            </div>

            <div class="input-group">
                <label for="user_email">이메일</label>
                <input type="email" id="user_email" name="user_email">
            </div>

			<div class="input-group address-group">
               <input type="text" id="postcode" name="user_addr" placeholder="우편번호" readonly>
               <button type="button" onclick="execDaumPostcode()" class="addr-btn">주소 검색</button>
           </div>

            <button type="submit" class="submit-btn">가입하기</button>

        </form>
    </div>
</main>

<!-- 공통 Footer -->
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
