<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Q&A 내용보기</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/qna_content_view.css">

</head>
<body>

<!-- 공통 Header 삽입 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <section class="notice-view">

        <table border="1">

            <!-- 제목 -->
            <tr class="title">
                <td>제목</td>
                <td colspan="6">${dto.qna_title}</td>
            </tr>

            <!-- 글 정보 -->
            <tr class="writer_tr">
                <td class="num">번호</td>
                <td class="write_td">${dto.qna_no}</td>

                <td class="answered">답변여부</td>
                <td class="answered2">${dto.is_answered}</td>

                <td class="date">작성일</td>
                <td colspan="2" class="write_td">${dto.created_date}</td>
            </tr>

            <!-- 첨부파일 -->
            <tr>
                <td class="file">첨부파일</td>
                <td colspan="6" class="file2">${dto.qna_file}</td>
            </tr>

            <!-- 질문 내용 -->
            <tr>
                <td colspan="7">
                    <div class="qna-content">
                        ${dto.qna_content}
                    </div>
                </td>
            </tr>

            <!-- 답변 영역 -->
            <tr>
                <td colspan="7" class="answer-title">답변</td>
            </tr>

            <tr class="writer_tr">
                <td class="date">작성자</td>
                <td>관리자</td>
                <td class="date">작성일</td>
                <td colspan="4">${reply.created_date}</td>
            </tr>

            <tr>
                <td colspan="7">
                    <div class="qna-content">
                        ${reply.reply_content}
                    </div>
                </td>
            </tr>

        </table>

        <!-- 관리자 버튼 -->
        <c:if test="${sessionScope.role == 'ADMIN'}">

            <!-- 답변하기 -->
            <div class="btn-box">
                <button type="button" 
                        onclick="location.href='/qna_reply_write?qna_no=${dto.qna_no}'"
                        class="btn-list">
                    답변하기
                </button>
            </div>

            <!-- 수정 / 삭제 -->
            <div class="btn-box">
                <button type="button"
                    onclick="location.href='/qna_modify_view?qna_no=${dto.qna_no}'"
                    class="btn-submit">
                    수정
                </button>

                <form action="/qna_delete" method="post" style="display:inline;">
                    <input type="hidden" name="qna_no" value="${dto.qna_no}" />
                    <button type="submit" class="btn-delete">삭제</button>
                </form>
            </div>

        </c:if>

        <!-- 목록보기 -->
        <div class="btn-box">
            <button type="button"
                onclick="location.href='/qna_page'"
                class="btn-list">
                목록보기
            </button>
        </div>

    </section>
</main>

<!-- 공통 Footer 삽입 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
