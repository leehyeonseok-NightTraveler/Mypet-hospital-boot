<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <title>자유게시판</title>

            <!-- 정적 리소스 -->
            <link rel="stylesheet" href="/css/mainpage.css">
            <link rel="stylesheet" href="/css/community_write.css">

            <!-- Bootstrap & Summernote -->
            <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
            <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">

            <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
            <script
                src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

            <!-- <script src="${pageContext.request.contextPath}/js/community_write.js"></script> -->

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
                    <p class="caption">수정 페이지</p>

                    <form method="post" action="/community_modify_ok">

                        <div class="row-box">
                            <div class="cell label">제목</div>
                            <div class="cell input">
                                <input type="text" name="post_title" required value="${content_view.post_title}">
                            </div>
                        </div>
                        <div class="row-box">
                            <div class="cell label">파일</div>
                            <div class="cell input">
                                <input type="text" name="post_file" value="${content_view.post_file}">
                            </div>
                        </div>
                        <div>
                            <textarea id="summernote" name="post_content">${content_view.post_content}</textarea>
                        </div>

                        <div class="button-group">
                            <input type="submit" value="수정 완료" class="btn-submit">
                            <button type="button" onclick="location.href='/community_list'" class="btn-cancel">
                                취소
                            </button>
                        </div>

                        <input type="hidden" name="post_no" value="${modify.post_no}">
                    </form>
                </div>
            </main>

            <!-- 공통 푸터 include -->
            <jsp:include page="/WEB-INF/views/common/footer.jsp" />

        </body>

        </html>