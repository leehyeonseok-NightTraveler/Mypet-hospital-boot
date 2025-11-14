<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>자동완성 검색</title>
  <script src="/js/auto_search.js"></script>
</head>
<body>
  <h2>자동완성 검색</h2>

  <!-- 폼 -->
  <form id="searchForm" action="/search_results" method="get">
    <input type="hidden" name="siteurl" id="siteurl">
    <input type="hidden" name="keyword" id="keyword">
    <input type="text" id="query" placeholder="검색어 입력..." oninput="searchKeyword()" />
    <button type="button" id="sendBtn">검색</button>
  </form>

  <div id="box"></div>

</body>
</html>
