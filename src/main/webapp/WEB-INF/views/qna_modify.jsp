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

        <form method="post" action="QnaModifyProcess" enctype="multipart/form-data">
            <input type="hidden" name="pageNum" id="modifyPageNum" value="${cri.pageNum}"/>
            <input type="hidden" name="amount" id="modifyAmount" value="${cri.amount}"/>
            <input type="hidden" name="qna_no" value="<c:out value='${modify.qna_no}'/>">

            <table class="table qna-form-table">
                <tr>
                    <td class="field-label">제목</td>
                    <td>
                        <input type="text" class="form-control" name="qna_title" required
                               placeholder="제목을 입력해 주세요."
                               value="<c:out value='${modify.qna_title}'/>">
                    </td>
                </tr>
                <tr>
                    <td class="field-label">첨부파일</td>
                    <td class="file-upload-cell">

                        <div class="file-control-group">
                            <c:choose>
                                <c:when test="${not empty modify.qna_file}">
                                    <span class="current-file-name">현재 파일: <c:out value='${modify.qna_file}'/></span>
                                    <input type="checkbox" id="delete_file_checkbox" name="delete_file" value="true">
                                    <label for="delete_file_checkbox">파일 삭제</label>

                                    <input type="hidden" name="original_file" value="<c:out value='${modify.qna_file}'/>">
                                </c:when>
                                <c:otherwise>
                                    <span class="current-file-name">현재 파일 없음</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <input type="file" id="qna_file" class="form-control" name="qna_newFile" style="display: none;">

                        <label for="qna_file" class="file-select-btn">
                            새 파일 선택
                        </label>

                        <span id="file_name_display" class="file-name-display">선택된 파일 없음</span>
                    </td>
                </tr>
            </table>

            <table class="qna-content-area">
                <tr class="content-row">
                    <td colspan="2">
                        <textarea id="summernote" name="qna_content"> <c:out value='${modify.qna_content}' escapeXml='false'/>
                        </textarea>
                    </td>
                </tr>
                <tr class="button-row">
                    <td colspan="2">
                        <input type="submit" value="수정 완료" class="btn-submit">

                        <button type="button"
                                onclick="location.href='/qna_view?qna_no=<c:out value='${modify.qna_no}'/>'"
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
    $(document).ready(function() {
        $('#summernote').summernote({
            height: 300,
            minHeight: 440,
            lang : 'ko-KR',
            placeholder: '내용을 입력하세요...',
            toolbar: [
                ['style', ['bold','italic','underline','clear']],
                ['font', ['fontname','fontsize','color']],
                ['para', ['ul','ol','paragraph']],
                ['insert', ['link','picture']],
                ['view', ['codeview']]
            ]
        });


        // 파일 이름 표시 로직 (새 파일 선택 시)
        $('#qna_file').on('change', function() {
            var fileName = $(this).val().split('\\').pop();
            if (fileName) {
                $('#file_name_display').text(fileName);
                // 새 파일을 선택하면 '파일 삭제' 체크를 해제합니다.
                $('#delete_file_checkbox').prop('checked', false);
            } else {
                $('#file_name_display').text("선택된 파일 없음");
            }
        });

        // '파일 삭제' 체크박스 클릭 시 새 파일 선택 필드를 초기화 (사용자 편의성)
        $('#delete_file_checkbox').on('click', function() {
            if ($(this).is(':checked')) {
                // 새 파일 선택 필드 초기화
                $('#qna_file').val('');
                $('#file_name_display').text("선택된 파일 없음");
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>