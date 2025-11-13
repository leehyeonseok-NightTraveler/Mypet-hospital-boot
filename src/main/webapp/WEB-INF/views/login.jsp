<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - MY PET 동물병원</title>

    <!-- 🔥 Boot 정적 리소스 경로 -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>

<div class="floating-icons">
    <a href="https://www.instagram.com/khieiorkr/" target="_blank" class="icon-instagram">
        <img src="https://img.icons8.com/fluent/48/000000/instagram-new.png" alt="인스타그램"/>
    </a>
    <a href="https://www.youtube.com/@KH_academy" target="_blank" class="icon-youtube">
        <img src="https://img.icons8.com/color/48/youtube-play.png" alt="유튜브"/>
    </a>
    <a href="<c:url value='/map' />" class="icon-map">
        <img src="https://img.icons8.com/color/48/000000/map-marker.png" alt="오시는길"/>
    </a>
</div>

<header>
    <div class="inner">
        <h1>
            <a href="<c:url value='/mainpage' />">
                <!-- 🔥 Boot 이미지 경로 -->
                <img src="/img/mypet.png">MY PET 동물병원
            </a>
        </h1>
        <ul id="gnb">
            <li><a href="<c:url value='/hospital_info' />">병원소개</a></li>
            <li class="dropdown-parent"><a href="#">게시판</a>
                <ul class="submenu">
                    <li><a href="<c:url value='/notices_list' />">공지사항</a></li>
                    <li><a href="#">자유게시판</a></li>
                    <li><a href="<c:url value='/qna_page' />">Q&A</a></li>
                </ul>
            </li>

            <c:choose>
                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
                    <li><a href="<c:url value='/reservation' />">예약</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="<c:url value='/login' />">예약</a></li>
                </c:otherwise>
            </c:choose>
        </ul>

        <ul class="util">
            <c:choose>
                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
                    <li><a href="<c:url value='/mypage_userinfo'/>">마이페이지</a></li>
                    <li><a href="<c:url value='/logout'/>">로그아웃</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="<c:url value='/login'/>">로그인</a></li>
                    <li><a href="<c:url value='/register'/>">회원가입</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</header>

<main>
    <div class="login-container">
        <h2>로그인 🐾</h2>
        <p>우리 MYPET 동물병원에 오신 것을 환영합니다!</p>

        <!-- 🔥 action 앞에 contextPath 제거 (Boot는 자동 적용됨) -->
        <form action="/loginProcess" method="post">
            <div class="input-group">
                <input type="text" name="user_id" placeholder="아이디" required>
            </div>
            <div class="input-group">
                <input type="password" name="user_pwd" placeholder="비밀번호" required>
            </div>
            <button type="submit" class="submit-btn">로그인</button>

            <div class="extra-links">
                <a href="<c:url value='/find_password'/>">비밀번호 찾기</a> |
                <a href="<c:url value='/register'/>">회원가입</a>
				<a href="<c:url value='/auth/kakao/login' />" class="kakao-login-button">
				    <img src="/img/kakao_login.png">
				</a>
            </div>
        </form>
    </div>
</main>

<footer>
    <div class="inner">
        <div class="footer-logo">
            <a href="#">
                MY PET 동물병원
            </a>
        </div>
        <div class="footer-info">
            <p>주소: 부산광역시 OO구 OO로 123번길 45 | 대표: 김원장 | 사업자등록번호: 123-45-67890</p>
            <p>TEL: 051-123-4567 | E-MAIL: contact@mypet.com</p>
            <p class="copyright">
                &copy; 2025 MY PET Animal Hospital. All Rights Reserved.
            </p>
        </div>
    </div>
</footer>

<c:if test="${not empty message}">
    <script>
        alert("${message}");
    </script>
</c:if>

</body>
</html>
