<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/jquery.bxslider.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/find_account.css">
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

    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <main>
        <div class="find-container">
        
            <h2>아이디 찾기 🔑</h2>
            <p>아래 정보를 입력하시면 <br>아이디를 메일로 발송해 드립니다.</p>
        <form method="post" action="findAccountOK">

                <div class="input-group">
                    <input class="email" type="text" name="account_email" placeholder="이메일">                
                </div>
                <div class="input-group">
                    <input class="phone" type="text" name="account_phone" placeholder="전화번호">                
                </div>
                <div>
                    <input class="submit-btn" type="submit" value="확인">  
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

    </main> 

    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>