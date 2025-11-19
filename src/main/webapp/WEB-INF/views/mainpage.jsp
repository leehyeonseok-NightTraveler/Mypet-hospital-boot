<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MY PET 동물병원</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/jquery.bxslider.css">

    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
	
</head>
<body>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<figure>
    <div class="pic">
        <ul class="slide_gallery">
            <li><img src="/img/main.png"></li>
            <li><img src="/img/main2.png"></li>
            <li><img src="/img/main3.png"></li>
        </ul>
    </div>

    <div class="main-text">
        <h1>소중한 반려동물의 평생 주치의</h1>
        <p>
            MY PET 동물병원은 최상의 의료 서비스로<br>
            보호자의 마음까지 치료합니다.
        </p>
    </div>
</figure>

<section class="features">
    <div class="inner">
        <h1>MY PET 동물병원의 약속</h1>

        <div class="wrap">
            <article>
                <img src="/img/1.png">
                <h2>최고의 의료진과 최신 장비</h2>
                <p>대학병원급 최신 장비를 통해 정확한 진단을 
                내리고 반려동물의 건강을 약속합니다.</p>
            </article>

            <article>
                <img src="/img/2.png">
                <h2>보호자가 신뢰하는 병원</h2>
                <p>과잉 진료 없이 정직하게 보호자가 가장 신뢰할 수 있는 선택이 되겠습니다.</p>
            </article>

            <article>
                <img src="/img/3.png">
                <h2>정확한 진단, 정직한 진료</h2>
                <p>오랜 임상 경험과 데이터를 바탕으로 반려동물 의료의 새로운 표준을 제시합니다.</p>
            </article>

            <article>
                <img src="/img/4.png">
                <h2>최적의 치료 솔루션</h2>
                <p>풍부한 임상 경험을 바탕으로 가장 안전하고 효과적인 치료법을 제공합니다.</p>
            </article>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="/js/jquery.js"></script>
<script src="/js/jquery.bxslider.js"></script>


<script>
$(document).ready(function(){
    $('.slide_gallery').bxSlider({
        auto: true,
        pause: 5000
    });
});
</script>

</body>
</html>