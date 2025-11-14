<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>추가 정보 입력 - MY PET 동물병원</title>

    <link rel="stylesheet" href="/css/mainpage.css">
    <link rel="stylesheet" href="/css/login.css">
    <link rel="stylesheet" href="/css/register_social.css">
    
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>

<header>
	<div class="inner">
	        <h1>
	            <a href="/mainpage">
	                <img src="/img/mypet.png">MY PET 동물병원
	            </a>
	        </h1>

	        <ul id="gnb">
	            <li><a href="/hospital_info">병원소개</a></li>
	            <li class="dropdown-parent"><a href="#">게시판</a>
	                <ul class="submenu">
	                    <li><a href="/notices_list">공지사항</a></li>
	                    <li><a href="#">자유게시판</a></li>
	                    <li><a href="/qna_page">Q&A</a></li>
	                </ul>
	            </li>

	            <c:choose>
	                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
	                    <li><a href="/reservation">예약</a></li>
	                </c:when>
	                <c:otherwise>
	                    <li><a href="/login">예약</a></li>
	                </c:otherwise>
	            </c:choose>
	        </ul>

	        <ul class="util">
	            <c:choose>
	                <c:when test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'USER'}">
	                    <li><a href="/mypage_userinfo">마이페이지</a></li>
	                    <li><a href="/logout">로그아웃</a></li>
	                </c:when>
	                <c:otherwise>
	                    <li><a href="/login">로그인</a></li>
	                    <li><a href="/register">회원가입</a></li>
	                </c:otherwise>
	            </c:choose>
	        </ul>
	    </div>
</header>

<main>
    <div class="login-container">
        <h2>추가 정보 입력 🐾</h2>
        <p>카카오 로그인에 성공했습니다! 원활한 서비스 이용을 위해 추가 정보를 입력해주세요.</p>

        <form action="<c:url value='/register_social_process'/>" method="post" id="socialForm">
            
            <input type="hidden" name="user_name" value="${userDTO.user_name}">
            <input type="hidden" name="user_email" value="${userDTO.user_email}">

            <div class="input-group">
                <input type="tel" name="user_phone" placeholder="휴대폰 번호 ('-' 없이 입력)" required>
            </div>
            
            <div class="input-group">
                <label for="birthday">생년월일</label>
                <input type="date" id="birthday" name="user_birthday">
            </div>

            <div class="input-group gender-group">
                <label>성별</label>
                <input type="radio" id="male" name="user_gender" value="M">
                <label for="male">남성</label>
                <input type="radio" id="female" name="user_gender" value="F">
                <label for="female">여성</label>
            </div>

            <div class="input-group address-group">
                <input type="text" id="postcode" name="user_addr" placeholder="우편번호" readonly>
                <button type="button" onclick="execDaumPostcode()" class="addr-btn">주소 검색</button>
            </div>
            <div class="input-group">
                <input type="text" id="address" name="user_addr_detail" placeholder="상세 주소">
            </div>

            <button type="submit" class="submit-btn">가입 완료</button>
        </form>
    </div>
</main>

<%-- (푸터 부분은 login.jsp와 동일하게 복사) --%>
<footer>
    ... (login.jsp의 footer 태그 내용) ...
</footer>

<script>
    // Daum Postcode API 스크립트
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('postcode').value = data.zonecode; // 우편번호
                document.getElementById("address").value = data.address; // 기본 주소
                
                // 상세주소 필드로 포커스 이동
                document.getElementById("address_detail").focus();
            }
        }).open();
    }
    
    // 폼 제출 시 주소 합치기 (선택 사항이지만 권장)
    // user_addr과 user_addr_detail을 합쳐서 user_addr로 보낼 경우
    // DTO의 user_addr 필드만 사용하면 됩니다.
    // 여기서는 DTO에 user_addr(우편번호), user_addr_detail(상세주소)이 있다고 가정하고
    // 컨트롤러에서 user_addr = 우편번호 + " " + 상세주소 로 합쳐야 합니다.
    
    // -> 지금은 DTO에 user_addr(우편번호), user_addr_detail(상세주소) 필드가 
    //    별도로 있다고 가정하고 그냥 제출합니다.
    //    만약 DTO에 user_addr 필드 하나만 있다면 아래 로직이 필요합니다.
    
    /*
    document.getElementById('socialForm').addEventListener('submit', function(e) {
        let postcode = document.getElementById('postcode').value;
        let detail = document.getElementById('address_detail').value;
        
        // user_addr 필드 하나에 합쳐서 넣기
        let fullAddress = "(" + postcode + ") " + detail;
        
        // hidden input을 만들어서 값을 설정
        let hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'user_addr';
        hiddenInput.value = fullAddress;
        this.appendChild(hiddenInput);
    });
    */
</script>

</body>
</html>