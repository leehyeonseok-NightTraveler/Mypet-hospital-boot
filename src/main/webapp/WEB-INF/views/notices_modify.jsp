<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 수정</title>

    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/notice_modify.css'/>">

    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

    <script>
        $(document).ready(function () {
            // 1. Summernote 초기화
            $('#summernote').summernote({
                height: 300,
                lang: 'ko-KR',
                placeholder: '내용을 입력하세요...',
                toolbar: [
                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['fontname', 'fontsize', 'color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['insert', ['link', 'picture']],
                    ['view', ['codeview']]
                ],
                fontNames: ['맑은 고딕', '굴림', '돋움', 'Arial', 'Courier New'],
                fontSizes: ['10', '12', '14', '16', '18', '20']
            });

            // 2. 파일 업로드 이름 표시 스크립트
            $('#noticeFileUpload').on('change', function() {
                var fileName = $(this).val().split('\\').pop();
                if (fileName) {
                    $('#fileNameDisplay').text(fileName);
                    $('#currentFileDisplay').hide(); // 새 파일 선택 시 기존 파일 정보 숨김
                } else {
                    $('#fileNameDisplay').text('선택된 파일 없음');
                    $('#currentFileDisplay').show(); // 선택 취소 시 기존 파일 정보 다시 표시
                }
            });

            // 3. 취소 버튼 이벤트 핸들러
            $('.btn-cancel').on('click', function() {
                location.href = '<c:url value="/notices_list"/>';
            });
        });
    </script>
</head>

<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h3 class="page-title">공지사항 수정</h3>

        <form method="post" action="<c:url value='/notices_modify'/>" enctype="multipart/form-data">
            <input type="hidden" name="notice_no" value="${dto.notice_no}">

            <table class="table modify-info-table">
                <tbody>
                <tr>
                    <td class="label-cell">제목</td>
                    <td>
                        <input type="text" class="form-control" name="notice_title"
                               value="${dto.notice_title}" required>
                    </td>
                </tr>

                <tr>
                    <td class="label-cell">첨부파일</td>
                    <td>
                        <div class="file-upload-wrapper">
                            <label for="noticeFileUpload" class="file-upload-button">파일 선택</label>
                            <input type="file" id="noticeFileUpload" name="notice_file_upload" class="form-control-file">
                            <span id="fileNameDisplay" class="file-upload-name">선택된 파일 없음 (수정 안 함)</span>
                        </div>
                        <c:if test="${not empty dto.notice_file}">
                            <p id="currentFileDisplay" class="current-file-info">
                                **현재 파일:** ${dto.notice_file}
                            </p>
                        </c:if>
                    </td>
                </tr>
                </tbody>
            </table>

            <div class="content-editor-area">
                <textarea id="summernote" name="notice_content">${dto.notice_content}</textarea>
            </div>

            <div class="btn-box">
                <input type="submit" value="수정" class="btn-submit">
                <button type="button" class="btn-cancel">
                    취소
                </button>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>