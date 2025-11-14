<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<c:set var="pageName" value="findpassword" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기 - MY PET 동물병원</title>

    <!-- 개별 페이지 CSS -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/find_password.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 페이지 본문 -->
    <main>
       <div class="find-container">
            <h2>비밀번호 찾기 🔑</h2>
            <p>가입 시 등록한 정보를 입력해 주세요.</p>
            
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/findPasswordProcess" method="post">
                <div class="input-group">
                    <input type="text" name="user_name" placeholder="보호자 성함" required>
                </div>
                <div class="input-group">
                    <input type="text" name="user_id" placeholder="아이디" required>
                </div>
                <div class="input-group">
                    <input type="email" name="user_email" placeholder="이메일" required>
                </div>
                <button type="submit" class="submit-btn">비밀번호 재설정</button>
                
                <div class="extra-links">
                    <a href="/login">로그인</a> |
                    <a href="/register">회원가입</a>
                </div>
            </form>
        </div>
    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- 비밀번호 찾기 메시지 alert -->
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

</body>
</html>
