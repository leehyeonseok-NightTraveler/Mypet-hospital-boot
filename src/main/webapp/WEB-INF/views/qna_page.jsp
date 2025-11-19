<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Q&A | [병원 이름]</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/qna_page.css">

</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div align="center">
    <table>
        <caption>
            <h2 class="qna_title">자주 묻는 질문 (FAQ)</h2>
        </caption>

        <tr>
            <td>
                <details id="det1">
                    <summary id="sum1">질문 1: 진료 예약은 필수인가요?</summary>
                    <p id="p1">내용 1: 네, 원활한 진료를 위해 **사전 예약**을 권장합니다. 예약 없이 방문 시 대기 시간이 길어질 수 있습니다.</p>
                </details>
            </td>
            <td>
                <details id="det2">
                    <summary id="sum2">질문 2: 야간 및 응급 진료도 가능한가요?</summary>
                    <p id="p2">내용 2: 현재 **평일 9시부터 7시까지** 운영하며, 응급 상황 발생 시 반드시 전화로 문의 후 내원해 주시기 바랍니다.</p>
                </details>
            </td>
            <td>
                <details id="det3">
                    <summary id="sum3">질문 3: 진료비는 어떻게 결제하나요?</summary>
                    <p id="p3">내용 3: 현금 및 카드 결제 모두 가능합니다. 진료 항목에 따라 비용이 상이하며, 자세한 내용은 수납 시 문의해 주세요.</p>
                </details>
            </td>
        </tr>

        <tr>
            <td>
                <details id="det4">
                    <summary id="sum4">질문 4: 고양이 진료도 가능한가요?</summary>
                    <p id="p4">내용 4: 네, 본원은 **강아지, 고양이** 외 특수 동물(햄스터, 토끼 등) 진료도 가능합니다. 특수 동물은 사전 문의 필수입니다.</p>
                </details>
            </td>
            <td>
                <details id="det5">
                    <summary id="sum5">질문 5: 강아지 예방접종 비용과 주기는요?</summary>
                    <p id="p5">내용 5: 5종 종합백신 및 코로나 장염 백신 등은 1년에 1회 접종하며, 비용은 내원 시 상세 안내해 드립니다.</p>
                </details>
            </td>
            <td>
                <details id="det6">
                    <summary id="sum6">질문 6: 주차 시설이 있나요?</summary>
                    <p id="p6">내용 6: 병원 건물 지하에 **전용 주차장**이 마련되어 있습니다. 진료 시간 동안 무료로 이용 가능합니다.</p>
                </details>
            </td>
        </tr>
    </table>
</div>

<main>
    <h2 id="top" class="qna_title">(질문사항) Q&A</h2>
    <div class="div">
        <table class="table">
            <tr class="column">
                <th id="number">번호</th>
                <th id="title">제목</th>
                <th id="answered">답변여부</th>
                <th id="date">작성일</th>
            </tr>

            <c:choose>
                <c:when test="${not empty qnaList}">
                    <c:forEach var="list" items="${qnaList}">
                        <tr>
                            <td>${list.qna_no}</td>
                            <td class="title">
                                <a href="<c:url value="/qna_view">
                                <c:param name="qna_no" value="${list.qna_no}"/>
                                <c:param name="pageNum" value="${pageMaker.cri.pageNum}"/>
                                <c:param name="amount" value="${pageMaker.cri.amount}"/>
                                </c:url>">${list.qna_title}</a>
                            </td>
                            <td>${list.is_answered}</td>
                            <td>${list.created_date}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">등록된 질문이 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>

            <tr class="button_tr">
                <td colspan="4">
                    <a href="/qna_write" class="button">질문작성</a>
                </td>
            </tr>
        </table>
    </div>
    <nav class="pagination-container">
        <ul class="pagination-list">
            <%-- 이전 페이지 링크 --%>
            <c:if test="${pageMaker.prev}">
                <li class="pagination-item prev paginate_button">
                    <a class="pagination-link"
                       href="qna_page?pageNum=${pageMaker.startPage - 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">이전</a>
                </li>
            </c:if>

            <%-- 페이지 번호 링크 반복 출력 --%>
            <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                <li class="pagination-item page-num paginate_button
                <c:out value='${pageMaker.cri.pageNum == num ? "active" : ""}'/>">
                    <a class="pagination-link"
                       href="qna_page?pageNum=<c:out value='${num}'/>&amount=<c:out value='${pageMaker.cri.amount}'/>">${num}</a>
                </li>
            </c:forEach>

            <%-- 다음 페이지 링크 --%>
            <c:if test="${pageMaker.next}">
                <li class="pagination-item next paginate_button">
                    <a class="pagination-link"
                       href="qna_page?pageNum=${pageMaker.endPage + 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">다음</a>
                </li>
            </c:if>
        </ul>
    </nav>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>