<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>미용예약 관리</title>
    <link rel="stylesheet" href="<c:url value='/css/reservation_manage.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <script src="<c:url value='/js/jquery.js'/>"></script>
    <script src="<c:url value='/js/manage_page.js'/>"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<%-- 공통 헤더 부분 포함 --%>

<main id="res-manage-main">
    <%-- 메인 컨텐츠 영역 시작 --%>
    <div class="floating-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <%-- 관리 메뉴 네비게이션 (플로팅 메뉴) --%>
            <a href="<c:url value='/user_manage'/>" class="menu-link" id="menu-user">회원정보 관리</a>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link" id="menu-veterinary">진료예약 관리</a>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link active" id="menu-grooming">미용예약 관리</a>
            <%-- 현재 페이지를 나타내기 위해 'active' 클래스 적용 --%>
        </nav>
    </div>

    <section id="res-list-section">
        <%-- 예약 리스트 섹션 시작 --%>
        <h2 class="section-title">미용예약관리</h2>
        <hr class="section-divider">

        <form method="get" action="groomingRes_manage" class="search-form" id="searchForm">
            <input type="text" name="keyword" id="keyword" placeholder="회원이름 검색"/>
            <select name="status" id="status">
                <option value="">전체</option>
                <option value="예약완료" ${param.status == '예약완료' ? 'selected' : ''}>예약완료</option>
                <option value="예약확정" ${param.status == '예약확정' ? 'selected' : ''}>예약확정</option>
                <option value="예약취소" ${param.status == '예약취소' ? 'selected' : ''}>예약취소</option>
            </select>
            <button type="submit">검색</button>

            <!-- 🔄 초기화 버튼 (폼 안에 위치) -->
            <button type="button" onclick="resetSearchForm()">초기화</button>
        </form>

        <table id="reservation-list-table">
            <%-- 미용 예약 목록 테이블 시작 --%>
            <thead>
            <tr>
                <th>예약번호</th>
                <th>회원번호</th>
                <th>회원이름</th>
                <th>동물번호</th>
                <th>동물이름</th>
                <th>전화번호</th>
                <th>미용내용</th>
                <th>예약날짜</th>
                <th>예약상태</th>
                <th>추가사항</th>
                <th>처리</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="ResList" items="${GroomingResList}">
                <%-- 모델 객체 'GroomingResList'를 반복하여 각 예약 항목 출력 --%>
                <tr>
                    <td>${ResList.res_no}</td>
                    <td>${ResList.user_no}</td>
                    <td>${ResList.user_name}</td>
                    <td>${ResList.pet_no}</td>
                    <td>${ResList.pet_name}(${ResList.pet_breed})</td>
                    <td>${ResList.user_phone}</td>
                    <td>
                        <button type="button" onclick="openDetailModal('${ResList.res_no}')">보기</button>
                            <%-- 미용내용(service_item)을 모달로 보기 위한 버튼 --%>
                    </td>
                    <td><fmt:formatDate value="${ResList.res_date}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <%-- 예약 날짜 및 시간 포맷 출력 --%>
                    <td>${ResList.res_status}</td>
                    <td>
                        <button type="button" onclick="openMemoModal('${ResList.res_no}')">보기</button>
                            <%-- 추가사항(memo)을 모달로 보기 위한 버튼 --%>
                    </td>
                    <td>
                        <c:choose>
                            <%-- 예약완료 상태: 확정 및 취소 버튼 표시 --%>
                            <c:when test="${ResList.res_status eq '예약완료'}">
                                <form method="post" action="confirmRes" style="display:inline;">
                                    <input type="hidden" name="res_no" value="${ResList.res_no}"/>
                                    <input type="hidden" name="type" value="grooming"/>
                                        <%-- 현재 페이지 상태 유지를 위한 hidden 필드 추가 --%>
                                    <input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}"/>
                                    <input type="hidden" name="amount" value="${pageMaker.cri.amount}"/>
                                    <button type="submit">예약확정</button>
                                </form>
                                <button type="button" onclick="openCancelModal('${ResList.res_no}', 'grooming')">예약취소
                                </button>
                            </c:when>

                            <%-- 예약확정 상태: 취소 버튼만 표시 --%>
                            <c:when test="${ResList.res_status eq '예약확정'}">
                                <button type="button" onclick="openCancelModal('${ResList.res_no}', 'grooming')">예약취소
                                </button>
                            </c:when>

                            <%-- 그 외 상태 (예약취소 등): 처리 불가 표시 --%>
                            <c:otherwise>
                                <span>-</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                </tr>

                <tr id="detailModal-${ResList.res_no}" class="info-modal-row" style="display:none;">
                        <%-- 미용내용 상세 정보를 보여줄 숨겨진 행 --%>
                    <td colspan="11">
                        <div class="info-modal-content">
                            <h3>진료내용</h3> <%-- (화면 표시 텍스트: '진료내용') --%>
                            <p>${ResList.service_item}</p>
                            <button type="button" onclick="closeDetailModal('${ResList.res_no}')">닫기</button>
                        </div>
                    </td>
                </tr>

                <tr id="memoModal-${ResList.res_no}" class="info-modal-row" style="display:none;">
                        <%-- 추가사항 상세 정보를 보여줄 숨겨진 행 --%>
                    <td colspan="11">
                        <div class="info-modal-content">
                            <h3>추가사항</h3>
                            <p>${ResList.memo}</p>
                            <button type="button" onclick="closeMemoModal('${ResList.res_no}')">닫기</button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <nav class="pagination-container">
            <ul class="pagination-list">
                <%-- 이전 페이지 링크 --%>
                <c:if test="${pageMaker.prev}">
                    <li class="pagination-item prev paginate_button">
                        <a class="pagination-link"
                           href="groomingRes_manage?pageNum=${pageMaker.startPage - 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">이전</a>
                    </li>
                </c:if>

                <%-- 페이지 번호 링크 반복 출력 --%>
                <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                    <li class="pagination-item page-num paginate_button
                <c:out value='${pageMaker.cri.pageNum == num ? "active" : ""}'/>">
                        <a class="pagination-link"
                           href="groomingRes_manage?pageNum=<c:out value='${num}'/>&amount=<c:out value='${pageMaker.cri.amount}'/>">${num}</a>
                    </li>
                </c:forEach>

                <%-- 다음 페이지 링크 --%>
                <c:if test="${pageMaker.next}">
                    <li class="pagination-item next paginate_button">
                        <a class="pagination-link"
                           href="groomingRes_manage?pageNum=${pageMaker.endPage + 1}&amount=<c:out value='${pageMaker.cri.amount}'/>">다음</a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </section>

    <div id="modalOverlay"></div>
    <%-- 모든 모달이 열릴 때 배경을 어둡게 처리하는 오버레이 요소 --%>

    <div id="cancelModal">
        <%-- 예약 취소 사유를 입력받는 모달 폼 --%>
        <form method="post" action="cancelRes">

            <input type="hidden" name="res_no" id="cancelResNo"/>
            <input type="hidden" name="type" id="cancelResType" value="grooming"/>

            <%-- 예약 취소 후 목록 복귀 시 상태 유지를 위한 hidden 필드 --%>
            <input type="hidden" name="pageNum" id="cancelPageNum" value="${pageMaker.cri.pageNum}"/>
            <input type="hidden" name="amount" id="cancelAmount" value="${pageMaker.cri.amount}"/>

            <label for="cancel_reason">취소 사유:</label><br>
            <textarea name="cancel_reason" id="cancel_reason" rows="4" cols="40" required></textarea><br><br>

            <button type="submit" class="btn-cancel-submit">확인</button>
            <button type="button" class="btn-cancel-close" onclick="closeCancelModal()">닫기</button>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
<%-- 공통 푸터 부분 포함 --%>
</body>
</html>