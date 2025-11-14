let timer;
let lastResults = [];
let controller = null; // 🔥 요청 취소용

function searchKeyword() {
  const v = document.getElementById('query').value;
  const box = document.getElementById('box');
  clearTimeout(timer);

  if (!v) { 
    lastResults = [];
    box.innerHTML = '';
    return; 
  }

  timer = setTimeout(() => {

    // 🔥 이전 요청 취소 (중요)
    if (controller) controller.abort();
    controller = new AbortController();

    fetch('/api/autocomplete?query=' + encodeURIComponent(v), {
      signal: controller.signal,
      cache: "no-store"
    })
      .then(r => r.json())
      .then(arr => {

        // 🔒 입력값이 바뀌었으면 이전 응답은 무시
        const currentValue = document.getElementById('query').value;
        if (currentValue !== v) return;

        lastResults = arr;

        if (arr.length === 0) {
          box.innerHTML = '<div style="padding:8px;color:#888;">검색 결과 없음</div>';
        } else {
          box.innerHTML = arr.map(o => {
            const link = o.siteurl
              ? (o.siteurl.startsWith('http') ? o.siteurl : '/' + o.siteurl)
              : '#';
            return `<div class="item" onclick="window.location.href='${link}'">${o.keyword}</div>`;
          }).join('');
        }
      })
      .catch(err => {
        if (err.name === "AbortError") return; // 취소는 정상 흐름
        console.error('fetch 오류:', err);
      });

  }, 250); // 반응 속도 조금 증가
}

// 버튼 + 엔터 이벤트
document.addEventListener("DOMContentLoaded", function() {

  // 🔥 버튼 클릭
  document.getElementById('sendBtn').addEventListener('click', () => {
    if (!lastResults || lastResults.length === 0) {
      alert('자동완성된 결과가 없습니다.');
      return;
    }

    document.getElementById('siteurl').value = JSON.stringify(lastResults.map(o => o.siteurl || ''));
    document.getElementById('keyword').value = JSON.stringify(lastResults.map(o => o.keyword || ''));
    document.getElementById('searchForm').submit();
  });

  // 🔥 엔터로 검색 실행 (입력창에서 감지해야 함)
  document.getElementById('query').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      e.preventDefault(); // 기본 엔터 제출 막기
      document.getElementById('sendBtn').click(); // 버튼 클릭과 동일하게 실행
    }
  });

});
