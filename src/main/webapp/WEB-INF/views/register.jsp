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
			<!-- 주소 -->
            <div class="input-group address">
                <div class="address-group">
                    <input type="text" id="postcode" placeholder="우편번호">
                    <button type="button" class="btn-find-address" onclick="execDaumPostcode()">주소 찾기</button>
                </div>

                <input type="text" id="address" name="user_addr" placeholder="기본 주소"
                       value="${loginUser.user_addr}">

                <input type="text" id="detailAddress" name="user_addr_detail"
                       placeholder="상세 주소">
            </div>

            <button type="submit" class="submit-btn">가입하기</button>

        </form>
    </div>
</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script>
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 1. 도로명/지번 주소 선택 로직
                var addr = ''; // 주소 변수

                if (data.userSelectedType === 'R') { // 도로명 주소 선택
                    addr = data.roadAddress;
                } else { // 지번 주소 선택
                    addr = data.jibunAddress;
                }

                // 2. ID를 이용해 값을 넣어줍니다. (보내주신 HTML ID와 일치함)
                document.getElementById('postcode').value = data.zonecode; // 우편번호
                document.getElementById("address").value = addr;           // 기본 주소

                // 3. 상세 주소 입력칸으로 커서 이동
                document.getElementById("detailAddress").focus();
            }
        }).open();
    }
</script>

</body>
</html>
