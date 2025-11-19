<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            <a href="/mypage_membership">멤버십</a>
            <a href="/mypage_medical">진료 내역</a>
            <a href="/mypage_grooming">미용 내역</a>
        </aside>

        <!-- 펫 상세 내용 -->
        <section class="content">
            <h2>펫 상세정보</h2>
			<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
			
			<%-- ⭐️ 3. 체중 관리 섹션 (CSS class 적용) ⭐️ --%>
			<div class="pet-weight-section">
			    <h3>체중 관리</h3>
			    
			    <div class="weight-comparison">
			        <p>
			            현재 체중: 
			            <strong>
			                <fmt:formatNumber value="${petInfo.current_weight}" pattern="0.0"/> kg
			            </strong>
			        </p>
			        <p>
			            권장 체중: 
			            <strong>
			                <fmt:formatNumber value="${petInfo.recommended_weight}" pattern="0.0"/> kg
			            </strong>
			        </p>
			        <c:if test="${petInfo.current_weight > 0 && petInfo.current_weight > petInfo.recommended_weight}">
			            <%-- ⭐️ 경고 메시지에 class="warning-text" 추가 ⭐️ --%>
			            <p class="warning-text">
			                (권장 체중보다 <fmt:formatNumber value="${petInfo.current_weight - petInfo.recommended_weight}" pattern="0.0"/> kg 초과)
			            </p>
			        </c:if>
			    </div>

			    <form action="/mypage/pet/${petInfo.pet_no}/add_weight" method="post" class="weight-form">
			        <label>오늘 체중 기록:</label>
			        <input type="number" step="0.01" name="weight_kg" required placeholder="예: 5.25">
			        <button type="submit">기록 저장</button>
			    </form>

			    <div class="chart-container">
			        <canvas id="weightChart"></canvas>
			    </div>
			</div>
			<%-- ⭐️ 3. (여기까지) 체중 관리 섹션 ⭐️ --%>
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
	<script>
	        // 이 페이지가 로드되면 즉시 실행
	        document.addEventListener("DOMContentLoaded", function() {
	            
	            // PetController의 @ResponseBody URL을 호출합니다.
	            fetch('/api/pet/${petInfo.pet_no}/weight_history')
	                .then(response => response.json())
	                .then(data => {
	                    
	                    // 1. DB에서 받은 데이터를 Chart.js 형식으로 가공
	                    const labels = data.map(item => 
	                        new Date(item.record_date).toLocaleDateString() // X축 (날짜)
	                    );
	                    const weights = data.map(item => 
	                        item.weight_kg // Y축 (체중)
	                    );

	                    // 2. 권장 체중 선 (DB의 recommended_weight 값 사용)
	                    const recommendedLine = new Array(labels.length).fill(${petInfo.recommended_weight});

	                    // 3. 차트 그리기
	                    const ctx = document.getElementById('weightChart').getContext('2d');
	                    new Chart(ctx, {
	                        type: 'line', // 라인 그래프
	                        data: {
	                            labels: labels,
	                            datasets: [
	                                {
	                                    label: '${petInfo.pet_name}의 체중 (kg)',
	                                    data: weights,
	                                    borderColor: 'blue',
	                                    fill: false,
	                                    tension: 0.1
	                                },
	                                {
	                                    label: '권장 체중 (kg)',
	                                    data: recommendedLine,
	                                    borderColor: 'red',
	                                    borderDash: [5, 5], // 점선
	                                    fill: false,
	                                    pointRadius: 0 // 점 숨기기
	                                }
	                            ]
	                        }
	                    });
	                })
	                .catch(error => console.error('그래프 데이터 로딩 실패:', error));
	        });
	    </script>
</body>
</html>
