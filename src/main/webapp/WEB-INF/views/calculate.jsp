<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비만 계산기</title>
    <link rel="stylesheet" href="/css/calculator.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="/css/mainpage.css">
    <style>
        /* 결과 영역 스타일 (인라인 스타일 유지) */
        .result-display-area {
            padding: 20px;
            border: 1px solid #ffe799; 
            border-radius: 8px;
            background-color: #fff9e6; 
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<body>

	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	
    <div class="calculator-container">
        
        <form action="/calculate/result" method="POST" id="obesityForm"> 

            <h2 class="section-section-title">반려동물 종류</h2>
			<div class="selection-grid type-selection">
			    <div class="selection-item selected" data-type="DOG"> 
			        <img src="/img/bcs/DOG.png" alt="강아지 아이콘">
			        <p>강아지</p>
			    </div>
			    <div class="selection-item" data-type="CAT">
			        <img src="/img/bcs/CAT.png" alt="고양이 아이콘">
			        <p>고양이</p>
			    </div>
			</div>

            <hr class="separator">

            <h2 class="section-title">우리 아이와 닮은 사진을 골라주세요.</h2>
            <p class="description">가장 가까운 체형을 선택해주세요. (왼쪽: 마름, 오른쪽: 비만)</p>
            
            <div class="selection-grid bcs-selection">
                <div class="bcs-item selected" data-bcs="1">
                    <img id="bcs-img-1" src="/img/bcs/bcs1.png" alt="BCS 1">
                    <div class="overlay">BCS 1</div>
                </div>
                <div class="bcs-item" data-bcs="2">
                    <img id="bcs-img-2" src="/img/bcs/bcs2.png" alt="BCS 2">
                    <div class="overlay">BCS 2</div>
                </div>
                <div class="bcs-item" data-bcs="3">
                    <img id="bcs-img-3" src="/img/bcs/bcs3.png" alt="BCS 3">
                    <div class="overlay">BCS 3</div>
                </div>
                <div class="bcs-item" data-bcs="4">
                    <img id="bcs-img-4" src="/img/bcs/bcs4.png" alt="BCS 4">
                    <div class="overlay">BCS 4</div>
                </div>
                <div class="bcs-item" data-bcs="5">
                    <img id="bcs-img-5" src="/img/bcs/bcs5.png" alt="BCS 5">
                    <div class="overlay">BCS 5</div>
                </div>
            </div>
            
            <input type="hidden" name="animalType" id="animalType" value="DOG">
            <input type="hidden" name="bcsScore" id="bcsScore" value="1">

            <button type="submit" class="result-button">결과보기</button>
            
        </form>
        
        <c:if test="${not empty resultDiagnosis}">
            <hr class="separator">
            <div class="result-display-area">
                <h3 style="color: #007bff; font-weight: bold; margin-bottom: 10px;">🩺 진단 결과</h3>
                <p style="font-size: 1.1rem; line-height: 1.5;">
                    ${resultDiagnosis}
                </p>
                <p style="font-size: 0.85rem; color: #777; margin-top: 10px;">
                    (진단 기준: BCS ${resultBcs}점)
                </p>
            </div>
        </c:if>
        
    </div>

    <script>
        // 이미지 소스를 변경하는 핵심 함수
		function updateBcsImages(animalType) {
		    // DOG: 'bcs', CAT: 'bcs_cat'
		    const prefix = (animalType === 'DOG') ? 'bcs' : 'bcs_cat';
		    
		    // 이미지 파일 경로 업데이트
		    document.getElementById('bcs-img-1').src = `/img/bcs/`+prefix+`1.png`; 
		    document.getElementById('bcs-img-2').src = `/img/bcs/`+prefix+`2.png`;
		    document.getElementById('bcs-img-3').src = `/img/bcs/`+prefix+`3.png`;
		    document.getElementById('bcs-img-4').src = `/img/bcs/`+prefix+`4.png`;
		    document.getElementById('bcs-img-5').src = `/img/bcs/`+prefix+`5.png`;
		}

        document.addEventListener('DOMContentLoaded', function() {
            // 초기값 설정 및 강아지 이미지 로딩
            const initialType = 'DOG';
            document.getElementById('animalType').value = initialType;
            document.getElementById('bcsScore').value = '1';
            updateBcsImages(initialType); 

            // 1. 반려동물 종류 선택 로직 (이미지 변경 로직 포함)
            document.querySelectorAll('.selection-item').forEach(item => {
                item.addEventListener('click', function() {
                    document.querySelectorAll('.selection-item').forEach(i => i.classList.remove('selected'));
                    this.classList.add('selected');
                    
                    const newType = this.getAttribute('data-type');
                    document.getElementById('animalType').value = newType;
                    
                    updateBcsImages(newType);
                });
            });

            // 2. BCS 이미지 선택 로직
            document.querySelectorAll('.bcs-item').forEach(item => {
                item.addEventListener('click', function() {
                    document.querySelectorAll('.bcs-item').forEach(i => i.classList.remove('selected'));
                    this.classList.add('selected');

                    document.getElementById('bcsScore').value = this.getAttribute('data-bcs');
                });
            });
            
            // ✅ 스크롤 위치 조정 로직
            const resultArea = document.querySelector('.result-display-area');
            if (resultArea) {
                // 결과 영역이 렌더링 되었을 경우, 해당 위치로 스크롤 이동
                resultArea.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    </script>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>