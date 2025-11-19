var actionForm = $("#actionForm");

//페이지 처리
$(".paginate_button a").on("click",function(e){
	e.preventDefault();
	console.log("click~!!!");
	console.log("@# href=>"+$(this).attr("href"));

	actionForm.find("input[name='pageNum']").val($(this).attr("href"));
	//actionForm.submit();
	// 버그 처리 (게시글 클릭 후 뒤로가기 누른 후 다른 페이지 클릭할 때 content_view 가 작동되는것을 해결)
	actionForm.attr("action","community_list").submit();
});// end of paginate_button click

//게시물 처리
$(".move_link").on("click",function(e){
	e.preventDefault();
	console.log("move_link click~!!!");
	console.log("@# href=>"+$(this).attr("href"));
	
	var targetBno = $(this).attr("href");

	var bno = actionForm.find("input[name='boardNo]").val();
	if(bno != ""){
		actionForm.find("input[name='boardNo]").remove();
	}

	//content_view?boardNo=${dto.boardNo} 를 actionForm 으로 처리
	actionForm.append("<input type='hidden' name='boardNo' value='"+targetBno+"'>");
	//컨트롤러에 content_view 로 찾아감
	actionForm.attr("action","content_view").submit();
});// end of paginate_button click
