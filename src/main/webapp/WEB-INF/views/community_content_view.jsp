<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <% System.out.println("### content_view user_no=" + request.getAttribute(" content_view")); %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>자유게시판 보기</title>

                    <!-- CSS -->
                    <link rel="stylesheet" href="/css/mainpage.css">
                    <link rel="stylesheet" href="/css/community_content_view.css">
                    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
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
                        <a href="/community_list" class="caption">자유게시판</a>
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

                            <!-- 버튼 영역 -->
                            <div class="btn-box">
                                <form action="/community_modify" method="post" style="display:inline;">
                                    <input type="hidden" name="post_no" value="${content_view.post_no}">
                                    <input type="hidden" name="pageNum" value="${param.pageNum}">
                                    <input type="hidden" name="amount" value="${param.amount}">
                                    <input type="hidden" name="user_no" value="${content_view.user_no}">
                                    <button type="submit" class="btn-submit">수정</button>
                                </form>

                                <form action="/community_delete" method="post" style="display:inline;">
                                    <input type="hidden" name="post_no" value="${content_view.post_no}">
                                    <input type="hidden" name="pageNum" value="${param.pageNum}">
                                    <input type="hidden" name="amount" value="${param.amount}">
                                    <input type="hidden" name="user_no" value="${content_view.user_no}">
                                    <button type="submit" class="btn-delete">삭제</button>
                                </form>

                                <button type="button"
                                    onclick="location.href='/community_list?pageNum=${param.pageNum}&amount=${param.amount}'"
                                    class="btn-list">
                                    목록보기
                                </button>
                            </div>

                            <!-- 댓글 작성 -->
                            <!-- 댓글 작성 -->
                            <div class="comment-write-box">
                                <input type="text" id="commentWriter" value="작성자 : ${user_name}" disabled
                                    class="comment-writer">

                                <input type="text" id="commentContent" placeholder="댓글을 입력하세요..." class="comment-input">

                                <button onclick="commentWrite()" class="comment-submit-btn">댓글 작성</button>
                            </div>

                            <!-- 댓글 목록 -->
                            <div id="comment-list" class="comment-list-box">
                                <table class="comment-table">
                                    <tr>
                                        <th>댓글번호</th>
                                        <th>작성자</th>
                                        <th>내용</th>
                                        <th>작성시간</th>
                                    </tr>

                                    <c:forEach items="${commentList}" var="comment" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td>
                                            <td>${comment.user_name}</td>
                                            <td class="comment-text">
                                                ${comment.comment_content}

                                                <!-- 본인 댓글만 삭제 버튼 보임 -->
                                                <c:if test="${comment.user_no == sessionScope.user_no}">
                                                    <button class="comment-del-btn"
                                                        onclick="deleteComment(${comment.comment_no})">×</button>
                                                </c:if>
                                            </td>
                                            <td>${comment.created_at2}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>

                        </section>
                    </main>

                    <!-- 공통 푸터 -->
                    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

                </body>
                <script>
                    function commentWrite() {
                        const writer = $("#commentWriter").val().trim();
                        const content = $("#commentContent").val().trim();
                        const no = "${content_view.post_no}";

                        if (writer === "" || content === "") {
                            alert("작성자와 내용을 입력하세요.");
                            return;
                        }

                        $.ajax({
                            type: "post",
                            url: "/comment/save",
                            data: {
                                user_name: writer,
                                comment_content: content,
                                post_no: no
                            },
                            success: function (commentList) {

                                alert("댓글 작성 완료");

                                let output = '<table class="comment-table">';
                                output += "<tr><th>댓글번호</th><th>작성자</th><th>내용</th><th>작성시간</th></tr>";

                                commentList.forEach((c, i) => {
                                    output += "<tr>";
                                    output += "<td>" + (i + 1) + "</td>";
                                    output += "<td>" + c.user_name + "</td>";
                                    output += "<td class='comment-text'>" + c.comment_content + "</td>";
                                    output += "<td>" + c.created_at2 + "</td>";
                                    output += "</tr>";
                                });

                                output += "</table>";

                                $("#comment-list").html(output);
                            },

                            error: function () {
                                alert("댓글 등록 실패!");
                            }
                        });
                    }

                </script>


                </html>