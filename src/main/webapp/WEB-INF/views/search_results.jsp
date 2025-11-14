<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>검색 결과</title>

    <!-- 공통 CSS -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <!-- 자동완성 검색창 CSS -->
    <link rel="stylesheet" href="/css/auto_search.css">
    <!-- 검색결과 전용 CSS -->
    <link rel="stylesheet" href="/css/search_results.css">
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
</header>

<div class="search-result-container">
    <h2>🔍 검색 결과 목록</h2>

    <!-- 카드형 검색결과 리스트 -->
    <c:forEach var="keyword" items="${keywordList}" varStatus="status">
        <div class="result-card">
            <a href="${siteurlList[status.index]}" target="_blank" class="result-title">
                ${keyword}
            </a>
            <div class="result-url">${siteurlList[status.index]}</div>
        </div>
    </c:forEach>

</div>

<footer>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</footer>

</body>
</html>
