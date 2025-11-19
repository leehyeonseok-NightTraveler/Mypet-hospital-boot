<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title>자유게시판 수정</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/community_write.css">

    <!-- Bootstrap & Summernote -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
</head>

<body>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main>
    <div class="container">
        <p class="caption">자유게시판 수정</p>

        <form method="post" action="/community_modify_ok" enctype="multipart/form-data">

            <!-- 제목 -->
            <div class="row-box">
                <div class="cell label">제목</div>
                <div class="cell input">
                    <input type="text" name="post_title" required value="${content_view.post_title}">
                </div>
            </div>

            <!-- 파일 -->
            <div class="row-box">
                <div class="cell label">첨부파일</div>
                <div class="cell input">

                    <input type="file" id="post_file" name="post_file_upload">

                    <label for="post_file" class="file-select-btn">
                        파일 선택
                    </label>

                    <span id="file_name_display" class="file-name-display">
                        <c:choose>
                            <c:when test="${not empty content_view.post_file}">
                                ${content_view.post_file}
                            </c:when>
                            <c:otherwise>선택된 파일 없음</c:otherwise>
                        </c:choose>
                    </span>

                    <input type="hidden" name="original_file" value="${content_view.post_file}">
                </div>
            </div>

            <!-- 내용 -->
            <div class="row-box" style="display:block; border:none; margin-top:10px;">
                <textarea id="summernote" name="post_content">${content_view.post_content}</textarea>
            </div>

            <!-- 버튼 -->
            <div class="button-group">
                <input type="submit" value="수정 완료" class="btn-submit">
                <button type="button" onclick="cancelModify()" class="btn-cancel">취소</button>
            </div>

            <input type="hidden" name="post_no" value="${content_view.post_no}">
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>
    /* 수정 중 업로드된 임시 파일 저장 */
    let tempFiles = [];

    $(document).ready(function () {

        /* --------------------- Summernote --------------------- */
        $('#summernote').summernote({
            height: 300,
            minHeight: 440,
            lang: 'ko-KR',
            placeholder: '내용을 입력하세요...',
            toolbar: [
                ['style', ['bold','italic','underline','clear']],
                ['font', ['fontname','fontsize','color']],
                ['para', ['ul','ol','paragraph']],
                ['insert', ['link','imageUpload','videoUpload']],
                ['view', ['codeview']]
            ],
            popover: { image: [] },

            buttons: {
                imageUpload: function (context) {
                    var ui = $.summernote.ui;
                    var button = ui.button({
                        contents: '<i class="note-icon-picture"></i> 이미지',
                        tooltip: '이미지 업로드',
                        click: function () {
                            var fileInput = $('<input type="file" accept="image/*">');
                            fileInput.trigger('click');
                            fileInput.on('change', function () {
                                uploadImage(this.files[0], context);
                            });
                        }
                    });
                    return button.render();
                },

                videoUpload: function (context) {
                    var ui = $.summernote.ui;
                    var button = ui.button({
                        contents: '<i class="note-icon-video"></i> 영상',
                        tooltip: '영상 업로드',
                        click: function () {
                            var fileInput = $('<input type="file" accept="video/mp4,video/webm">');
                            fileInput.trigger('click');
                            fileInput.on('change', function () {
                                uploadVideo(this.files[0], context);
                            });
                        }
                    });
                    return button.render();
                }
            },

            callbacks: {
                onInit: function () {
                    $('.note-editable img').attr('draggable', 'true');
                },

                onPaste: function(e) {
                    let items = (e.originalEvent || e).clipboardData.items;
                    for (let i = 0; i < items.length; i++) {
                        if (items[i].type.indexOf("image") !== -1) {
                            e.preventDefault();
                            return;
                        }
                    }
                },

                onDrop: function(e) {
                    if (e.originalEvent.dataTransfer &&
                        e.originalEvent.dataTransfer.files.length > 0) {
                        e.preventDefault();
                        return;
                    }
                }
            }
        });

        /* 첨부파일 이름 표시 */
        $('#post_file').on('change', function () {
            let fileName = $(this).val().split('\\').pop();
            $('#file_name_display').text(fileName || "선택된 파일 없음");
        });
    });


    /* ------------------------ 이미지 업로드 ------------------------ */
    function uploadImage(file, context) {
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/upload/summernote?type=community",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {

                if (data.responseCode === "success") {
                    tempFiles.push(data.url);

                    var tag = '<img src="' + data.url + '" style="max-width:100%; height:auto;">';
                    context.invoke('editor.pasteHTML', tag);
                }
            }
        });
    }

    /* ------------------------ 비디오 업로드 ------------------------ */
    function uploadVideo(file, context) {
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/upload/summernote/video?type=community",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {

                if (data.responseCode === "success") {
                    tempFiles.push(data.url);

                    var tag =
                        '<video controls style="max-width:100%;">' +
                        '<source src="' + data.url + '" type="video/mp4">' +
                        '</video><br>';

                    context.invoke('editor.pasteHTML', tag);
                }
            }
        });
    }

    /* ------------------------ 취소 → 임시 파일 삭제 ------------------------ */
    function cancelModify() {

        if (tempFiles.length > 0) {
            $.ajax({
                url: "/upload/cleanup-temp",
                type: "POST",
                traditional: true,
                data: { files: tempFiles },
                success: function () { console.log("임시 파일 삭제 완료"); }
            });
        }

        location.href = "/community_list";
    }
</script>

</body>
</html>
