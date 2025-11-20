<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자유게시판 보기</title>

    <!-- CSS -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/community_content_view.css">

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>

    <!-- 세션 전달 -->
    <script>
        const sessionUserNo = Number("${sessionScope.loginUser.user_no}");
        const sessionRole    = "${sessionScope.role}";
    </script>
</head>

<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div>
    <a href="/community_list" class="caption">자유게시판</a>
</div>

<main>
    <section class="notice-view">

        <!-- 게시글 정보 -->
        <table>
            <tr class="title">
                <td>제목</td>
                <td colspan="6">${content_view.post_title}</td>
            </tr>

            <tr class="writer_tr">
                <td class="writer">작성자 : ${content_view.user_name}</td>
                <td class="date">작성일</td>
                <td>
                    <fmt:formatDate value="${content_view.created_date}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                            <a href="/download?folder=community&file=${content_view.post_file}">
                                    ${content_view.post_file}
                            </a>
                        </c:when>
                        <c:otherwise>첨부파일 없음</c:otherwise>
                    </c:choose>
                </td>
            </tr>

            <tr>
                <td colspan="7">
                    <div class="notice-content">
                        <c:out value="${content_view.post_content}" escapeXml="false"/>
                    </div>
                </td>
            </tr>
        </table>

        <!-- 버튼 -->
        <div class="btn-box">
            <c:if test="${sessionScope.loginUser.user_no == content_view.user_no}">
                <form action="/community_modify" method="post" style="display:inline;">
                    <input type="hidden" name="post_no" value="${content_view.post_no}">
                    <input type="hidden" name="pageNum" value="${param.pageNum}">
                    <input type="hidden" name="amount" value="${param.amount}">
                    <button type="submit" class="btn-submit">수정</button>
                </form>

                <form action="/community_delete" method="post" style="display:inline;">
                    <input type="hidden" name="post_no" value="${content_view.post_no}">
                    <input type="hidden" name="pageNum" value="${param.pageNum}">
                    <input type="hidden" name="amount" value="${param.amount}">
                    <button type="submit" class="btn-delete">삭제</button>
                </form>
            </c:if>

            <button type="button"
                    onclick="location.href='/community_list?pageNum=${param.pageNum}&amount=${param.amount}'"
                    class="btn-list">
                목록보기
            </button>
        </div>

        <!-- 댓글 입력 -->
        <div class="comment-write-box">
            <input type="text" value="작성자 : ${sessionScope.loginUser.user_name}" disabled class="comment-writer">
            <input type="text" id="commentContent" placeholder="댓글을 입력하세요..." class="comment-input">
            <button onclick="commentWrite()" class="comment-submit-btn">댓글 작성</button>
        </div>

        <!-- 댓글 목록 영역 : id는 한 번만 -->
        <div id="comment-list" class="comment-list-box">
            <table class="comment-table">
                <colgroup>
                    <col style="width:80px;">
                    <col style="width:120px;">
                    <col style="width:auto;">
                    <col style="width:150px;">
                </colgroup>

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
                            <c:if test="${comment.user_no == sessionScope.loginUser.user_no or sessionScope.role == 'ADMIN'}">
                                <button class="comment-del-btn" onclick="deleteComment(${comment.comment_no})">×</button>
                            </c:if>
                        </td>
                        <td>${comment.created_at2}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />


<!-- ============================
     댓글 작성 AJAX
============================ -->
<script>
    function commentWrite() {
        var content = $("#commentContent").val().trim();
        var postNo  = "${content_view.post_no}";

        if (content === "") {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        $.ajax({
            type: "post",
            url: "/comment/save",
            data: {
                comment_content: content,
                post_no: postNo
            },
            success: function (commentList) {
                console.log("서버 응답:", commentList);

                var output = "";
                output += '<table class="comment-table">';
                output += '  <colgroup>';
                output += '    <col style="width:80px;">';
                output += '    <col style="width:120px;">';
                output += '    <col style="width:auto;">';
                output += '    <col style="width:150px;">';
                output += '  </colgroup>';
                output += '  <tr>';
                output += '    <th>댓글번호</th>';
                output += '    <th>작성자</th>';
                output += '    <th>내용</th>';
                output += '    <th>작성시간</th>';
                output += '  </tr>';

                commentList.forEach(function(c, i) {
                    var canDelete = (Number(c.user_no) === sessionUserNo || sessionRole === "ADMIN");

                    output += '  <tr>';
                    output += '    <td>' + (i + 1) + '</td>';
                    output += '    <td>' + (c.user_name || '') + '</td>';
                    output += '    <td class="comment-text">';
                    output +=          (c.comment_content || '');
                    if (canDelete) {
                        output += '      <button class="comment-del-btn" onclick="deleteComment(' + c.comment_no + ')">×</button>';
                    }
                    output += '    </td>';
                    output += '    <td>' + (c.created_at2 || '') + '</td>';
                    output += '  </tr>';
                });

                output += '</table>';

                $("#comment-list").html(output);
                $("#commentContent").val("");
            },
            error: function () {
                alert("댓글 작성은 로그인 후 이용할 수 있습니다.");
            }
        });
    }
</script>


<!-- ============================
     댓글 삭제 AJAX
============================ -->
<script>
    function deleteComment(commentNo) {
        if (!confirm("댓글을 삭제할까요?")) return;

        $.ajax({
            url: "/comment/deleteComment",
            type: "POST",
            data: { comment_no: commentNo },
            success: function (result) {
                if (result === "success") {
                    alert("삭제되었습니다.");
                    location.reload();
                } else {
                    alert("삭제 실패");
                }
            },
            error: function () {
                alert("서버 오류");
            }
        });
    }
</script>

</body>
</html>