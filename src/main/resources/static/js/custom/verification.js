$(document).ready(function(e){
	
	
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