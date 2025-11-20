<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자유게시판 작성</title>

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
        <a href="/community_list" class="caption">자유게시판</a>

        <form method="post" action="/community_write" enctype="multipart/form-data">

            <!-- 제목 -->
            <div class="row-box">
                <div class="cell label">제목</div>
                <div class="cell input">
                    <input type="text" name="post_title" required placeholder="제목을 입력하세요.">
                </div>
            </div>

            <!-- 첨부파일 -->
            <div class="row-box">
                <div class="cell label">파일</div>
                <div class="cell input">
                    <input type="file" id="post_file" name="post_file_upload">

                    <label for="post_file" class="file-select-btn">파일 선택</label>

                    <span id="file_name_display" class="file-name-display">선택된 파일 없음</span>
                </div>
            </div>

            <!-- 내용 -->
            <div class="row-box" style="display:block; border:none; margin-top:10px;">
                <textarea id="summernote" name="post_content"></textarea>
            </div>

            <!-- 버튼 그룹 -->
            <div class="button-group">
                <input type="submit" value="등록" class="btn-submit">
                <button type="button" onclick="cancelWrite()" class="btn-cancel">취소</button>
            </div>

        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>
    /* 🔥 업로드 시 생성된 임시 파일 목록 */
    let tempFiles = [];

    $(document).ready(function () {

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

                /* 🔥 이미지 업로드 */
                imageUpload: function (context) {
                    var ui = $.summernote.ui;

                    var button = ui.button({
                        contents: '<i class="note-icon-picture"></i> 이미지',
                        tooltip: '이미지 업로드',
                        click: function () {
                            var fileInput = $('<input type="file" accept="image/*">');
                            fileInput.trigger('click');

                            fileInput.on('change', function () {
                                var file = this.files[0];
                                uploadImage(file, context);
                            });
                        }
                    });
                    return button.render();
                },

                /* 🔥 비디오 업로드 */
                videoUpload: function (context) {
                    var ui = $.summernote.ui;

                    var button = ui.button({
                        contents: '<i class="note-icon-video"></i> 영상',
                        tooltip: '영상 업로드',
                        click: function () {
                            var fileInput = $('<input type="file" accept="video/mp4,video/webm">');
                            fileInput.trigger('click');

                            fileInput.on('change', function () {
                                var file = this.files[0];
                                uploadVideo(file, context);
                            });
                        }
                    });
                    return button.render();
                }
            },

            callbacks: {
                /* 이미지 이동 가능 */
                onInit: function () {
                    $('.note-editable img').attr('draggable', 'true');
                },

                /* 붙여넣기 이미지 방지 */
                onPaste: function(e) {
                    let items = (e.originalEvent || e).clipboardData.items;
                    for (let i = 0; i < items.length; i++) {
                        if (items[i].type.indexOf("image") !== -1) {
                            e.preventDefault();
                            return;
                        }
                    }
                },

                /* 드롭 이미지 방지 */
                onDrop: function(e) {
                    if (e.originalEvent.dataTransfer && e.originalEvent.dataTransfer.files.length > 0) {
                        e.preventDefault();
                        return;
                    }
                }
            }
        });

        /* 첨부파일 표시 */
        $('#post_file').on('change', function() {
            let fileName = $(this).val().split('\\').pop();
            $('#file_name_display').text(fileName || "선택된 파일 없음");
        });
    });

    /* -------------------------
     * 이미지 업로드 함수
     * ------------------------- */
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

                    tempFiles.push(data.url);  // 임시 저장

                    var tag = '<img src="' + data.url + '" style="max-width:100%; height:auto;">';
                    context.invoke('editor.pasteHTML', tag);

                } else {
                    alert("이미지 업로드 실패");
                }
            }
        });
    }

    /* -------------------------
     * 비디오 업로드 함수
     * ------------------------- */
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

                    tempFiles.push(data.url); // 임시 저장

                    var tag =
                        '<video controls style="max-width:100%;">' +
                        '<source src="' + data.url + '" type="video/mp4">' +
                        '</video><br>';

                    context.invoke('editor.pasteHTML', tag);

                } else {
                    alert("영상 업로드 실패");
                }
            }
        });
    }

    /* -------------------------
     * 취소 → 임시 파일 삭제
     * ------------------------- */
    function cancelWrite() {

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