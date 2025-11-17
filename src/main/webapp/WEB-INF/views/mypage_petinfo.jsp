<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageName" value="mypage_petinfo" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>펫 상세정보 - MY PET 동물병원</title>

    <!-- 공통 + 개별 CSS -->
    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/petinfo_style.css">
</head>
<body>

    <!-- 공통 HEADER -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 본문 시작 -->
    <main class="mypage-container">

        <!-- 마이페이지 사이드 메뉴 -->
        <aside class="sidemenu">
            <h2>마이페이지 🐾</h2>
            <a href="/mypage_userinfo">내 정보</a>
            <a href="/mypage_petlist" class="active">펫 목록</a>
        </aside>

        <!-- 펫 상세 내용 -->
        <section class="content">
            <h2>펫 상세정보</h2>

            <div class="pet-detail-box">

                <!-- DB 경로 그대로 출력 -->
                <img src="/upload/${petInfo.pet_img}"	
                     alt="${petInfo.pet_name}"
                     class="pet-profile-img"/>

                <table class="pet-detail-table">
                    <tr>
                        <th>이름</th>
                        <td>${petInfo.pet_name}</td>
                    </tr>
                    <tr>
                        <th>나이</th>
                        <td>${petInfo.pet_age}세</td>
                    </tr>
                    <tr>
                        <th>생일</th>
                        <td>${petInfo.pet_birthday}</td>
                    </tr>
                    <tr>
                        <th>성별</th>
                        <td>${petInfo.pet_gender}</td>
                    </tr>
                    <tr>
                        <th>종</th>
                        <td>${petInfo.pet_species}</td>
                    </tr>
                    <tr>
                        <th>품종</th>
                        <td>${petInfo.pet_breed}</td>
                    </tr>
                    <tr>
                        <th>중성화 여부</th>
                        <td>
                            <c:choose>
                                <c:when test="${petInfo.pet_neutered eq 'Y'}">완료</c:when>
                                <c:otherwise>미완료</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>칩 등록 여부</th>
                        <td>
                            <c:choose>
                                <c:when test="${petInfo.pet_haschip eq 'Y'}">
                                    있음 
                                    <c:if test="${not empty petInfo.pet_chip_regdate}">
                                        (${petInfo.pet_chip_regdate})
                                    </c:if>
                                </c:when>
                                <c:otherwise>없음</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>등록일</th>
                        <td>${petInfo.pet_regdate}</td>
                    </tr>
                </table>
            </div>

            <div class="edit-pet-box">
                <button class="edit-pet-btn"
                    onclick="location.href='/mypage_petinfo_edit?pet_no=${petInfo.pet_no}'">
                    펫 정보 수정
                </button>
            </div>

        </section>
    </main>

    <!-- 공통 FOOTER -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
