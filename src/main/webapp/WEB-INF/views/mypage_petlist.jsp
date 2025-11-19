<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageName" value="mypage_petlist" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지 - 펫 목록</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/petlist_style.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 본문 영역 -->
    <main class="mypage-container">

        <aside class="sidemenu">
            <h2>마이페이지 🐾</h2>
            <a href="/mypage_userinfo">내 정보</a>
            <a href="/mypage_petlist" class="active">펫 목록</a>
            <a href="/mypage_membership">멤버십</a>
            <a href="/mypage_medical">진료 내역</a>
            <a href="/mypage_grooming">미용 내역</a>
        </aside>

        <section class="content">
            <h2>펫 목록</h2>

            <c:if test="${not empty petList}">
                <div class="pet-grid">

                    <c:forEach var="pet" items="${petList}">
                        <div class="pet-card clickable"
                             onclick="location.href='/mypage_petinfo?pet_no=${pet.pet_no}'">

							 <img src="/upload/${pet.pet_img}" 
							      alt="펫 이미지" 
							      class="pet-img">


                            <h3>${pet.pet_name}</h3>
                            <p>종: ${pet.pet_species}</p>
                            <p>품종: ${pet.pet_breed}</p>
                            <p>성별: ${pet.pet_gender}</p>
                            <p>생일: ${pet.pet_birthday}</p>
                        </div>
                    </c:forEach>

                </div>
            </c:if>

            <c:if test="${empty petList}">
                <p style="margin-top: 20px;">등록된 펫이 없습니다.</p>
            </c:if>

            <div class="add-pet-box">
                <button class="add-pet-btn" onclick="location.href='/pet_add'">
                    펫 등록
                </button>
            </div>

        </section>

    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
