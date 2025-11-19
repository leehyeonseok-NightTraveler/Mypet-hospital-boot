<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageName" value="login" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인 - MY PET 동물병원</title>

    <!-- 공통 + 개별 CSS -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 페이지 본문 -->
    <main>
        <div class="login-container">
            <h2>로그인 🐾</h2>
            <p>우리 MYPET 동물병원에 오신 것을 환영합니다!</p>

            <form action="/loginProcess" method="post">
                <div class="input-group">
                    <input type="text" name="user_id" placeholder="아이디" required>
                </div>
                <div class="input-group">
                    <input type="password" name="user_pwd" placeholder="비밀번호" required>
                </div>
                <button type="submit" class="submit-btn">로그인</button>

                <div class="extra-links">
                    <a href="/find_password">비밀번호 찾기</a> |
                    <a href="/register">회원가입</a>
                </div>

                <a href="/auth/kakao/login" class="kakao-login-button">
                    <img src="/img/kakao_login.png" >
                </a>
				<a href="<c:url value='/auth/google/login' />" class="google-login-button">
				        <img src="/img/google_login.png"> </a>
				<a href="/auth/naver/login">
				    <img src="/img/naver_login.png" height="50" alt="네이버로 로그인하기">
				</a>
            </form>
        </div>
    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- 메시지 alert -->
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

</body>
</html>
