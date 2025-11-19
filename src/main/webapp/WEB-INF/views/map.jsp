<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageName" value="map" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오시는 길 - MY PET 동물병원</title>

    <link rel="stylesheet" href="/css/mainpage.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 본문 시작 -->
    <figure>
        <div class="pic">
            <img src="/img/map.png">
        </div>

        <div class="main-text">
            <h1>오시는 길</h1>
            <p>MY PET 동물병원으로 오시는 길을 안내해드립니다.</p>
        </div>
    </figure>

    <div class="location-container">
        <div class="info">
            <h2>MY PET 동물병원 오시는 길</h2>
            Seoul, Gangnam District, 강남구 테헤란로14길 6

            <h2 style="margin-top:20px;">대중교통 이용</h2>
            지하철 | 서울 신분당선 강남역 1번 출구 도보 5분

            <table class="info-table" style="margin-top:20px;">
                <tr>
                    <th colspan="2">진료시간</th>
                </tr>
                <tr>
                    <th>월 - 금</th>
                    <td>오전 9시 - 오후 7시</td>
                </tr>
                <tr>
                    <th>주말</th>
                    <td>오전 9시 - 오후 6시</td>
                </tr>
                <tr>
                    <th>점심시간</th>
                    <td>오후 12시 30분 - 오후 2시</td>
                </tr>
            </table>
        </div>

		<div class="map">
			<iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3165.378200009597!2d127.03033407640345!3d37.49899722799723!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x357c9ec255555555%3A0x3565475c3365c5bb!2zS0jsoJXrs7TqtZDsnKHsm5A!5e0!3m2!1sko!2skr!4v1763448507329!5m2!1sko!2skr"
			 width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
		</div>
    </div>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
