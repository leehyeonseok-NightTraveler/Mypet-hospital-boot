<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageName" value="hospital_info" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>병원소개 - MY PET 동물병원</title>

    <!-- 공통 스타일 -->
    <link rel="stylesheet" href="/css/mainpage.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 본문 영역 -->
    <section class="intro-section">
        <div class="inner">
            <h1>소중한 가족을 위한 마음, MY PET이 함께합니다.</h1>
            <p class="sub-heading">
                'MY PET 동물병원'은 단순한 진료를 넘어, 반려동물과 보호자의 행복한 삶을 최우선으로 생각하는 공간입니다.<br>
                말 못 하는 아이들의 작은 아픔까지 헤아리는 섬세함과 보호자의 걱정스러운 마음을 먼저 공감하는 따뜻함으로<br>
                최상의 의료 서비스를 제공할 것을 약속합니다.
            </p>

            <div class="intro-image">
            </div>

            <div class="promise-box">
                <h2>MY PET 동물병원의 약속</h2>

                <div class="promise-list">
                    <div class="promise-item">
                        <h3>정직하고 투명한 진료</h3>
                        <p>과잉 진료 없이 꼭 필요한 치료만을 정직하게 제안하며, 모든 진료 과정을 투명하게 설명하여 보호자님께서 충분히 이해하고 신뢰할 수 있도록 하겠습니다.</p>
                    </div>

                    <div class="promise-item">
                        <h3>최신 장비와 정확한 진단</h3>
                        <p>대학병원 수준의 최신 의료 장비를 통해 질병을 조기에 정확하게 진단하고, 아이에게 가장 안전하고 효과적인 치료 계획을 수립합니다.</p>
                    </div>
                </div>

                <div class="promise-list" style="margin-top: 40px;">
                    <div class="promise-item">
                        <h3>마음을 나누는 따뜻한 주치의</h3>
                        <p>풍부한 임상 경험을 갖춘 의료진이 내 아이를 돌보는 마음으로 정성을 다해 진료합니다. 반려동물이 병원을 편안한 곳으로 기억할 수 있도록 언제나 노력하겠습니다.</p>
                    </div>

                    <div class="promise-item">
                        <h3>평생을 함께하는 건강 파트너</h3>
                        <p>어린 강아지와 고양이부터 노령 동물까지, 생애주기 전반에 걸친 체계적인 건강 관리 프로그램을 통해 소중한 가족의 곁을 오랫동안 지켜드리겠습니다.</p>
                    </div>
                </div>

            </div>
        </div>
    </section>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
