<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지사항</title>

  <!-- 정적 리소스 경로 Spring Boot 표준 적용 -->
  <link rel="stylesheet" href="/css/mainpage.css">
  <link rel="stylesheet" href="/css/community_list.css">
  <script src="/js/community_list.js"></script>
</head>
<body>

<!-- 공통 헤더 include -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
  <h2 class="notice-title">자유게시판</h2>

  <table class="table">
    <thead>
      <tr class="column">
        <th id="number">번호</th>
        <th>제목</th>
        <th id="writer">작성자</th>
        <th id="date">작성일</th>
        <th id="count">조회수</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="dto" items="${list}">
        <tr>
          <td>${dto.post_no}</td>
          <td class="title">
            <a href="/community_view?post_no=${dto.post_no}">${dto.post_title}</a>
          </td>
          <td>${dto.user_name}</td>
          <td>
            <fmt:formatDate value="${dto.created_date}" pattern="yyyy-MM-dd HH:mm:ss" />
          </td>
          <td>${dto.view_count}</td>
        </tr>
      </c:forEach>

        <tr class="divider_tr"><td colspan="5"></td></tr>
        <tr class="button_tr">
          <td colspan="5">
            <form action="/notices_write_view" method="get">
              <button type="submit" class="button">글쓰기</button>
            </form>
          </td>
        </tr>
    </tbody>
  </table>

	<div class="div_page">
		<ul>
			<c:if test="${pageMaker.prev}">
				<li class="paginate_button">
					<a href="${pageMaker.startPage -1}">
						[Previous]
					</a>
				</li>
			</c:if>
			<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
				<li class="paginate_button" ${pageMaker.cri.pageNum == num ? "style='background-color:yellow'" : ""}>
					<a href="${num}">
						[${num}]
					</a>					
				</li>
			</c:forEach>
			<c:if test="${pageMaker.next}">
				<li  class="paginate_button">
					<a href="${pageMaker.endPage +1}">
						[Next]
					</a>
				</li>
			</c:if>
		</ul>
	</div>
	<form method="get" id="actionForm">
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
		<input type="hidden" name="amount" value="${pageMaker.cri.amount}">
	</form>


</main>

<!-- 공통 푸터 include -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script>
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

	
</script>
