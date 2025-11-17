// 예약취소 모달 열기
function openCancelModal(resNo, type) {
    // 1. 예약 번호 설정: Hidden input with id="cancelResNo"
    document.getElementById('cancelResNo').value = resNo;

    // 2. 예약 유형 설정: Hidden input with id="cancelResType"
    // 인자로 받은 type 값을 모달 폼의 input에 설정합니다.
    document.getElementById('cancelResType').value = type;

    // 3. 모달 및 오버레이 표시
    document.getElementById('cancelModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 예약취소 모달 닫기
function closeCancelModal() {
    // 닫을 때 값 초기화 (선택 사항)
    document.getElementById('cancelResNo').value = '';
    document.getElementById('cancelResType').value = ''; // type도 초기화
    document.getElementById('cancel_reason').value = ''; // 사유도 초기화

    document.getElementById('cancelModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

// 진료내용 모달 열기
function openDetailModal(resNo) {
    const row = document.getElementById(`detailModal-${resNo}`);
    if (row) row.style.display = 'table-row';
}

// 진료내용 모달 닫기
function closeDetailModal(resNo) {
    const row = document.getElementById(`detailModal-${resNo}`);
    if (row) row.style.display = 'none';
}

// 추가사항 모달 열기
function openMemoModal(resNo) {
    const row = document.getElementById(`memoModal-${resNo}`);
    if (row) row.style.display = 'table-row';
}

// 추가사항 모달 닫기
function closeMemoModal(resNo) {
    const row = document.getElementById(`memoModal-${resNo}`);
    if (row) row.style.display = 'none';
}