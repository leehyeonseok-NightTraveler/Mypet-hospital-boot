/**
 * 예약 관리 및 회원 관리 페이지에서 사용되는 JavaScript 함수 모음
 */

// 예약취소 모달 열기
function openCancelModal(resNo, type) {
    // 1. 예약 번호 설정: Hidden input with id="cancelResNo"
    document.getElementById('cancelResNo').value = resNo;

    // 2. 예약 유형 설정: Hidden input with id="cancelResType"
    // 인자로 받은 type 값을 모달 폼의 input에 설정합니다.
    document.getElementById('cancelResType').value = type;

    // 💡 페이징 정보는 JSP에서 모달 폼의 hidden input에 초기값으로 설정되어 있다고 가정합니다.
    // 만약 동적으로 설정해야 한다면 이 위치에 추가해야 합니다.

    // 3. 모달 및 오버레이 표시
    document.getElementById('cancelModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 예약취소 모달 닫기
function closeCancelModal() {
    // 닫을 때 값 초기화
    document.getElementById('cancelResNo').value = '';
    document.getElementById('cancelResType').value = '';
    document.getElementById('cancel_reason').value = ''; // 사유도 초기화

    document.getElementById('cancelModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

// 진료내용 모달 열기 (테이블 행 토글 방식)
function openDetailModal(resNo) {
    const row = document.getElementById(`detailModal-${resNo}`);
    if (row) row.style.display = 'table-row';
}

// 진료내용 모달 닫기
function closeDetailModal(resNo) {
    const row = document.getElementById(`detailModal-${resNo}`);
    if (row) row.style.display = 'none';
}

// 추가사항 모달 열기 (테이블 행 토글 방식)
function openMemoModal(resNo) {
    const row = document.getElementById(`memoModal-${resNo}`);
    if (row) row.style.display = 'table-row';
}

// 추가사항 모달 닫기
function closeMemoModal(resNo) {
    const row = document.getElementById(`memoModal-${resNo}`);
    if (row) row.style.display = 'none';
}

// 🌟 JavaScript 함수: 활동정지 모달 열기 (user_detail.jsp) 🌟
function openSuspendModal(userNo, targetStatus, pageNum, amount) {
    // 폼 필드에 값 설정
    document.getElementById('suspendUserNo').value = userNo;
    // targetStatus는 모달 폼에 INACTIVE로 고정되어 있음

    // 💡 누락된 페이징 정보 추가 💡
    document.getElementById('suspendPageNum').value = pageNum;
    document.getElementById('suspendAmount').value = amount;

    document.getElementById('suspension_reason').value = ''; // 사유 초기화

    // 모달 및 오버레이 표시
    document.getElementById('suspendModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 🌟 JavaScript 함수: 활동정지 모달 닫기 (user_detail.jsp) 🌟
function closeSuspendModal() {
    // 닫을 때 값 초기화 (선택 사항)
    document.getElementById('suspendUserNo').value = '';
    document.getElementById('suspension_reason').value = '';

    document.getElementById('suspendModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

// 🌟 JavaScript 함수: 활동정지 해제 처리 (user_detail.jsp) 🌟
function confirmToggle(userNo, targetStatus, pageNum, amount) {
    if (confirm("정말로 활동정지를 해제하시겠습니까?")) {
        // 숨겨진 activateForm에 값 설정 후 POST 전송
        document.getElementById('activateUserNo').value = userNo;
        // targetStatus는 'ACTIVE'로 폼에 고정되어 있음
        document.getElementById('activatePageNum').value = pageNum;
        document.getElementById('activateAmount').value = amount;

        // POST 폼 전송
        document.getElementById('activateForm').submit();
    }
}

function resetSearchForm() {
    document.getElementById('keyword').value = '';
    document.getElementById('status').value = '';
}


