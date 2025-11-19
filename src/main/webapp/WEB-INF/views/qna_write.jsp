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

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <div class="container">
        <h2 class="qna-title-header">Q&A</h2>
        <br>

        <form method="post" action="/qna_write_ok" enctype="multipart/form-data">

            <!-- 제목 + 첨부파일 -->
            <table class="table qna-form-table">
                <tr>
                    <td class="field-label">제목</td>
                    <td>
                        <input type="text" class="form-control" name="qna_title" required
                               placeholder="제목을 입력해 주세요.">
                    </td>
                </tr>
                <tr>
                    <td class="field-label">첨부파일</td>
                    <td class="file-upload-cell">
                        <input type="file" id="qna_file" class="form-control" name="qna_file_upload" style="display: none;">

                        <label for="qna_file" class="file-select-btn">
                            파일 선택
                        </label>

                        <span id="file_name_display" class="file-name-display">선택된 파일 없음</span>
                    </td>
                </tr>
            </table>

            <!-- 작성 영역 -->
            <table class="qna-content-area">
                <tr class="content-row">
                    <td colspan="2">
                        <textarea id="summernote" name="qna_content"></textarea>
                    </td>
                </tr>
                <tr class="button-row">
                    <td colspan="2">
                        <input type="submit" value="등록" class="btn-submit">
                        <button type="button" onclick="cancelWrite()" class="btn-cancel">취소</button>
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
    // 사용자가 업로드한 임시 이미지 저장용 배열
    let tempImages = [];

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
                ['insert', ['link','imageUpload','videoUpload']], 
                ['view', ['codeview']]
            ],
            popover: {
                image: []   // 이미지 팝오버 제거 → 이동 가능
            },
            buttons: {

                // 이미지 업로드 버튼
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
                                uploadImageToServer(file, context);
                            });
                        }
                    });

                    return button.render();
                },

                // 영상 업로드 버튼
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
                                uploadVideoToServer(file, context);
                            });
                        }
                    });

                    return button.render();
                }
            },

            // 초기 설정 및 네이티브 이동 허용
            callbacks: {
                onInit: function() {
                    $('.note-editable img').attr('draggable', 'true');
                },
                onImageUpload: function(files) {},
                onPaste: function(e) {},
                onDrop: function(e) {}
            }
        });


        // 첨부파일 이름 표시
        $('#qna_file').on('change', function() {
            var fileName = $(this).val().split('\\').pop();
            $('#file_name_display').text(fileName || "선택된 파일 없음");
        });

    });


    /* 이미지 업로드 처리 */
    function uploadImageToServer(file, context) {
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/upload/summernote?type=qna",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.responseCode === "success") {

                    tempImages.push(data.url); // 임시 저장 목록에 추가

                    var tag = '<img src="' + data.url +
                        '" style="max-width:100%; height:auto;">';

                    context.invoke('editor.pasteHTML', tag);
                } else {
                    alert("이미지 업로드 실패");
                }
            }
        });
    }

    /* 영상 업로드 처리 */
    function uploadVideoToServer(file, context) {
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
			url: "/upload/summernote/video?type=qna",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.responseCode === "success") {

					tempImages.push(data.url);// 수정 중 임시 비디오 저장
					
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

    /* 취소 버튼 클릭 → 임시 이미지 삭제 요청 */
    function cancelWrite() {

        if (tempImages.length > 0) {
            $.ajax({
                url: "/upload/cleanup-temp",
                type: "POST",
                traditional: true,
                data: { files: tempImages },
                success: function() {
                    console.log("임시 파일 삭제 완료");
                }
            });
        }

        location.href = "/qna_page";
    }

</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
