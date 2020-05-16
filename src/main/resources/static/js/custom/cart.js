$(document).ready(function(e){
	$(".addItem").click(function(){
		var id = $(this).attr("data-sku");
		
		var val = $("#"+id).val();
		
	
		callAPI($(this), parseInt(val)+1, "Item Added to Cart", id, parseInt(val)+1);
	});
	
	$(".removeItem").click(function(){
		var id = $(this).attr("data-sku");
		
		var val = $("#"+id).val();
		
	
		callAPI($(this), parseInt(val)-1, "Item Removed from Cart", id, parseInt(val)-1);
	});
	
	function callAPI(selector, qty, message, id, val){
		var data = {
				product_sku: selector.attr("data-sku"),
				store_id: parseInt(selector.attr("data-storeid")),
				price: parseFloat(selector.attr("data-price")),
				//price: 35,
				user_id: parseInt(selector.attr("data-userid")),
				quantity: qty//parseInt($(this).val())
		};
		
		$.ajax({
			url: "/updateItemInCart",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				$("#"+id).val(val);
				showNotification(message,'bg-green','bottom','right');
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