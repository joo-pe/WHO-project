//새로운 비번
$('#newPw').on('propertychange change keyup paste input', function() {
	//비밀번호 공백 확인
	if($("#newPw").val() === ""){
		$('#newPwMsg').html('<b>비밀번호는 필수 정보입니다.</b>');
		chk2 = false;
			         
	} else {
		$('#newPwMsg').html('');
		chk2 = true;
	}
			
}); //end of new password
			
			
//비밀번호 확인
$('#newPwCheck').on('propertychange change keyup paste input', function() {
	
	if($("#newPwCheck").val() === "") {
		$('#newPwMsg').html('<b">비밀번호 확인은 필수 정보입니다.</b>');
		chk3 = false;
	} else if( $("#newPw").val() != $("#newPwCheck").val() ) {
		$('#newPwMsg').html('<b>비밀번호가 일치하지 않습니다.</b>');
		chk3 = false;
	} else {
		$('#newPwMsg').html('');
		chk3 = true;
}
	
			});//end of passwordCheck
			
$("#checkPassword").click(function () {
	
	let currentpw = $("#currentPw").val();
	let email = $("#email").val();
	
	$.ajax({
		type: "POST",
		url: "http://localhost:8090/check/Pw",
		data: { "currentpw": currentpw,
				"email": email
		},
		datatype: "json",
		success: function() {
			
		let email = $("#email").val();
        let newpw = $("#newPw").val();
                        
            $.ajax({
	            type: "POST",
	            url: "/check/Pw/changePw",
	            data: {
	                "email": email,
	                "newpw": newpw
	            },
				success: function () {
					swal("변경 완료!", "입력하신 password로 로그인해주시기 바랍니다", "success").then(function(OK) {
						if(OK){
							window.location = "/login";
							}
						else {
							alert('입력정보를 다시 확인하세요.');
							}
					})
				}
            })
		},
	})
});   
