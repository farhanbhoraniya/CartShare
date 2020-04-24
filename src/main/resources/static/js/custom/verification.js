$.fn.verificationPage = function(){ 
	//send ajax call to get verification code
	//store verification code in session
	sessionStorage.code = "1234";
	window.location.href = "/verification"; 
}
$(document).ready(function(){
	
	$("#verify").on("click",function(){
		var code = $("#code").val();
		if(code == sessionStorage.code){
			//then do the next page
			window.location.href="/createStore";
		}else{
			//error in code
		}
	});
});