<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원 상세보기</title>
    <link rel="stylesheet" href="<c:url value='/css/user_detail.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/mainpage.css'/>">
    <script src="<c:url value='/js/jquery.js'/>"></script>
    <script src="<c:url value='/js/manage_page.js'/>"></script>
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
                                <a href="#"
                                   onclick="confirmToggle(
                                           '${UserInfo.user_no}',
                                           'ACTIVE',
                                           '${cri.pageNum}',
                                           '${cri.amount}'
                                           );"
                                   class="btn-toggle-inline inactive">활동정지 해제</a>
                            </c:when>
                            <c:otherwise>
                                <a href="#"
                                   onclick="openSuspendModal(
                                           '${UserInfo.user_no}',
                                           'INACTIVE',
                                           '${cri.pageNum}',
                                           '${cri.amount}'
                                           );"
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
		
		<div class="user-membership-box">
		    <h3>멤버십 정보</h3>

		    <p><strong>현재 등급:</strong> ${UserInfo.current_grade}</p>
		    <p><strong>만료일:</strong> ${UserInfo.grade_expiry_date}</p>

		    <h4>등급 이력</h4>
		    <table class="membership-table">
		        <thead>
		        <tr>
		            <th>등급</th>
		            <th>시작일</th>
		            <th>종료일</th>
		            <th>평가일</th>
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach var="g" items="${GradeHistory}">
		            <tr>
						<td><span class="grade-badge grade-${g.grade}">${g.grade}</span></td>
		                <td>${g.start_date}</td>
		                <td>${g.end_date}</td>
		                <td>${g.evaluation_date}</td>
		            </tr>
		        </c:forEach>
		        </tbody>
		    </table>

			<h4>
			    서비스 이용 내역
			    <a href="<c:url value='/user_servicehistory?user_no=${UserInfo.user_no}'/>"
			       class="btn-history-add">+ 등록</a>
			</h4>
		    <table class="membership-table">
		        <thead>
		        <tr>
		            <th>방문일</th>
		            <th>종류</th>
		            <th>항목</th>
		            <th>메모</th>
		            <th>완료 여부</th>
					<th>완료일</th>
					<th>완료 처리</th>
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach var="s" items="${ServiceHistory}">
		            <tr>
		                <td>${s.service_date}</td>
		                <td>${s.service_type}</td>
		                <td>${s.service_item}</td>
		                <td>${s.details_memo}</td>
		                <td>${s.completion_status}</td>
						<td>${s.completion_date}</td>
						<td>
						    <c:choose>
						        <c:when test="${s.completion_status eq 'N'}">
						           <form action="<c:url value='/user_serviceComplete'/>"
						                  method="post" style="display:inline;">
						                <input type="hidden" name="service_no" value="${s.service_no}">
						                <input type="hidden" name="user_no" value="${UserInfo.user_no}">
						                <button type="submit" class="btn-finish">완료</button>
						           </form>
						        </c:when>
											
						        <c:otherwise>
						            <span class="finished-text">완료됨</span>
						        </c:otherwise>
						   </c:choose>
						</td>
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
		

        <%-- 활동정지 해제(ACTIVE)를 위한 숨겨진 POST 폼 --%>
        <form method="post" action="UserStatusProcess" id="activateForm" style="display:none;">
            <input type="hidden" name="user_no" id="activateUserNo"/>
            <input type="hidden" name="targetStatus" value="ACTIVE"/>
            <input type="hidden" name="pageNum" id="activatePageNum"/>
            <input type="hidden" name="amount" id="activateAmount"/>
            <input type="hidden" name="suspension_reason" value=""/>
        </form>

        <%-- 💡 CSS 파일로 스타일 분리 --%>
        <div id="modalOverlay"></div>
        <%-- 모달 배경 오버레이. display:none; 등 스타일은 CSS 파일로 이동 --%>

        <%-- 💡 CSS 파일로 스타일 분리 --%>
        <div id="suspendModal">
            <h3>회원 활동정지 사유 입력</h3>

            <%-- 정지 처리 폼 (POST) --%>
            <form method="post" action="UserStatusProcess" id="suspendForm">
                <input type="hidden" name="user_no" id="suspendUserNo"/>
                <input type="hidden" name="targetStatus" value="INACTIVE"/>
                <input type="hidden" name="pageNum" id="suspendPageNum"/>
                <input type="hidden" name="amount" id="suspendAmount"/>

                <label for="suspension_reason">정지 사유:</label><br>
                <textarea name="suspension_reason" id="suspension_reason" rows="4" cols="40" required></textarea><br><br>

                <button type="submit" class="btn-suspend-submit">정지 처리</button>
                <button type="button" class="btn-suspend-close" onclick="closeSuspendModal()">닫기</button>
            </form>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>