<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Q&A</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/qna_page.css">

</head>
<body>

<!-- 공통 헤더 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<!-- FAQ 영역 -->
<div align="center">
    <table>
        <br><br><br>
        <caption>
            <h2 class="qna_title">자주 묻는 질문</h2>
            <br><br>
        </caption>

        <tr>
            <td>
                <details id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
            <td>
                <details style="width:300px;" id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
            <td>
                <details style="width:300px;" id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
        </tr>

        <tr>
            <td>
                <details style="width:300px;" id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
            <td>
                <details style="width:300px;" id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
            <td>
                <details style="width:300px;" id="det">
                    <summary id="sum">질문</summary>
                    <p id="p">내용</p>
                </details>
            </td>
        </tr>
    </table>
</div>

<!-- QNA 리스트 -->
<main>
    <br><br>
    <h2 id="top" class="qna_title">(질문사항) Q&A</h2>
    <br><br>

    <div class="div">
        <table class="table">
            <tr class="column">
                <th width="90" id="number">번호</th>
                <th width="600" id="title">제목</th>
                <th id="answered">답변여부</th>
                <th id="date">작성일</th>
            </tr>

            <c:forEach var="dto" items="${dd}">
                <tr>
                    <td>${dto.qna_no}</td>
                    <td class="title">
                        <a href="/qna_view?qna_no=${dto.qna_no}">
                            ${dto.qna_title}
                        </a>
                    </td>
                    <td>${dto.is_answered}</td>
                    <td>${dto.created_date}</td>
                </tr>
            </c:forEach>

            <tr class="button_tr">
                <td colspan="5">
                    <a href="/qna_write" class="button">질문작성</a>
                </td>
            </tr>
        </table>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagination">
        <c:if test="${totalPage > 1}">
            <c:if test="${currentPage > 1}">
                <a href="/qna_page?page=${currentPage - 1}" class="page-btn">이전</a>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPage}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="page-btn active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="/qna_page?page=${i}" class="page-btn">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPage}">
                <a href="/qna_page?page=${currentPage + 1}" class="page-btn">다음</a>
            </c:if>
        </c:if>
    </div>
</main>

<!-- 공통 푸터 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
