<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 정보 수정 - MY PET 동물병원</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/mypage_userinfo_edit.css">
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>

<!-- 공통 헤더 + 플로팅 아이콘 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="edit-container">
        <h2>내 정보 수정 🐾</h2>
        <p>변경하실 정보를 입력해주세요.</p>

        <form action="/mypage_userinfo_edit_ok" method="post" enctype="multipart/form-data">

            <!-- 아이디 (읽기 전용) -->
            <div class="input-group">
                <input type="text" name="user_id" value="${loginUser.user_id}" readonly>
            </div>

			<!-- 비밀번호 변경 영역 (카카오가 아닌 경우에만 표시) -->
			<c:if test="${loginUser.social_type ne 'kakao'}">
			    <div class="input-group">
			        <input type="password" name="user_pwd" placeholder="새 비밀번호 (변경 시 입력)">
			    </div>
			    <div class="input-group">
			        <input type="password" name="user_pwd_confirm" placeholder="새 비밀번호 확인">
			    </div>
			</c:if>
			
			<!-- 보호자 이름 -->
			<div class="input-group">
			    <input type="text" name="user_name" value="${loginUser.user_name}" required placeholder="보호자 이름">
			</div>


            <!-- 연락처 -->
            <div class="input-group">
                <input type="text" name="user_phone" value="${loginUser.user_phone}" required>
            </div>

            <!-- 이메일 -->
            <div class="input-group">
                <input type="email" name="user_email" value="${loginUser.user_email}" required>
            </div>

            <!-- 주소 -->
			<div class="input-group address">
			    
			    <div class="address-group">
			        
			        <input type="text" id="postcode" placeholder="우편번호" readonly>
			        
			        <button type="button" class="btn-find-address" onclick="execDaumPostcode()">주소 찾기</button>
			    </div>

			    <input type="text" id="address" name="user_addr" placeholder="기본 주소">

			    <input type="text" id="detailAddress" name="user_addr_detail" placeholder="상세 주소">
			</div>
            <!-- 이미지 업로드 -->
            <div class="input-group">
                <label for="user_img" class="file-label">프로필 사진 변경</label>
                <input type="file" id="user_img" name="user_img">
            </div>

            <div class="button-group">	
                <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                <button type="submit" class="btn btn-primary">수정 완료</button>
            </div>
        </form>
    </div>
</main>

<!--  공통 footer + 공통 JS -->
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

 "locale": "ko", // 한국어 설정

 dateFormat: "Y-m-d", // DB에 YYYY-MM-DD 형식으로 전송

 allowInput: true, // 직접 입력 허용 (선택)

 maxDate: "today" // 오늘 이후 날짜는 선택 불가 (생일이므로)

 });

 </script>


</body>
</html>
