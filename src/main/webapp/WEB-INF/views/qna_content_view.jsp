<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Q&A 내용보기</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/qna_content_view.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <section class="qna-detail-section">
        <table class="qna-detail-table">
            <tr class="info-row qna-title-row">
                <td class="info-label title-label">제목</td>
                <td colspan="5" class="title-value">${detail.qna_title}</td>
            </tr>

            <tr class="info-row meta-data-row">
                <td class="info-label qna-num-label">번호</td>
                <td class="info-value qna-num">${detail.qna_no}</td>

                <td class="info-label answered-label">답변여부</td>
                <td class="info-value answered-status">
                    <c:choose>
                        <c:when test="${detail.is_answered eq 'Y'}">
                            <span class="status-answered">답변 완료</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-pending">답변 대기</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td class="info-label date-label">작성일</td>
                <td class="info-value created-date">${detail.created_date}</td>
            </tr>

            <tr class="info-row file-row">
                <td class="info-label file-label">첨부파일</td>
                <td colspan="5" class="info-value file-download-area">
                    <c:choose>
                        <c:when test="${not empty detail.qna_file}">
                            <c:url var="downloadUrl" value="/download">
                                <c:param name="folder" value="qna"/>
                                <c:param name="file" value="${detail.qna_file}"/>
                            </c:url>

                            <a href="${downloadUrl}">
                                ${detail.qna_file}
                            </a>
                        </c:when>

                        <c:otherwise>첨부파일 없음</c:otherwise>
                    </c:choose>
                </td>
            </tr>

            <!-- Summernote HTML 렌더링 -->
            <tr class="content-row question-content-row">
                <td colspan="6">
                    <div class="content-box">
						<c:out value="${detail.qna_content}" escapeXml="false"/>
                    </div>
                </td>
            </tr>

            <c:if test="${not empty reply}">
                <tr class="answer-header">
                    <td colspan="6" class="answer-title">답변</td>
                </tr>

                <tr class="info-row reply-meta-row">
                    <td class="info-label reply-writer-label">작성자</td>
                    <td class="info-value reply-writer">관리자</td>
                    <td class="info-label reply-date-label">작성일</td>
                    <td colspan="3" class="info-value reply-date">${reply.created_date}</td>
                </tr>

                <tr class="content-row answer-content-row">
                    <td colspan="6">
                        <div class="content-box answer-content-box">
                            ${fn:replace(fn:replace(reply.reply_content, '&lt;', '<'), '&gt;', '>')}
                        </div>
                    </td>
                </tr>
            </c:if>
        </table>

        <!-- ⭐ 숨겨진 textarea raw injection 방지용 -->
        <c:if test="${not empty reply}">
            <input type="hidden" id="replyContentRaw"
                   value="<c:out value='${reply.reply_content}' escapeXml='false' />" />
        </c:if>

        <c:if test="${role eq 'ADMIN'}">
            <div id="reply-form-area" style="display:none;">
                <form action="/ReplyProcess" method="post" id="replyForm">
                    <input type="hidden" name="qna_no" value="${detail.qna_no}" />
                    <input type="hidden" name="mode" id="replyMode" value="" />

                    <h4><span id="formTitle">답변 작성</span></h4>

                    <textarea name="reply_content" id="replyContentArea"
                              rows="6" required placeholder="답변 내용을 입력하세요."></textarea>

                    <div style="text-align: right; margin-top: 10px;">
                        <button type="submit" class="btn btn-reply-submit" id="submitButton">답변 등록</button>
                        <button type="button" class="btn btn-reply-cancel" onclick="hideReplyForm()">취소</button>
                    </div>
                </form>
            </div>
        </c:if>

        <div class="button-container">
            <c:if test="${role eq 'ADMIN'}">
                <div class="admin-buttons">

                    <c:choose>
                        <c:when test="${empty reply}">
                            <button type="button"
                                    onclick="loadCreateForm()"
                                    class="btn btn-reply">
                                답변하기
                            </button>
                        </c:when>

                        <c:otherwise>
                            <!-- ⭐ reply 내용 전달 제거 (문제 해결 핵심) -->
                            <button type="button"
                                    onclick="loadModifyForm()"
                                    class="btn btn-reply-modify">
                                답변 수정
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <form action="/qna_delete" method="post" class="inline-form">
                        <input type="hidden" name="qna_no" value="${detail.qna_no}" />
                        <button type="submit" class="btn btn-delete">삭제</button>
                    </form>
                </div>
            </c:if>

            <c:if test="${role ne 'ADMIN' and user_no eq detail.user_no}">
                <div class="user-buttons">
                    <button type="button"
                            onclick="location.href='/qna_modify?qna_no=${detail.qna_no}&pageNum=${cri.pageNum}&amount=${cri.amount}'"
                            class="btn btn-modify">
                        질문 수정
                    </button>

                    <form action="/qna_delete" method="post" class="inline-form">
                        <input type="hidden" name="qna_no" value="${detail.qna_no}" />
                        <button type="submit" class="btn btn-delete">삭제</button>
                    </form>
                </div>
            </c:if>

            <div class="common-buttons">
                <a href="<c:url value='/qna_page'>
                    <c:param name='pageNum' value='${cri.pageNum}' />
                    <c:param name='amount' value='${cri.amount}' />
                </c:url>" class="btn btn-list" id="btn-back">목록으로</a>
            </div>
        </div>

    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    const formArea = document.getElementById('reply-form-area');
    const replyButton = document.querySelector('.btn-reply');
    const adminButtons = document.querySelector('.admin-buttons');
    const formTitle = document.getElementById('formTitle');
    const replyMode = document.getElementById('replyMode');
    const replyContentArea = document.getElementById('replyContentArea');
    const submitButton = document.getElementById('submitButton');

    function loadCreateForm() {
        formTitle.textContent = '답변 작성';
        replyMode.value = 'create';
        replyContentArea.value = '';
        submitButton.textContent = '답변 등록';

        formArea.style.display = 'block';
        replyButton.style.display = 'none';
        formArea.scrollIntoView({ behavior: 'smooth' });
    }

    function loadModifyForm() {
        var raw = document.getElementById("replyContentRaw").value;

        formTitle.textContent = '답변 수정';
        replyMode.value = 'modify';
        replyContentArea.value = raw.trim();
        submitButton.textContent = '수정 완료';

        adminButtons.style.display = 'none';
        formArea.style.display = 'block';
        formArea.scrollIntoView({ behavior: 'smooth' });
    }

    function hideReplyForm() {
        formArea.style.display = 'none';

        if (replyMode.value === 'create') {
            replyButton.style.display = 'block';
        }
        if (replyMode.value === 'modify') {
            adminButtons.style.display = 'flex';
        }
    }
</script>

</body>
</html>
