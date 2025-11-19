<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% System.out.println("### content_view user_no = " + request.getAttribute("content_view")); %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>자유게시판 상세보기</title>

                <!-- CSS -->
                <link rel="stylesheet" href="/css/mainpage.css">
                <link rel="stylesheet" href="/css/community_content_view.css">

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

                <!-- 공통 헤더 -->
                <jsp:include page="/WEB-INF/views/common/header.jsp" />

                <div>
                    <h3 class="caption">자유게시판</h3>
                </div>

                <main>
                    <section class="notice-view">
                        <table>
                            <tr class="title">
                                <td>제목</td>
                                <td colspan="6">${content_view.post_title}</td>
                            </tr>

                            <tr class="writer_tr">
                                <td class="writer">작성자 : ${content_view.user_name}</td>
                                <td class="date">작성일</td>
                                <td>
                                    <fmt:formatDate value="${content_view.created_date}"
                                        pattern="yyyy-MM-dd HH:mm:ss" />
                                </td>
                                <td class="num">번호</td>
                                <td>${content_view.post_no}</td>
                                <td class="viewnum">조회수</td>
                                <td>${content_view.view_count}</td>
                            </tr>

                            <tr>
                                <td class="file">첨부파일</td>
                                <td colspan="6" class="file2">
                                    <c:choose>
                                        <c:when test="${not empty content_view.post_file}">
                                            <a href="/download?file=${content_view.post_file}">
                                                ${content_view.post_file}
                                            </a>
                                        </c:when>
                                        <c:otherwise>첨부파일 없음</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <tr>
                                <td colspan="7">
                                    <div class="notice-content">${content_view.post_content}</div>
                                </td>
                            </tr>
                        </table>

                        <div class="btn-box">

                            <!-- 수정 버튼 -->
                            <form action="/community_modify" method="post" style="display:inline;">
                                <input type="hidden" name="post_no" value="${content_view.post_no}">
                                <input type="hidden" name="pageNum" value="${param.pageNum}">
                                <input type="hidden" name="amount" value="${param.amount}">
                                <input type="hidden" name="user_no" value="${content_view.user_no}">
                                <button type="submit" class="btn-submit">수정</button>
                            </form>

                            <!-- 삭제 -->
                            <form action="/community_delete" method="post" style="display:inline;">
                                <input type="hidden" name="post_no" value="${content_view.post_no}">
                                <input type="hidden" name="pageNum" value="${param.pageNum}">
                                <input type="hidden" name="amount" value="${param.amount}">
                                <button type="submit" class="btn-delete">삭제</button>
                            </form>

                            <!-- 목록 -->
                            <button type="button"
                                onclick="location.href='/community_list?pageNum=${param.pageNum}&amount=${param.amount}'"
                                class="btn-list">
                                목록보기
                            </button>

                        </div>
                    </section>
                </main>

                <!-- 공통 푸터 -->
                <jsp:include page="/WEB-INF/views/common/footer.jsp" />

            </body>

            </html>