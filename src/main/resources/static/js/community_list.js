$(document).ready(function () {
    const actionForm = $("#actionForm");

    // 페이지 클릭
    $(".paginate_button a").on("click", function (e) {
        e.preventDefault();

        const page = $(this).attr("href");
        actionForm.find("input[name='pageNum']").val(page);

        const type = actionForm.find("input[name='type']").val();
        const keyword = actionForm.find("input[name='keyword']").val();

        // 검색이면 => 무조건 검색 URL로 보낸다 (keyword 값이 비어있어도 검색으로 처리)
        if (type && type !== "null" && type !== "undefined") {
            actionForm.attr("action", "/community_search");
        } else {
            actionForm.attr("action", "/community_list");
        }

        actionForm.submit();
    });

    // 게시글 클릭
    $(".move_link").on("click", function (e) {
        e.preventDefault();

        const postNo = $(this).attr("href");

        actionForm.append(`<input type="hidden" name="postNo" value="${postNo}">`);
        actionForm.attr("action", "/community_content_view");
        actionForm.submit();
    });
});
