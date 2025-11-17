<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Q&A 작성</title>

<link rel="stylesheet" href="/css/mainpage.css">
<link rel="stylesheet" href="/css/qna_write.css">

<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">

</head>
<body>

<!-- 공통 헤더 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h2 align="center" id="h22">Q&A</h2>
        <br>

        <form method="post" action="/qna_write_ok" enctype="multipart/form-data">
            <table class="table">
                <tr>
                    <td width="80" id="center1">제목</td>
                    <td><input type="text" class="form-control" name="qna_title" required></td>
                </tr>
                <tr>
                    <td id="center2">첨부파일</td>
                    <td><input type="file" class="form-control" name="qna_file_upload"></td>
            </table>

            <table id="table2">
                <tr class="content" align="left">
                    <td colspan="2">
                        <textarea id="summernote" name="qna_content"></textarea>
                    </td>
                </tr>
                <tr class="content2">
                    <td colspan="2" align="center">
                        <input type="submit" value="등록" class="btn-submit">
                        <button type="button" onclick="location.href='/qna_page'" class="btn-cancel">취소</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</main>

<!-- Summernote scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

<script>
$(document).ready(function() {
    $('#summernote').summernote({
        height: 300,
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
});
</script>

<!-- 공통 푸터 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
