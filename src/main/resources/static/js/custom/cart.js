$(document).ready(function(e){
	$.fn.addItemInCart = function(){
		var data = {
				product_sku: $("#addItem").attr("data-sku"),
				store_id: parseInt($("#addItem").attr("data-storeid")),
				price: parseFloat($("#addItem").attr("data-price")),
				//price: 35,
				user_id: parseInt($("#addItem").attr("data-userid")),
				quantity: parseInt($("#qty").val())
		}
		//call to create Pool API
		 $.ajax({
			url: "/addItemToCart",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {

				showNotification("Item added to Cart",'bg-green','bottom','right');
            },
			 error: function(res){
				 showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
            }
		 });
	}
});