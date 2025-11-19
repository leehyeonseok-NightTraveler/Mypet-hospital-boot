<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Q&A 수정</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/qna_modify.css">

    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h2 class="qna-title-header">Q&A 수정</h2>
        <br>

        <!-- ⭐ modify 대신 detail 사용 -->
        <form method="post" action="/QnaModifyProcess" enctype="multipart/form-data">
            <input type="hidden" name="pageNum" value="${cri.pageNum}">
            <input type="hidden" name="amount" value="${cri.amount}">
            <input type="hidden" name="qna_no" value="${detail.qna_no}">
            <input type="hidden" name="original_file" value="${detail.qna_file}">

            <table class="table qna-form-table">
                <tr>
                    <td class="field-label">제목</td>
                    <td>
                        <input type="text" class="form-control" name="qna_title" value="${detail.qna_title}" required>
                    </td>
                </tr>

                <tr>
                    <td class="field-label">첨부파일</td>
                    <td class="file-upload-cell">

                        <!-- ⭐ 현재 파일 출력 -->
                        <c:if test="${not empty detail.qna_file}">
                            <div class="current-file-name">
                                현재 파일: ${detail.qna_file}
                            </div>
                        </c:if>

                        <!-- 새 파일 선택 -->
                        <input type="file" id="qna_file" name="qna_newFile" class="form-control" style="display:none;">
                        <label for="qna_file" class="file-select-btn">새 파일 선택</label>
                        <span id="file_name_display">선택된 파일 없음</span>

                    </td>
                </tr>
            </table>

            <table class="qna-content-area">
                <tr class="content-row">
                    <td colspan="2">
                        <textarea id="summernote" name="qna_content">${detail.qna_content}</textarea>
                    </td>
                </tr>

                <tr class="button-row">
                    <td colspan="2">

                        <input type="submit" value="수정 완료" class="btn-submit">

                        <!-- ⭐ modify → detail 로 변경 -->
                        <button type="button"
                                onclick="location.href='/qna_view?qna_no=${detail.qna_no}'"
                                class="btn-cancel">
                            취소
                        </button>

                    </td>
                </tr>
            </table>
        </form>
    </div>
</main>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

<script>
    $(document).ready(function () {

        $('#summernote').summernote({
            height: 300,
            minHeight: 440,
            lang: 'ko-KR',
            toolbar: [
                ['style', ['bold','italic','underline','clear']],
                ['font', ['fontname','fontsize','color']],
                ['para', ['ul','ol','paragraph']],
                ['insert', ['link','picture']],
                ['view', ['codeview']]
            ]
        });

        // 파일 선택 시 표시
        $('#qna_file').on('change', function () {
            var fileName = $(this).val().split('\\').pop();
            if (fileName) {
                $('#file_name_display').text(fileName);
            } else {
                $('#file_name_display').text("선택된 파일 없음");
            }
        });

    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
