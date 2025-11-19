$(document).ready(function() {
    const actionForm = $("form");

    $(".btn-submit").on("click", function(e) {
        e.preventDefault();

        const title = $("input[name='post_title']").val().trim();
        const content = $("textarea[name='post_content']").val().trim();

        if (title === "") {
            alert("제목을 입력해 주세요.");
            $("input[name='post_title']").focus();
            return;
        }

        if (content === "") {
            alert("내용을 입력해 주세요.");
            $("textarea[name='post_content']").focus();
            return;
        }

        actionForm.attr("action", "community_write").submit();
    });
});
