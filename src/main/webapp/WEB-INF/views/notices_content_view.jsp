<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 상세보기</title>

    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/notices_content_view.css'/>">

    <script>
        // 파일 다운로드 함수
        function downloadFile(path) {
            const encoded = encodeURIComponent(path);
            window.location.href = '<c:url value="/download?path="/>' + encoded;
        }

        // 내용 영역 자동 높이 조정 (현재는 CSS로 처리하는 것이 더 좋으므로 제거 권장)
        /* window.addEventListener('DOMContentLoaded', () => {
          const textarea = document.querySelector('.notice-content'); // div로 변경
          if (textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
          }
        }); */
    </script>
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <section class="notice-view-section">
        <h3 class="page-title">공지사항 상세 내용</h3>

        <table class="notice-table">
            <thead>
            <tr class="title-row">
                <td colspan="4">${dto.notice_title}</td>
            </tr>
            </thead>

            <tbody>
            <tr class="info-row">
                <td class="label">작성자</td>
                <td class="data">관리자</td>

                <td class="label">작성일</td>
                <td class="data">${dto.created_date}</td>
            </tr>

            <tr class="info-row">
                <td class="label">번호</td>
                <td class="data">${dto.notice_no}</td>

                <td class="label">조회수</td>
                <td class="data">${dto.view_count}</td>
            </tr>

            <tr class="file-row">
                <td class="label">첨부파일</td>
                <td colspan="3" class="file-data">
                    <c:choose>
                        <c:when test="${not empty dto.notice_file}">
							<a href="/download?folder=notices&file=${dto.notice_file}">
							    ${dto.notice_file}
							</a>
                        </c:when>
                        <c:otherwise>
                            <span class="no-file">첨부파일 없음</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>

            <tr>
                <td colspan="4" class="content-cell">
                    <div class="notice-content">
                        ${dto.notice_content}
                    </div>
                </td>
            </tr>
            </tbody>
        </table>

        <div class="btn-box">

            <div class="admin-btn-group">
                <c:if test="${sessionScope.role == 'ADMIN'}">
                    <button type="button"
                            onclick="location.href='<c:url value='/notices_modify_view?notice_no=${dto.notice_no}'/>'"
                            class="btn btn-submit">수정</button>

                    <form id="deleteForm" action="<c:url value='/notices_delete'/>" method="post" style="display:inline;">
                        <input type="hidden" name="notice_no" value="${dto.notice_no}" />
                        <button type="submit" class="btn btn-delete">삭제</button>
                    </form>
                </c:if>
            </div>

            <div class="list-btn-group">
                <button type="button"
                        onclick="location.href='<c:url value='/notices_list'/>'"
                        class="btn btn-list">목록보기</button>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>