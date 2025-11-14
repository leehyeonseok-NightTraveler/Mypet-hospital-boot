<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지사항 상세보기</title>

  <!-- 정적 리소스 경로 -->
  <link rel="stylesheet" href="/css/mainpage.css">
  <link rel="stylesheet" href="/css/notices_content_view.css">

  <script>
    window.addEventListener('DOMContentLoaded', () => {
      const textarea = document.querySelector('textarea[readonly]');
      if (textarea) {
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';
      }
    });
  </script>
</head>
<body>

<!-- 공통 헤더 include -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
  <section class="notice-view">
    <table>
      <tr class="title">
        <td>제목</td>
        <td colspan="6">${dto.notice_title}</td>
      </tr>

      <tr class="writer_tr">
        <td class="writer">작성자 : 관리자</td>
        <td class="date">작성일</td>
        <td>${dto.created_date}</td>
        <td class="num">번호</td>
        <td>${dto.notice_no}</td>
        <td class="viewnum">조회수</td>
        <td>${dto.view_count}</td>
      </tr>

      <tr>
        <td class="file">첨부파일</td>
        <td colspan="6" class="file2">
          <c:choose>
            <c:when test="${not empty dto.notice_file}">
              <a href="/download?file=${dto.notice_file}">
                ${dto.notice_file}
              </a>
            </c:when>
            <c:otherwise>첨부파일 없음</c:otherwise>
          </c:choose>
        </td>
      </tr>

      <tr>
        <td colspan="7">
          <div class="notice-content">
            ${dto.notice_content}
          </div>
        </td>
      </tr>
    </table>

    <!-- 관리자 전용 버튼 -->
    <c:if test="${sessionScope.role == 'ADMIN'}">
      <div class="btn-box">
        <button type="button"
                onclick="location.href='/notices_modify_view?notice_no=${dto.notice_no}'"
                class="btn-submit">수정</button>

        <form action="/notices_delete" method="post" style="display:inline;">
          <input type="hidden" name="notice_no" value="${dto.notice_no}" />
          <button type="submit" class="btn-delete">삭제</button>
        </form>

        <button type="button"
                onclick="location.href='/notices_list'"
                class="btn-list">목록보기</button>
      </div>
    </c:if>

  </section>
</main>

<!-- 공통 푸터 include -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
