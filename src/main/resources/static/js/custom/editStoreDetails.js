$(document).ready(function(){
	
	var details = sessionStorage.editDetails;
	var json = JSON.parse(details);
	
	$("#no").val(json.storeID);
	$("#name").val(json.storeName);
	$("#stNum").val(json.streetName);
	$("#city").val(json.city);
	$("#state").val(json.state);
	$("#zip").val(json.zipcode);
	
	$("#updateStore").on("click",function(){
		$("#no").val();
		$("#name").val();
		$("#stNum").val();
		$("#city").val();
		$("#state").val();
		$("#zip").val();
	});
});