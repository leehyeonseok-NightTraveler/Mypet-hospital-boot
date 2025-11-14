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

            <div class="input-group">
                <label for="user_addr">주소</label>
                <input type="text" name="user_addr" placeholder="기본 주소">
                <input type="text" name="user_addr_detail" placeholder="상세 주소">
            </div>

            <button type="submit" class="submit-btn">가입하기</button>

        </form>
    </div>
</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
