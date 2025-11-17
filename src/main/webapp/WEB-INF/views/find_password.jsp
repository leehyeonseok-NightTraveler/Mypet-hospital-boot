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
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script src="${pageContext.request.contextPath}/js/findPW.js"></script>

    <script>
        const findFail = "${findFail}";
        if (findFail === "true") {
            alert("계정 정보를 찾지 못했습니다.");
            history.replaceState(null, null, location.href);
        }
    </script>
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 페이지 본문 -->
    <main>
       <div class="find-container">
            <h2>비밀번호 찾기 🔑</h2>
            <p>아래 정보를 입력하시면 임시 비밀번호를<br>이메일로 발송해 드립니다.</p>
            
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
            
            <form action="findPwYn" method="post">
                <div class="input-group">
                    <input type="text" name="account_id" placeholder="아이디" required>
                </div>
                <div class="input-group">
                    <input type="text" name="account_phone" placeholder="전화번호" required>
                </div>
                <div class="input-group">
                    <input type="text" name="account_email" placeholder="이메일" required>
                </div>
                <div>
                    <button type="submit" class="submit-btnp">확인</button>
                </div>
                <div>
                    <input class="submit-btn2" type="button" onclick="location.href='login'" value="로그인 페이지 이동">  
                </div>
                <div class="link_wrap">
                        <a href="findAccount" class="link">아이디 찾기</a>
                        &nbsp;/&nbsp;
                        <a href="find_password" class="link">비밀번호 찾기</a>
                        &nbsp;/&nbsp;
                        <a href="register" class="link">회원가입</a>
                </div>
            </form>
        </div>
    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>	
</html>
