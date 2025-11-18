document.addEventListener('DOMContentLoaded', function() {
    // 1. UI 요소 가져오기
    const toggleBtn = document.getElementById('chat-toggle-btn');
    const menuPopup = document.getElementById('chat-menu-popup');
    const chatPopup = document.getElementById('chat-popup-container');
    const iconOpen = document.getElementById('chat-icon-open-img');
    const iconClose = document.getElementById('chat-icon-close');
    const msgArea = document.getElementById('message-area');
    const userInput = document.getElementById('user-input');
    const sendBtn = document.getElementById('send-btn');

    let currentOpenPopup = null; // 현재 열린 팝업 추적 ('menu' 또는 'chat')

    // ----------------------------------------------------
    // [UI 상호작용 및 토글 로직]
    // ----------------------------------------------------
    
    // 메인 토글 버튼 클릭 시
    toggleBtn.addEventListener('click', function() {
        if (currentOpenPopup === 'chat') { // 채팅창 열려있으면 닫기
            closePopup('chat');
        } else if (currentOpenPopup === 'menu') { // 메뉴 열려있으면 닫기
            closePopup('menu');
        } else { // 둘 다 닫혀있으면 메뉴 열기
            openPopup('menu');
        }
    });

    // 팝업 열기 헬퍼 함수
    function openPopup(type) {
        if (type === 'menu') {
            menuPopup.classList.remove('hidden');
            currentOpenPopup = 'menu';
        } else if (type === 'chat') {
            chatPopup.classList.remove('hidden');
            currentOpenPopup = 'chat';
            if (msgArea.children.length === 0) {
                startWorkflow(); // 채팅창 열릴 때 초기 메시지 시작
            }
        }
        updateToggleIcon(true);
    }
    
    // 팝업 닫기 헬퍼 함수
    function closePopup(type) {
        if (type === 'menu') {
            menuPopup.classList.add('hidden');
        } else if (type === 'chat') {
            chatPopup.classList.add('hidden');
            // 채팅 팝업 닫힐 때 입력 및 메시지 초기화
            userInput.value = '';
        }
        currentOpenPopup = null;
        updateToggleIcon(false);
    }
    
    // 아이콘 모양 업데이트
    function updateToggleIcon(isOpen) {
        iconOpen.style.display = isOpen ? 'none' : 'block';
        iconClose.style.display = isOpen ? 'block' : 'none';
        
        // ⭐ SVG 아이콘 교체 로직 (필요 시) ⭐
        // 만약 <i class="fas"> 대신 <img src="chatbot.svg">를 사용한다면
        // 이 부분에서 이미지 src를 토글하는 로직으로 대체해야 합니다.
    }

    // 메뉴 항목 클릭 시 채팅 팝업 열기
    window.openChatPopup = function() {
        menuPopup.classList.add('hidden'); // 메뉴 팝업 닫기
        openPopup('chat'); // 채팅 팝업 열기
    };
    
    // 전송 버튼 클릭 및 엔터 키 입력 처리
    sendBtn.addEventListener('click', handleUserSend);
    userInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault(); // 기본 폼 제출 방지
            handleUserSend();
        }
    });

    // ----------------------------------------------------
    // [워크플로우 및 챗봇 로직]
    // ----------------------------------------------------

    // 메시지 출력 헬퍼 함수
	function displayMessage(text, isBot) {
	    const msgArea = document.getElementById('message-area');
	    const msgDiv = document.createElement('div');
	    msgDiv.classList.add(isBot ? 'bot-message' : 'user-message');

	    if (isBot) {
	        // 1. 마크다운 링크 처리 ([예약](url))
	        text = text.replace(
	            /\[([^\]]+)\]\(http:\/\/localhost:8686\/reservation\)/g,
	            '<a href="http://localhost:8686/reservation" target="_blank" style="color:#007bff;font-weight:bold;text-decoration:underline;">$1</a>'
	        );

	        // 2. 괄호 안에 있는 링크 처리 <http://...>
	        text = text.replace(
	            /<http:\/\/localhost:8686\/reservation>/g,
	            '<a href="http://localhost:8686/reservation" target="_blank" style="color:#007bff;font-weight:bold;text-decoration:underline;">예약하기 페이지</a>'
	        );

	        // 3. 그냥 텍스트로 나온 링크 처리
	        text = text.replace(
	            /http:\/\/localhost:8686\/reservation/g,
	            '<a href="http://localhost:8686/reservation" target="_blank" style="color:#007bff;font-weight:bold;text-decoration:underline;">예약하기 페이지</a>'
	        );

	        // 4. 링크는 없는데 "예약"이라는 단어가 들어간 문장은 자동으로 링크 추가
//	        if (text.includes('예약') && !text.includes('reservation')) {
//	            text = text.replace(
//	                /예약/g,
//	                '<a href="http://localhost:8686/reservation" target="_blank" style="color:#007bff;font-weight:bold;text-decoration:underline;">예약</a>'
//	            );
//	        }
	    }

	    msgDiv.innerHTML = text;
	    msgArea.appendChild(msgDiv);
	    msgArea.scrollTop = msgArea.scrollHeight;
	}

    // 챗봇 시작 및 초기 FAQ 버튼 출력
    function startWorkflow() {
        displayMessage("방문해 주셔서 감사합니다 :) 무엇을 도와드릴까요?", true);
        
        // 초기 FAQ 버튼 그룹 (HTML 코드를 직접 삽입)
        const buttonsHtml = `
            <div class="faq-options" style="display:flex; flex-direction:column; gap:8px; margin-top:10px;">
                <button onclick="handleButtonQuery('예약_문의')" style="padding:10px; border-radius:5px; border:1px solid #ddd; background:white; cursor:pointer;">🩺 예약 문의</button>
                <button onclick="handleButtonQuery('운영_시간')" style="padding:10px; border-radius:5px; border:1px solid #ddd; background:white; cursor:pointer;">⏰ 운영 시간</button>
                <button onclick="handleButtonQuery('상담_연결')" style="padding:10px; border-radius:5px; border:1px solid #ddd; background:white; cursor:pointer;">🧑‍💻 상담 연결</button>
            </div>
        `;
        // 응답 딜레이를 주어 봇처럼 보이게 함
        setTimeout(() => displayMessage(buttonsHtml, true), 500);
    }
    
    // 사용자 입력 전송 처리
    function handleUserSend() {
        const query = userInput.value.trim();
        if (!query) return;

        displayMessage(query, false); // 사용자 메시지 출력
        handleUserQuery(query);       // 쿼리 처리
        userInput.value = '';         // 입력창 비우기
    }
    
    // 버튼 클릭 처리 함수 (버튼 텍스트를 메시지처럼 처리하여 로직 호출)
    window.handleButtonQuery = function(key) {
        const queryText = key.replace('_', ' ');
        displayMessage(queryText, false);
        handleUserQuery(queryText);
    }

    // ⭐ 핵심 워크플로우 로직 (FAQ 답변 분기) ⭐
	function handleUserQuery(query) {
	    fetch('/api/chat/ask', {
	        method: 'POST',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify({ query: query })
	    })
	    .then(res => res.json())
	    .then(data => {
	        setTimeout(() => displayMessage(data.response, true), 700);
	    })
	    .catch(() => {
	        setTimeout(() => displayMessage("일시적인 장애가 발생했어요. 잠시 후 다시 시도해주세요.", true), 700);
	    });
	}
    
    // 초기 상태 설정: 모든 팝업 숨김 (DOMContentLoaded 끝)
});