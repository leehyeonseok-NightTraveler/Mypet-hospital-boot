<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>나의 펫 등록 - MY PET 동물병원</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/mypage_userinfo_edit.css">

</head>
<body>

<!-- HEADER -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<!-- MAIN CONTENT -->
<main>
    <div class="edit-container">
        <h2>나의 펫 등록 🐾</h2>
        <p>정보를 입력해주세요.</p>

        <form action="/petjoinProcess" method="post" enctype="multipart/form-data">

            <div class="input-group">
                <input type="text" name="pet_name" placeholder="펫 이름" required>
            </div>

            <div class="input-group">
                <input type="text" name="pet_species" placeholder="종(강아지, 고양이 등)" required>
            </div>

            <div class="input-group">
                <input type="text" name="pet_breed" placeholder="품종(예: 말티즈, 리트리버)" required>
            </div>

            <div class="input-group">
                <label>성별</label>
                <div class="gender-options">
                    <label><input type="radio" name="pet_gender" value="M"> 남성</label>
                    <label><input type="radio" name="pet_gender" value="F"> 여성</label>
                </div>
            </div>

            <div class="input-group">
                <label for="pet_birthday">생년월일</label>
                <input type="date" id="pet_birthday" name="pet_birthday">
            </div>

            <div class="input-group">
                <label>중성화 여부</label>
                <div class="pet_neutered-options">
                    <label><input type="radio" name="pet_neutered" value="Y"> 예</label>
                    <label><input type="radio" name="pet_neutered" value="N"> 아니오</label>
                </div>
            </div>

            <div class="input-group">
                <label for="pet_img" class="file-label">프로필 사진 추가</label>
                <input type="file" id="pet_img" name="pet_img">
            </div>

            <div class="button-group">
                <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                <button type="submit" class="btn btn-primary">등록하기</button>
            </div>

        </form>
    </div>
</main>

<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
