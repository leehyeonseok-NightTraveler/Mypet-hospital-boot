<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 안내 - MY PET 동물병원</title>

<link rel="stylesheet" href="/css/reservation.css">

</head>
<body>

<!-- 공통 Header -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="main-container">

    <table>
        <tr>
            <td>
                <h3>예약 가능 일자</h3><br>
                <hr>
                <div>평일 : 오전 9시 ~ 오후 6시</div>
                <div>주말 / 공휴일 : 오전 9시 ~ 오후 3시</div>
            </td>

            <td>
                <button class="reserve-btn" onclick="location.href='<c:url value="/reservation_pet_medical"/>'">
                    진료 예약
                </button><br><br>

                <button class="reserve-btn" onclick="location.href='<c:url value="/reservation_pet_medical_check"/>'">
                    예약 조회
                </button>
            </td>

            <td>
                <button class="reserve-btn" onclick="location.href='<c:url value="/reservation_pet_grooming"/>'">
                    미용 예약
                </button><br><br>

                <button class="reserve-btn" onclick="location.href='<c:url value="/reservation_pet_grooming_check"/>'">
                    예약 조회
                </button>
            </td>
        </tr>
    </table>

</main>

<!-- 공통 Footer -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
