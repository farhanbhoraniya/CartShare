$(document).ready(function(e){
	var unit = "";
	
	$("#unit").change(function(){
        unit = $(this).children("option:selected").text();
	});
	var v="";
	$("#storeID").change(function(){
        v = $(this).children("option:selected").val();
	});
	
	$.fn.createProduct = function(){
		var stores = v.split('');
		var data = {
				sku: $("#sku").val(),
				stores: $("#storeID").val(),
				price: parseFloat($("#price").val()),
				name: $("#name").val(),
				unit: unit,
				description: $("#description").val(),
				imageurl: $("#imageurl").val(),
				brand: $("#brand").val(),
		}
		 $.ajax({
			url: "/admin/products",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
                console.log(res);
				showNotification("Products created successfully",'bg-green','bottom','right');
				window.location = "/productList";
            },

			 error: function(res){
				 showNotification("Error Creating Product. Please try again.",'bg-red','bottom','right');
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Creating Product. Please try again.",'bg-red','bottom','right');
            }
		 });
	}
});