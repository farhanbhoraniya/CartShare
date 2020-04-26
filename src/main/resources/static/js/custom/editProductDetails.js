$(document).ready(function(){
	
	var details = sessionStorage.editProductDetails;
	var json = JSON.parse(details);
	
	$("#skn").val(json.skn);
	$("#name").val(json.name);
	$("#desc").val(json.desc);
	$("#link").val(json.url);
	$("#brand").val(json.brand);
	$("#unit").val(json.unit);
	$("#price").val(json.price);
	$("#storeID").val(json.storeID);
	
	$("#updateStore").on("click",function(){
		$("#skn").val(json.storeID);
		$("#name").val(json.storeName);
		$("#desc").val(json.streetName);
		$("#link").val(json.city);
		$("#brand").val(json.state);
		$("#unit").val(json.zipcode);
		$("#price").val(json.zipcode);
		$("#storeID").val(json.zipcode);
	});
});