<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pet.pet_name} 체중 관리</title>
    
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <link rel="stylesheet" href="/css/mainpage.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<main>
    <h2>${pet.pet_name} 님의 체중 관리</h2>

    <div>
        <p>
            현재 체중: <strong><fmt:formatNumber value="${pet.current_weight}" pattern="0.0"/> kg</strong>
        </p>
        <p>
            권장 체중: <strong><fmt:formatNumber value="${pet.recommended_weight}" pattern="0.0"/> kg</strong>
        </p>
        
        <c:if test="${pet.current_weight > 0 && pet.current_weight > pet.recommended_weight}">
            <p style="color: red; font-weight: bold;">
                권장 체중보다 
                <fmt:formatNumber value="${pet.current_weight - pet.recommended_weight}" pattern="0.0"/> kg 
                초과입니다! 식단 관리가 필요합니다.
            </p>
        </c:if>
    </div>
    <hr>

    <form action="/mypage/pet/${pet.pet_no}/add_weight" method="post">
        <label>오늘 체중 입력:</label>
        <input type="number" step="0.01" name="weight_kg" required placeholder="예: 5.25">
        <button type="submit">기록 저장</button>
    </form>
    <hr>
    
    
    <div style="width: 80%; max-width: 800px; margin: auto;">
        <canvas id="weightChart"></canvas>
    </div>

    <script>
        // 이 페이지가 로드되면 즉시 실행
        document.addEventListener("DOMContentLoaded", function() {
            
            // 컨트롤러의 @ResponseBody URL을 호출합니다.
            fetch('/api/pet/${pet.pet_no}/weight_history')
                .then(response => response.json())
                .then(data => {
                    
                    // 1. DB에서 받은 데이터를 Chart.js 형식으로 가공
                    const labels = data.map(item => 
                        new Date(item.record_date).toLocaleDateString() // 날짜
                    );
                    const weights = data.map(item => 
                        item.weight_kg // 체중
                    );

                    // 2. 권장 체중 선 (데이터가 없는 것처럼 점선으로 표시)
                    const recommendedLine = new Array(labels.length).fill(${pet.recommended_weight});

                    // 3. 차트 그리기
                    const ctx = document.getElementById('weightChart').getContext('2d');
                    new Chart(ctx, {
                        type: 'line', // 라인 그래프
                        data: {
                            labels: labels,
                            datasets: [
                                {
                                    label: '${pet.pet_name}의 체중 (kg)',
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
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>