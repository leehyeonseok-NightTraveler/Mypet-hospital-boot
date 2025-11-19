/**
 * 예약 관리 및 회원 관리 페이지에서 사용되는 JavaScript 함수 모음
 * * openCertificateModal 함수는 새 브라우저 팝업(window.open)을 사용합니다.
 */

// 예약취소 모달 열기
function openCancelModal(resNo, type) {
    // 1. 예약 번호 설정: Hidden input with id="cancelResNo"
    document.getElementById('cancelResNo').value = resNo;

    // 2. 예약 유형 설정: Hidden input with id="cancelResType"
    document.getElementById('cancelResType').value = type;

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

// 회원 활동정지 모달 열기 (user_detail.jsp)
function openSuspendModal(userNo, targetStatus, pageNum, amount) {
    // 폼 필드에 값 설정
    document.getElementById('suspendUserNo').value = userNo;
    // targetStatus는 'INACTIVE'로 폼에 고정되어 있음

    // 페이징 정보 추가
    document.getElementById('suspendPageNum').value = pageNum;
    document.getElementById('suspendAmount').value = amount;

    document.getElementById('suspension_reason').value = ''; // 사유 초기화

    // 모달 및 오버레이 표시
    document.getElementById('suspendModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 회원 활동정지 모달 닫기 (user_detail.jsp)
function closeSuspendModal() {
    // 닫을 때 값 초기화
    document.getElementById('suspendUserNo').value = '';
    document.getElementById('suspension_reason').value = '';

    document.getElementById('suspendModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

// 회원 활동정지 해제 처리 (user_detail.jsp)
function confirmToggle(userNo, targetStatus, pageNum, amount) {
    // NOTE: 운영 환경에서는 window.confirm() 대신 커스텀 모달을 사용해야 합니다.
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

// 검색 폼 초기화
function resetSearchForm() {
    document.getElementById('keyword').value = '';
    document.getElementById('status').value = '';
}


// 🌟 [확인서 발급] 새 브라우저 팝업 열기 (user_detail.jsp) 🌟
function openCertificateModal(serviceNo, userNo, petNo) {
    // 1. URL 생성 (서비스 번호와 사용자 번호 포함)
    const url = `/Certificate?service_no=${serviceNo}&user_no=${userNo}&pet_no=${petNo}`;

    // 2. 새 브라우저 팝업 열기
    const popupName = 'certificatePopup';
    // 팝업 창의 크기(800x600)와 위치 설정
    const features = 'width=800,height=600,top=100,left=100,scrollbars=yes,resizable=yes';

    const newWindow = window.open(url, popupName, features);

    // 팝업 차단 시 대비 (선택 사항)
    if (newWindow === null || typeof newWindow === 'undefined' || newWindow.closed) {
        // 콘솔에 오류 기록
        console.error("팝업이 차단되었습니다. 브라우저 설정을 확인해 주세요.");
    }
}
function generatePDF() {
    // A4 문서 크기 (mm)
    const A4_WIDTH_MM = 210;
    const A4_HEIGHT_MM = 297;
    // 여백을 0으로 설정하여 A4 용지 전체를 사용하도록 변경
    const PAGE_PADDING_MM = 0;

    // 캡처할 요소는 A4 용지 규격이 적용된 컨테이너
    const element = document.querySelector('.certificate-container');

    // 1. 버튼 숨김 (PDF에 포함되지 않도록)
    const printButton = document.querySelector('.print-button');
    if (printButton) {
        // [수정] display: none 대신 visibility: hidden으로 변경하여 공간은 유지하고 시각적으로만 숨깁니다.
        printButton.style.visibility = 'hidden';
    }

    // 2. html2canvas 옵션 조정: scale과 여백을 명확히 정의
    html2canvas(element, {
        scale: 4, // [수정] scale 값을 4로 높여 고화질 캡처를 유도합니다.
        useCORS: true,
        // Window 사이즈가 아닌, element의 실제 크기만 캡처하도록
        width: element.offsetWidth,
        height: element.offsetHeight
    }).then(canvas => {
        // PNG보다 용량이 작은 JPEG로 변경 (화질 1.0 유지)
        // JPEG 포맷을 유지하여 파일 크기를 합리적으로 관리합니다.
        const imgData = canvas.toDataURL('image/jpeg', 1.0);

        // jsPDF 객체 생성 (A4 크기)
        const pdf = new window.jspdf.jsPDF('p', 'mm', 'a4');

        // 이미지의 실제 크기 계산 (HTML에서 렌더링된 비율 그대로)
        const imgWidth = A4_WIDTH_MM; // PDF A4 너비에 맞춤
        const imgHeight = canvas.height * imgWidth / canvas.width; // A4 너비 기준으로 높이 재계산

        // heightLeft, position 변수는 다중 페이지 로직에서 사용되므로 제거하거나 사용하지 않습니다.

        // 3. 내용을 A4 한 페이지 안에 강제로 배치 (여백 0mm 적용)
        // x, y를 0으로 설정하고, w를 A4 너비 전체로 설정하여 여백을 없앱니다.
        const x = 0; // 0mm 시작 여백 (X축)
        const y = 0; // 0mm 시작 여백 (Y축)
        const w = A4_WIDTH_MM; // 실제 내용이 들어갈 너비 (A4 너비 전체)
        const h = imgHeight * w / imgWidth; // 재계산된 높이 (내용이 길면 A4 높이를 초과할 수 있음)

        pdf.addImage(imgData, 'JPEG', x, y, w, h);
        // 다중 페이지를 처리하던 while 루프는 완전히 제거되었습니다.

        pdf.save("확인서.pdf");

    }).finally(() => {
        // 4. 버튼 다시 표시 (오류 발생 여부와 관계없이)
        if (printButton) {
            // [수정] visibility: hidden으로 숨긴 것을 visibility: visible로 다시 표시합니다.
            printButton.style.visibility = 'visible';
        }
    });
}