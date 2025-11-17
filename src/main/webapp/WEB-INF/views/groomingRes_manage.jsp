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
<%-- 공통 헤더 부분 포함 (다른 JSP 파일) --%>

<main id="res-manage-main">
    <%-- 메인 컨텐츠 영역 시작 --%>
    <div class="floating-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <%-- 관리 메뉴 네비게이션 (플로팅 메뉴) --%>
            <a href="<c:url value='/user_manage'/>" class="menu-link" id="menu-user">회원정보 관리</a>
            <%-- 회원정보 관리 링크 --%>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link" id="menu-veterinary">진료예약 관리</a>
            <%-- 진료예약 관리 링크 --%>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link active" id="menu-grooming">미용예약 관리</a>
            <%-- 현재 페이지 (미용예약 관리) 링크 및 'active' 클래스 적용 --%>
        </nav>
    </div>

    <section id="res-list-section">
        <%-- 예약 리스트 섹션 시작 --%>
        <h2 class="section-title">미용예약관리</h2>
        <hr class="section-divider">

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
                <%-- 'GroomingResList' 모델 객체를 반복하여 각 예약 항목 출력 --%>
                <tr>
                    <td>${ResList.res_no}</td>
                    <td>${ResList.user_no}</td>
                    <td>${ResList.user_name}</td>
                    <td>${ResList.pet_no}</td>
                    <td>${ResList.pet_name}(${ResList.pet_breed})</td>
                    <td>${ResList.user_phone}</td>
                    <td>
                        <button type="button" onclick="openDetailModal('${ResList.res_no}')">보기</button>
                            <%-- 미용내용(service_item)을 모달로 보기 위한 버튼. 예약번호를 인자로 JavaScript 함수 호출 --%>
                    </td>
                    <td><fmt:formatDate value="${ResList.res_date}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <%-- 예약 날짜 및 시간을 지정된 형식으로 포맷하여 출력 --%>
                    <td>${ResList.res_status}</td>
                    <td>
                        <button type="button" onclick="openMemoModal('${ResList.res_no}')">보기</button>
                            <%-- 추가사항(memo)을 모달로 보기 위한 버튼. 예약번호를 인자로 JavaScript 함수 호출 --%>
                    </td>
                    <td>
                        <c:choose>
                            <%-- 예약완료 상태: 확정 + 취소 버튼 --%>
                            <c:when test="${ResList.res_status eq '예약완료'}">
                                <form method="post" action="confirmRes" style="display:inline;">
                                    <input type="hidden" name="res_no" value="${ResList.res_no}"/>
                                    <input type="hidden" name="type" value="grooming"/>
                                    <button type="submit">예약확정</button>
                                </form>
                                <button type="button" onclick="openCancelModal('${ResList.res_no}', 'grooming')">예약취소</button>
                            </c:when>

                            <%-- 예약확정 상태: 취소 버튼만 --%>
                            <c:when test="${ResList.res_status eq '예약확정'}">
                                <button type="button" onclick="openCancelModal('${ResList.res_no}', 'grooming')">예약취소</button>
                            </c:when>

                            <%-- 예약취소 상태: 처리 불가 --%>
                            <c:otherwise>
                                <span>-</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                </tr>

                <tr id="detailModal-${ResList.res_no}" class="info-modal-row" style="display:none;">
                        <%-- 미용내용 상세 정보를 보여줄 행. 초기에는 숨김 처리 --%>
                    <td colspan="11">
                        <div class="info-modal-content">
                            <h3>진료내용</h3> <%-- (표시 텍스트는 '진료내용'으로 되어있음) --%>
                            <p>${ResList.service_item}</p>
                                <%-- 실제 미용 서비스 항목 내용 --%>
                            <button type="button" onclick="closeDetailModal('${ResList.res_no}')">닫기</button>
                        </div>
                    </td>
                </tr>

                <tr id="memoModal-${ResList.res_no}" class="info-modal-row" style="display:none;">
                        <%-- 추가사항 상세 정보를 보여줄 행. 초기에는 숨김 처리 --%>
                    <td colspan="11">
                        <div class="info-modal-content">
                            <h3>추가사항</h3>
                            <p>${ResList.memo}</p>
                                <%-- 예약 시 작성된 추가 메모 내용 --%>
                            <button type="button" onclick="closeMemoModal('${ResList.res_no}')">닫기</button>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>

    <div id="modalOverlay"></div>
    <%-- 모달이 열릴 때 뒷 배경을 어둡게 처리하는 오버레이 요소 --%>

    <div id="cancelModal">
        <%-- 예약 취소 사유를 입력받는 모달 창 --%>
        <form method="post" action="cancelRes">
            <%-- 예약 취소 처리를 위한 폼 (POST 요청) --%>

            <input type="hidden" name="res_no" id="cancelResNo"/>
            <%-- 🌟 수정: value="${ResList.res_no}" 제거하고, id="cancelResNo" 추가 🌟 --%>

            <input type="hidden" name="type" id="cancelResType" value="grooming"/>
            <%-- 🌟 수정: value를 grooming 고정하고, id="cancelResType" 추가 🌟 --%>

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