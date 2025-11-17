$(document).ready(function() {
    const actionForm = $("form");

    $(".submit-btnp").on("click", function(e) {
        e.preventDefault();

        const id = $("input[name='account_id']").val().trim();
        const ph = $("input[name='account_phone']").val().trim();
        const em = $("input[name='account_email']").val().trim();

        if (id === "") {
            alert("아이디를 입력하세요.");
            $("input[name='account_id']").focus();
            return;
        }

        if (ph === "") {
            alert("전화번호를 입력하세요.");
            $("input[name='account_phone']").focus();
            return;
        }
		
        if (em === "") {
            alert("이메일을 입력하세요.");
            $("input[name='account_email']").focus();
            return;
        }
		
        actionForm.attr("action", "findPwYn").submit();
    });
});

$(document).ready(function() {
    const actionForm = $("form");

    $(".submit-btn").on("click", function(e) {
        e.preventDefault();

        const ph = $("input[name='account_phone']").val().trim();
        const em = $("input[name='account_email']").val().trim();

        if (em === "") {
            alert("이메일을 입력하세요.");
            $("input[name='account_email']").focus();
            return;
        }
        if (ph === "") {
            alert("전화번호를 입력하세요.");
            $("input[name='account_phone']").focus();
            return;
        }
		
		
        actionForm.attr("action", "findAccountOK").submit();
    });
});

