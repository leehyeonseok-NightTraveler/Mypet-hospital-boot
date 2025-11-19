<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>자유게시판</title>

                <!-- 정적 리소스 경로 Spring Boot 표준 적용 -->
                <link rel="stylesheet" href="/css/mainpage.css">
                <link rel="stylesheet" href="/css/community_list.css">
                <script src="${pageContext.request.contextPath}/js/jquery.js"></script>

                <script>
                    const paramType = "${param.type}";
                    const paramKeyword = "${param.keyword}";
                </script>
            </head>

            <body>

                <!-- 공통 헤더 include -->
                <jsp:include page="/WEB-INF/views/common/header.jsp" />

                <main>
                    <a href="/community_list" class="notice-title">자유게시판</a>
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
                                        <a
                                            href="/community_content_view?postNo=${dto.post_no}&pageNum=${pageMaker.cri.pageNum}&amount=${pageMaker.cri.amount}&user_no=${dto.user_no}">
                                            ${dto.post_title}
                                        </a>
                                    </td>
                                    <td>${dto.user_name}</td>
                                    <td>
                                        <fmt:formatDate value="${dto.created_date}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </td>
                                    <td>${dto.view_count}</td>
                                </tr>
                            </c:forEach>

                            <tr class="button_tr">
                                <td colspan="4" class="search">
                                    <form action="/community_search" method="get" class="search-form">
                                        <select name="type">
                                            <option value="title" ${param.type=='title' ? 'selected' : '' }>제목</option>
                                            <option value="content" ${param.type=='content' ? 'selected' : '' }>내용
                                            </option>
                                            <option value="title_content" ${param.type=='title_content' ? 'selected'
                                                : '' }>제목+내용</option>
                                        </select>

                                        <input type="text" name="keyword" placeholder="검색어 입력"
                                            value="${param.keyword}" />

                                        <button type="submit" class="search-btn">검색</button>
                                    </form>
                                </td>


                                <td>
                                    <form action="/community_write_view" method="get">
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
                                        이전
                                    </a>
                                </li>
                            </c:if>
                            <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                                <li class="paginate_button ${pageMaker.cri.pageNum == num ? 'active' : ''}">
                                    <a href="${num}">
                                        ${num}
                                    </a>
                                </li>
                            </c:forEach>
                            <c:if test="${pageMaker.next}">
                                <li class="paginate_button">
                                    <a href="${pageMaker.endPage +1}">
                                        다음
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </div>

                    <form method="get" id="actionForm">
                        <input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
                        <input type="hidden" name="amount" value="${pageMaker.cri.amount}">
                        <input type="hidden" name="type" value="${param.type}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                    </form>


                </main>

                <!-- 공통 푸터 include -->
                <jsp:include page="/WEB-INF/views/common/footer.jsp" />

            </body>

            </html>

            <script src="/js/community_list.js"></script>