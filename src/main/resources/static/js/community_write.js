$(document).ready(function() {
    const actionForm = $("form");

    $(".btn-submit").on("click", function(e) {
        e.preventDefault();

        const title = $("input[name='post_title']").val().trim();
        const content = $("#summernote").summernote('code').trim();

        if (title === "") {
            alert("제목을 입력해 주세요.");
            $("input[name='post_title']").focus();
            return;
        }

        // summernote 내용이 비었는지 검사
        const plainText = $("<div>").html(content).text().trim();
        if (plainText === "") {
            alert("내용을 입력해 주세요.");
            $("#summernote").summernote('focus');
            return;
        }

        actionForm.attr("action", "/community_write").submit();
    });
});
