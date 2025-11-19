<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>미용 내역 - MY PET</title>
    
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/userinfo_style.css">
    
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.10/index.global.min.js'></script>
    
    <style>
        #calendar { max-width: 900px; margin: 20px auto; }
        .fc-event { cursor: pointer; }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main class="mypage-container">

    <aside class="sidemenu">
        <h2>마이페이지 🐾</h2>
        <a href="/mypage_userinfo">내 정보</a>
        <a href="/mypage_petlist">펫 목록</a>
        <a href="/mypage_membership">멤버십</a>
        <a href="/mypage_medical">진료 내역</a>
        <a href="/mypage_grooming" class="active">미용 내역</a>
    </aside>

    <section class="content">
        <h2>✂️ 미용 히스토리</h2>
        <div id='calendar'></div>
    </section>

</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,listYear'
            },
            // ⭐️ 미용 데이터 API 호출
            events: '/api/history/grooming',
            eventColor: '#4d96f7', // 파란색
            
            eventClick: function(info) {
                // window.location.href = "/grooming/detail?no=" + info.event.id;
            }
        });
        calendar.render();
    });
</script>

</body>
</html>