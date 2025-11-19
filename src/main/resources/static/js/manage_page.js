/**
 * 예약 관리 및 회원 관리 페이지에서 사용되는 JavaScript 함수 모음
 * * openCertificateModal 함수는 회원 상세 페이지의 모달을 사용합니다.
 */

// --- [예약/진료/메모 모달] ---

// 예약취소 모달 열기
function openCancelModal(resNo, type) {
    document.getElementById('cancelResNo').value = resNo;
    document.getElementById('cancelResType').value = type;
    document.getElementById('cancelModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 예약취소 모달 닫기
function closeCancelModal() {
    document.getElementById('cancelResNo').value = '';
    document.getElementById('cancelResType').value = '';
    document.getElementById('cancel_reason').value = '';
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

// --- [회원 관리 모달/기능] ---

// 회원 활동정지 모달 열기 (user_detail.jsp)
function openSuspendModal(userNo, targetStatus, pageNum, amount) {
    document.getElementById('suspendUserNo').value = userNo;
    document.getElementById('suspendPageNum').value = pageNum;
    document.getElementById('suspendAmount').value = amount;
    document.getElementById('suspension_reason').value = '';
    document.getElementById('suspendModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

// 회원 활동정지 모달 닫기 (user_detail.jsp)
function closeSuspendModal() {
    document.getElementById('suspendUserNo').value = '';
    document.getElementById('suspension_reason').value = '';
    document.getElementById('suspendModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

// 회원 활동정지 해제 처리 (user_detail.jsp)
function confirmToggle(userNo, targetStatus, pageNum, amount) {
    if (confirm("정말로 활동정지를 해제하시겠습니까?")) {
        document.getElementById('activateUserNo').value = userNo;
        document.getElementById('activatePageNum').value = pageNum;
        document.getElementById('activateAmount').value = amount;
        document.getElementById('activateForm').submit();
    }
}

// 검색 폼 초기화
function resetSearchForm() {
    document.getElementById('keyword').value = '';
    document.getElementById('status').value = '';
}

// --- [확인서 발급 모달/기능] ---

/**
 * 확인서 발급 모달 열기 및 데이터 설정
 */
function openCertificateModal(serviceNo, userNo, petNo) {
    document.getElementById('certServiceNo').value = serviceNo;
    document.getElementById('certUserNo').value = userNo;
    document.getElementById('certPetNo').value = petNo;

    document.getElementById('certErrorMessage').style.display = 'none';
    document.getElementById('id_number').value = '';

    document.getElementById('certificateModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';

    document.getElementById('id_number').focus();
}

/**
 * 확인서 발급 모달 닫기
 */
function closeCertificateModal() {
    document.getElementById('certificateModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

/*
* 주민등록번호 확인 및 폼 제출 (새 브라우저 창(팝업)으로)
*/
function submitCertificateForm() {
    const idNumberInput = document.getElementById('id_number');
    const idNumber = idNumberInput.value.trim();
    const errorMessage = document.getElementById('certErrorMessage');
    const form = document.getElementById('certificateForm');

    // 폼에서 hidden 필드의 값들을 가져옵니다.
    const serviceNo = document.getElementById('certServiceNo').value;
    const userNo = document.getElementById('certUserNo').value;
    const petNo = document.getElementById('certPetNo').value;

    // 주민등록번호 형식 검증 (총 13자리 숫자)
    if (idNumber.length === 13 && /^\d+$/.test(idNumber)) {
        errorMessage.style.display = 'none';

        // 1. Controller URL (user_detail.jsp에 설정된 action 사용)
        const baseUrl = form.action;

        // 2. 쿼리 파라미터 조합 (URLSearchParams를 사용하여 안전하게 인코딩)
        const params = new URLSearchParams();
        params.append('service_no', serviceNo);
        params.append('user_no', userNo);
        params.append('pet_no', petNo);
        params.append('id_number', idNumber); // 주민등록번호 포함

        const fullUrl = `${baseUrl}?${params.toString()}`;

        // 3. 💡 window.open()을 사용하여 새 브라우저 창을 띄웁니다.
        // options: width와 height를 지정하여 새 창 크기(800x900)를 설정하고, 새 창(팝업)으로 열리도록 합니다.
        const windowFeatures = 'width=800,height=900,resizable=yes,scrollbars=yes,status=no';

        // 두 번째 인수는 창 이름입니다 (CertificatePopup).
        window.open(fullUrl, 'CertificatePopup', windowFeatures);

        // 4. 현재 페이지의 모달을 닫습니다.
        closeCertificateModal();

    } else {
        errorMessage.textContent = '주민등록번호 13자리를 숫자만 정확히 입력해주세요.';
        errorMessage.style.display = 'block';
        idNumberInput.focus();
    }
}

// --- [PDF 생성 기능] ---

function generatePDF() {
    const A4_WIDTH_MM = 210;
    const A4_HEIGHT_MM = 297;

    const element = document.querySelector('.certificate-container');
    const filename = "확인서.pdf";

    // 1. 버튼 숨김 (PDF에 포함되지 않도록)
    const printButton = document.querySelector('.print-button');
    if (printButton) {
        printButton.style.visibility = 'hidden';
    }

    // 2. html2canvas 옵션 조정: 고화질 캡처
    html2canvas(element, {
        scale: 4,
        useCORS: true,
        width: element.offsetWidth,
        height: element.offsetHeight
    }).then(canvas => {
        const imgData = canvas.toDataURL('image/jpeg', 1.0);

        // jsPDF 객체 생성 (A4 크기)
        const pdf = new window.jspdf.jsPDF('p', 'mm', 'a4');

        // 이미지 크기 계산 (PDF A4 너비에 맞춤)
        const imgWidth = A4_WIDTH_MM;
        const imgHeight = canvas.height * imgWidth / canvas.width;

        const x = 0;
        const y = 0;
        const w = A4_WIDTH_MM;
        const h = imgHeight * w / imgWidth;

        pdf.addImage(imgData, 'JPEG', x, y, w, h);

        pdf.save(filename);

    }).finally(() => {
        // 3. 버튼 다시 표시
        if (printButton) {
            printButton.style.visibility = 'visible';
        }
    });
}