<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원 상세보기</title>
    <link rel="stylesheet" href="<c:url value='/css/user_detail.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <script src="<c:url value='/js/jquery.js'/>"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main id="user-manage-main">
    <div class="floating-wrapper" id="floating-menu-wrapper">
        <nav class="floating-menu" id="manage-menu">
            <a href="<c:url value='/user_manage'/>" class="menu-link active" id="menu-user">회원정보 관리</a>
            <a href="<c:url value='/veterinaryRes_manage'/>" class="menu-link" id="menu-veterinary">진료예약 관리</a>
            <a href="<c:url value='/groomingRes_manage'/>" class="menu-link" id="menu-grooming">미용예약 관리</a>
        </nav>
    </div>

    <section id="user-detail-section">
        <h2 class="section-UserName" id="user-name">${UserInfo.user_name}님</h2>
        <h4 class="section-UserId" id="user-id">@${UserInfo.user_id}</h4>
        <hr class="section-divider" id="user-divider">

        <div class="user-detail-box" id="user-detail-box">
            <table class="user-detail-table" id="user-detail-table">
                <thead>
                <tr>
                    <th colspan="2">상세정보</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th>이메일</th>
                    <td>${UserInfo.user_email}</td>
                </tr>
                <tr>
                    <th>전화번호</th>
                    <td>${UserInfo.user_phone}</td>
                </tr>
                <tr>
                    <th>생년월일</th>
                    <td>${UserInfo.user_birthday}</td>
                </tr>
                <tr>
                    <th>성별</th>
                    <td>${UserInfo.user_gender}</td>
                </tr>
                <tr>
                    <th>주소</th>
                    <td>${UserInfo.user_addr}</td>
                </tr>
                <tr>
                    <th>활동상태</th>
                    <td>
                        <span class="user-status-text ${UserInfo.user_status}">${UserInfo.user_status}</span>
                        <c:choose>
                            <c:when test="${UserInfo.user_status eq 'INACTIVE'}">
                                <a href="<c:url value='/user_toggle'>
                                        <c:param name='user_no' value='${UserInfo.user_no}'/>
                                        <c:param name='pageNum' value='${cri.pageNum}'/>
                                        <c:param name='amount' value='${cri.amount}'/>
                                        <c:param name='type' value='${cri.type}'/>
                                        <c:param name='keyword' value='${cri.keyword}'/>
                                    </c:url>"
                                   class="btn-toggle-inline inactive">활동정지 해제</a>
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='/user_toggle'>
                                        <c:param name='user_no' value='${UserInfo.user_no}'/>
                                        <c:param name='pageNum' value='${cri.pageNum}'/>
                                        <c:param name='amount' value='${cri.amount}'/>
                                        <c:param name='type' value='${cri.type}'/>
                                        <c:param name='keyword' value='${cri.keyword}'/>
                                    </c:url>"
                                   class="btn-toggle-inline active">활동정지</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="user-pet-box">
            <h3>등록된 보유 펫</h3>
            <table class="user-pet-table">
                <thead>
                <tr>
                    <th>이름</th>
                    <th>종류</th>
                    <th>나이</th>
                    <th>성별</th>
                    <th>등록일</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${PetList}">
                    <tr>
                        <td>${list.pet_name}</td>
                        <td>${list.pet_breed}</td>
                        <td>${list.pet_age}</td>
                        <td>${list.pet_gender}</td>
                        <td>${list.pet_regdate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="user-detail-actions" id="user-detail-actions">
            <a href="<c:url value='/user_manage'>
            <c:param name="pageNum" value="${cri.pageNum}"/>
            <c:param name="amount" value="${cri.amount}"/>
            </c:url>" class="btn-back" id="btn-back">목록으로</a>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>