<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageName" value="mypage_petinfo_edit" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>펫 정보 수정 - MY PET 동물병원</title>

    <!-- 공통 CSS + 개별 CSS 로딩 -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/mypage_petinfo_edit.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 본문 (페이지 개별 내용) -->
    <main>
        <div class="edit-container">
            <h2>펫 정보 수정 🐾</h2>
            <p>변경하실 정보를 입력해주세요.</p>

            <form action="/mypage_petinfo_edit_ok" method="post" enctype="multipart/form-data">

                <input type="hidden" name="pet_no" value="${petInfo.pet_no}">

                <div class="input-group">
                    <label for="pet_name">펫 이름</label>
                    <input type="text" id="pet_name" name="pet_name"
                           value="${petInfo.pet_name}">
                </div>

                <div class="input-group">
                    <label for="pet_age">펫 나이</label>
                    <input type="number" id="pet_age" name="pet_age"
                           value="${petInfo.pet_age}" required>
                </div>

                <div class="input-group">
                    <label for="pet_birthday">생일</label>
                    <input type="date" id="pet_birthday" name="pet_birthday"
                           value="${petInfo.pet_birthday}" required>
                </div>

                <div class="input-group">
                    <label for="pet_gender">성별</label>
                    <select id="pet_gender" name="pet_gender" required>
                        <option value="M" ${petInfo.pet_gender eq 'M' ? 'selected' : ''}>수컷</option>
                        <option value="F" ${petInfo.pet_gender eq 'F' ? 'selected' : ''}>암컷</option>
                    </select>
                </div>

                <div class="input-group">
                    <label for="pet_species">종</label>
                    <input type="text" id="pet_species" name="pet_species"
                           value="${petInfo.pet_species}">
                </div>

                <div class="input-group">
                    <label for="pet_breed">품종</label>
                    <input type="text" id="pet_breed" name="pet_breed"
                           value="${petInfo.pet_breed}">
                </div>

                <div class="input-group">
                    <label for="pet_img" class="file-label">프로필 사진 변경</label>
                    <input type="file" id="pet_img" name="pet_img">
                </div>

                <div class="button-group">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                    <button type="submit" class="btn btn-primary">수정 완료</button>
                </div>

            </form>
        </div>
    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
