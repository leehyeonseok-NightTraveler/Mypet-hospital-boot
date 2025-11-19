<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지사항 작성</title>

    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/notice_write.css'/>">

    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

    <script>
        $(document).ready(function () {
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

            $('.btn-cancel').on('click', function() {
                location.href = '<c:url value="/notices_list"/>';
            });

            // --- 파일 업로드 이름 표시 스크립트 추가 ---
            $('#noticeFileUpload').on('change', function() {
                var fileName = $(this).val().split('\\').pop(); // 파일 경로에서 파일명만 추출
                if (fileName) {
                    $('#fileNameDisplay').text(fileName);
                } else {
                    $('#fileNameDisplay').text('선택된 파일 없음');
                }
            });
            // --- 파일 업로드 이름 표시 스크립트 추가 끝 ---
        });
    </script>
</head>

<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h3>공지사항 작성</h3>

        <form method="post" action="<c:url value='/notices_write'/>" enctype="multipart/form-data">

            <table class="table write-info-table">
                <tbody>
                <tr>
                    <td class="label">제목</td>
                    <td>
                        <input type="text" class="form-control" name="notice_title" placeholder="제목을 입력하세요." required>
                    </td>
                </tr>
                <tr>
                    <td class="label">첨부파일</td>
                    <td>
                        <div class="file-upload-wrapper">
                            <label for="noticeFileUpload" class="file-upload-button">파일 선택</label>
                            <input type="file" id="noticeFileUpload" name="notice_file_upload" class="form-control-file">
                            <span id="fileNameDisplay" class="file-upload-name">선택된 파일 없음</span>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>

            <div class="content-editor-area">
                <textarea id="summernote" name="notice_content"></textarea>
            </div>

            <div class="btn-area">
                <input type="submit" value="등록" class="btn-submit">
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