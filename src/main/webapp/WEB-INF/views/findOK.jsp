<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mainpage.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/findOK.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/views/common/header.jsp" />

        <main>
        <div class="find-container">
            <div>
                <p>입력하신 이메일 주소로 계정 정보를 전송했습니다.</p>
            </div>
            <div>
                <button class="submit-btn" type="button" onclick="location.href='login'">로그인 페이지로 돌아가기</button>
            </div>

            <div class="link_wrap">
                <a href="findAccount" class="link">아이디 찾기</a>
                &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;
                <a href="find_password" class="link">비밀번호 찾기</a>
                &nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;
                <a href="register" class="link">회원가입</a>
            </div>
        </div>
        </main>

        <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    </body>

    </html>