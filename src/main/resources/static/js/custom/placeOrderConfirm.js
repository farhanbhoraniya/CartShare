$(document).ready(function(){
	
	$.fn.placeOrder = function(){
		$("#pickUpOrderModal").modal('show');
	} 
	
	$("#pickOrder").on("click",function(){
		alert("Clicked");	
	});
});