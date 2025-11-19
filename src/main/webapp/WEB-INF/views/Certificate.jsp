<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="now" value="<%= new java.util.Date() %>"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>확인서 발급</title>
    <link rel="stylesheet" href="<c:url value='/css/Certificate.css'/>">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
    <script src="<c:url value='/js/jquery.js'/>"></script>
    <script src="<c:url value='/js/manage_page.js'/>"></script>
</head>
<body>

<div class="button-container">
    <button onclick="generatePDF()" class="print-button">확인서 PDF로 저장</button>
</div>

<div class="certificate-container">
    <c:choose>
        <c:when test="${certificate.service_type == '미용'}">
            <h2>미용 확인서</h2>
        </c:when>
        <c:otherwise>
            <h2>진료 확인서</h2>
        </c:otherwise>
    </c:choose>

    <table border="1" id="certificate-table">

        <tr class="group-header">
            <td colspan="6" class="header-label">보호자 정보</td>
        </tr>
        <tr>
            <th class="th-user-name">이름</th>
            <td colspan="2"><c:out value="${certificate.user_name}"/></td>
            <th class="th-user-age">연령</th>
            <td colspan="2"><c:out value="${certificate.user_age}"/> 세</td>
        </tr>
        <tr>
            <th colspan="2" class="th-user-id">주민등록번호</th>
            <td colspan="4">
                <%-- 💡 수정된 출력 로직: 앞 6자리와 뒷 7자리를 모두 노출합니다. --%>
                <c:set var="residentId" value="${residentId}"/> <%-- Controller에서 받은 13자리 값 --%>

                <c:choose>
                    <c:when test="${not empty residentId and fn:length(residentId) == 13}">
                        <%-- 앞 6자리 추출 --%>
                        <c:out value="${fn:substring(residentId, 0, 6)}"/>
                        -
                        <%-- 뒷 7자리 추출 --%>
                        <c:out value="${fn:substring(residentId, 6, 13)}"/>
                    </c:when>
                    <c:otherwise>
                        주민등록번호 정보 없음
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr class="row-large">
        <tr class="row-large">
            <th class="th-address">주소</th>
            <td colspan="5">
                <c:out value="${certificate.user_addr}" default=""/>
                <c:out value="${certificate.user_addr_detail}" default=""/>
            </td>
        </tr>

        <tr class="group-header">
            <td colspan="6" class="header-label">동물 정보</td>
        </tr>
        <tr>
            <th class="th-pet-name">이름</th>
            <td><c:out value="${certificate.pet_name}"/></td>
            <th class="th-pet-species">종류</th>
            <td><c:out value="${certificate.pet_species}"/></td>
            <th class="th-pet-breed">품종</th>
            <td><c:out value="${certificate.pet_breed}"/></td>
        </tr>
        <tr>
            <th class="th-pet-gender">성별</th>
            <td colspan="2"><c:out value="${certificate.pet_gender}"/></td>
            <th class="th-pet-age">나이</th>
            <td colspan="2"><c:out value="${certificate.pet_age}"/> 살</td>
        </tr>

        <tr class="group-header">
            <td colspan="6" class="header-label">진료/미용 정보</td>
        </tr>
        <tr>
            <th class="th-date">날짜</th>
            <td colspan="5">
                <fmt:formatDate value="${certificate.service_date}" pattern="yyyy년 MM월 dd일"/>
            </td>
        </tr>
        <tr>
            <th class="th-service-type">분류</th>
            <td colspan="2"><c:out value="${certificate.service_type}"/></td>
            <th class="th-service-item">항목</th>
            <td colspan="2"><c:out value="${certificate.service_item}"/></td>
        </tr>
        <tr>
            <th class="th-in">입원</th>
            <td colspan="2"></td>
            <th class="th-out">퇴원</th>
            <td colspan="2"></td>
        </tr>
        <tr class="row-memo">
            <th class="th-memo">추가사항</th>
            <td colspan="5"><c:out value="${certificate.details_memo}"/></td>
        </tr>

        <tr>
            <c:choose>
                <c:when test="${certificate.service_type == '미용'}">
                    <td colspan="6" class="confirmation-text">상기와 같이 미용 받았음을 확인합니다.</td>
                </c:when>
                <c:otherwise>
                    <td colspan="6" class="confirmation-text">상기와 같이 진료 받았음을 확인합니다.</td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr class="row-footer">
            <td colspan="6" class="footer-info">
                발 행 일: <fmt:formatDate value="${now}" pattern="yyyy년 MM월 dd일"/><br><br>
                의 사 성 명: 김원장 (인) <br><br>
                면 허 번 호: 제2025-부산-98765호
                <br><br>
                주소 및 명칭: 부산광역시 OO구 OO로 123번길 45 MY PET 동물병원 <br><br>
                전 화 번 호: 051-123-4567 (FAX) 051-987-6543 <br><br>

                <div class="right-align-text">
                    의료기관의 장: 김원장 (인)
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>