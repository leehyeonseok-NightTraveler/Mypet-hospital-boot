<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지사항</title>

  <!-- 정적 리소스 경로 Spring Boot 표준 적용 -->
  <link rel="stylesheet" href="/css/mainpage.css">
  <link rel="stylesheet" href="/css/notices_list.css">
</head>
<body>

<!-- 공통 헤더 include -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
  <h2 class="notice-title">공지사항</h2>

  <table class="table">
    <thead>
      <tr class="column">
        <th id="number">번호</th>
        <th>제목</th>
        <th id="writer">작성자</th>
        <th id="date">작성일</th>
        <th id="count">조회수</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="dto" items="${notices}">
        <tr>
          <td>${dto.notice_no}</td>
          <td class="title">
            <a href="/notices_view?notice_no=${dto.notice_no}">
              <c:if test="${dto.is_fixed == 'Y'}">📌 </c:if>
              <c:out value="${dto.notice_title}" />
            </a>
          </td>
          <td>관리자</td>
          <td>
            <fmt:formatDate value="${dto.created_date}" pattern="yyyy-MM-dd HH:mm:ss" />
          </td>
          <td>${dto.view_count}</td>
        </tr>
      </c:forEach>

      <!-- 관리자 전용 버튼 -->
      <c:if test="${sessionScope.role == 'ADMIN'}">
        <tr class="divider_tr"><td colspan="5"></td></tr>
        <tr class="button_tr">
          <td colspan="5">
            <form action="/notices_write_view" method="get">
              <button type="submit" class="button">글쓰기</button>
            </form>
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>

  <!-- 페이지 네비게이션 -->
  <div class="pagination">
    <c:if test="${currentPage > 1}">
      <a href="/notices_list?page=${currentPage - 1}" class="page-btn">이전</a>
    </c:if>

    <c:forEach var="i" begin="1" end="${totalPage}">
      <c:choose>
        <c:when test="${i == currentPage}">
          <span class="page-btn active">${i}</span>
        </c:when>
        <c:otherwise>
          <a href="/notices_list?page=${i}" class="page-btn">${i}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <c:if test="${currentPage < totalPage}">
      <a href="/notices_list?page=${currentPage + 1}" class="page-btn">다음</a>
    </c:if>
  </div>
</main>

<!-- 공통 푸터 include -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
