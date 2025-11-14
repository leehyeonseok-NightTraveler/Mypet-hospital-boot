<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 재설정 - MY PET 동물병원</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/reset_password.css">

    <script>
    function validateForm() {
        var newPassword = document.getElementById("user_pwd").value;
        var confirmPassword = document.getElementById("user_pwd_confirm").value;

        if (newPassword === "" || confirmPassword === "") {
            alert("비밀번호를 입력해 주세요.");
            return false;
        }
        if (newPassword !== confirmPassword) {
            alert("입력한 비밀번호가 서로 일치하지 않습니다.");
            return false;
        }
        return true;
    }
    </script>
</head>
<body>

<!-- 공통 Header+Floating Icons -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="reset-container">
        <h2>비밀번호 재설정 🔒</h2>
        <p>새로운 비밀번호를 입력해 주세요.</p>

        <form action="<c:url value='/updatePassword' />" method="post"
              onsubmit="return validateForm();">

            <input type="hidden" name="user_id" value="${user_id}">

            <div class="input-group">
                <input type="password" id="user_pwd" name="user_pwd"
                       placeholder="새 비밀번호" required>
            </div>

            <div class="input-group">
                <input type="password" id="user_pwd_confirm"
                       name="user_pwd_confirm"
                       placeholder="새 비밀번호 확인" required>
            </div>

            <button type="submit" class="submit-btn">변경하기</button>
        </form>
    </div>
</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<c:if test="${not empty message}">
<script>
    alert("${message}");
</script>
</c:if>

</body>
</html>
