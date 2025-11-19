<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>진료 / 미용 이력 등록</title>
	<link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="<c:url value='/css/user_detail.css'/>">
	<link rel="stylesheet" href="<c:url value='/css/user_servicehistory.css'/>">
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="service-form-box">
    <h2>진료 / 미용 이력 등록</h2>

    <form action="<c:url value='/user_servicehistoryProcess'/>" method="post">

        <input type="hidden" name="user_no" value="${user_no}">
        <input type="hidden" name="pageNum" value="${cri.pageNum}">
        <input type="hidden" name="amount" value="${cri.amount}">

        <table class="service-form-table">
            <tr>
                <th>방문일</th>
                <td><input type="date" name="service_date" required></td>
            </tr>

			<tr>
			    <th>펫 선택</th>
			       <td>
			           <select name="pet_no" required>
			               <option value="">-- 펫 선택 --</option>
			               <c:forEach var="p" items="${PetList}">
			                   <option value="${p.pet_no}">
			                       ${p.pet_name} (${p.pet_breed})
			                   </option>
			               </c:forEach>
			           </select>
			       </td>
			   </tr>  
            <tr>
                <th>서비스 종류</th>
                <td>
                    <select name="service_type" required>
                        <option value="진료">진료</option>
                        <option value="미용">미용</option>
                    </select>
                </td>
            </tr>

            <tr>
                <th>서비스 항목</th>
                <td>
                    <input type="text" name="service_item" placeholder="예: 종합 검진 / 발톱 미용" required>
                </td>
            </tr>

            <tr>
                <th>상세 메모</th>
                <td>
                    <textarea name="details_memo" id="memo" placeholder="진료 또는 미용 상세 내용을 입력하세요"></textarea>
                </td>
            </tr>

        </table>

        <div class="service-btn-box">
            <button type="submit" class="btn-submit">등록하기</button>
            <a href="<c:url value='/user_detail?user_no=${user_no}'/>" class="btn-cancel">취소</a>
        </div>

    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>
