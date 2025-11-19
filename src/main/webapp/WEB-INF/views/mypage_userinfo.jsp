<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 - 내 정보</title>

    <!-- 정적 리소스 -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/userinfo_style.css">
</head>
<body>

<!-- 공통 헤더 include -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="mypage-container">

    <aside class="sidemenu">
        <h2>마이페이지 🐾</h2>
        <a href="/mypage_userinfo" class="active">내 정보</a>
        <a href="/mypage_petlist">펫 목록</a>
		<a href="/mypage_membership">멤버십</a>
		<a href="/mypage_medical">진료 내역</a>
        <a href="/mypage_grooming">미용 내역</a>
    </aside>

    <section class="content">
        <h2>내 정보</h2>

        <div class="profile-box">
			<c:choose>
			    <c:when test="${not empty loginUser.user_img}">
			        <!-- 업로드된 이미지 보여주기 -->
			        <img src="/upload/${loginUser.user_img}" class="profile-img">
			    </c:when>
			    <c:otherwise>
			        <!-- 업로드 이미지가 없으면 기본 이미지 -->
			        <img src="/img/default_profile.png" class="profile-img">
			    </c:otherwise>
			</c:choose>

            <div class="profile-name">
				<h2>
				    ${loginUser.user_name}
				    <span class="badge badge-${loginUser.current_grade}">
				        ${loginUser.current_grade}
				    </span>
				</h2>
                <p>${loginUser.user_email}</p>
            </div>
        </div>

        <div class="info-card">
            <ul>
                <li><strong>전화번호:</strong> ${loginUser.user_phone}</li>
                <li><strong>이메일:</strong> ${loginUser.user_email}</li>
                <li><strong>주소:</strong> ${loginUser.user_addr}</li>
                <li><strong>가입일:</strong> ${loginUser.user_regidate}</li>
                <li><strong>회원 상태:</strong> ${loginUser.user_status}</li>
				<li><strong>현재 멤버십 등급:</strong> ${loginUser.current_grade}</li>
				<li><strong>멤버십 만료일:</strong> ${loginUser.grade_expiry_date}</li>
            </ul>
        </div>

        <div class="status-box">
            <button class="edit-btn" onclick="location.href='/mypage_userinfo_edit'">
                정보 수정
            </button>
        </div>
    </section>

</main>

<!-- 공통 푸터 include -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
