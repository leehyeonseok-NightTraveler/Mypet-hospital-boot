<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>공지사항 작성</title>

  <!-- 정적 리소스 -->
  <link rel="stylesheet" href="/css/mainpage.css">
  <link rel="stylesheet" href="/css/notice_write.css">

  <!-- Bootstrap & Summernote -->
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
    });
  </script>
</head>

<body>

<!-- 공통 헤더 include -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
  <div class="container">
    <h3>공지사항 작성</h3>

    <form method="post" action="/notices_write"  enctype="multipart/form-data">
      <table class="table">
        <tr>
          <td class="label">제목</td>
          <td>
            <input type="text" class="form-control" name="notice_title" required>
          </td>
        </tr>

        <tr>
          <td class="label">첨부파일</td>
          <td>
			<input type="file" name="notice_file_upload" class="form-control">
          </td>
        </tr>
      </table>

      <table id="content-table">
        <tr>
          <td colspan="2">
            <textarea id="summernote" name="notice_content"></textarea>
          </td>
        </tr>

        <tr>
          <td colspan="2" align="center" class="btn-area">
            <input type="submit" value="등록" class="btn-submit">
            <button type="button" onclick="location.href='/notices_list'" class="btn-cancel">
              취소
            </button>
          </td>
        </tr>
      </table>
    </form>
  </div>
</main>

<!-- 공통 푸터 include -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
