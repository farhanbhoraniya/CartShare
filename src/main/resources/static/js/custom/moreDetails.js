$(document).ready(function(){
	
	var name = sessionStorage.name;
	var email = sessionStorage.email;
	
	if(name !== ""){
		$("#name").val(name);
		$( "#name" ).prop( "disabled", true );
	}
	
	if(email !== ""){
		$("#email").val(email);
		$( "#email" ).prop( "disabled", true );
	}
	
	$("#createAcc").on("click",function(){
		window.location.href="/verification";
	});
});