$(document).ready(function(e){
	var selector, qty, message, id, val, rate, delPrevItems;
	$(".addItem").click(function(){
		var sku = $(this).attr("data-sku");
		var unit = $(this).attr("data-unit");
		var inval = $("#"+sku).val();
		selector = $(this);
		qty = parseInt(inval)+1;
		message = "Item Added to Cart";
		id = sku;
		val = inval+1;
		rate = "inc";
		delPrevItems = false;
		if(unit === 'Piece' && val > 1){
			return;
		}
		$(".addItem").attr('disabled', true);
		$(".removeItem").attr('disabled', true);
		callAPI();
		
	});
	
	$(".removeItem").click(function(){
		var sku = $(this).attr("data-sku");
		
		var inval = $("#"+sku).val();
		
		selector = $(this);
		qty = parseInt(inval)-1;
		message = "Item Removed from Cart";
		id = sku;
		val = inval-1;
		rate = "dsc";
		delPrevItems = false;
		
		if(val >= 0){
			$(".removeItem").attr('disabled', true);
			$(".addItem").attr('disabled', true);
			callAPI();
		}
	});
	
	function callAPI(){
		var data = {
				product_sku: selector.attr("data-sku"),
				store_id: parseInt(selector.attr("data-storeid")),
				price: parseFloat(selector.attr("data-price")),
				user_id: parseInt(selector.attr("data-userid")),
				quantity: qty,
				delPrevItems: delPrevItems
		};
		
		$.ajax({
			url: "/updateItemInCart",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				res = JSON.parse(res);
				if(res.code == 300){
					$(".removeItem").attr('disabled', false);
					$(".addItem").attr('disabled', false);
					showNotification("You need to join a pool to add items in cart",'bg-red','bottom','right');
					
				 }
				 else if(res.code == 500){
					 $(".removeItem").attr('disabled', false);
						$(".addItem").attr('disabled', false);
					 $("#confirmationModal").modal('show'); 
				 }else{
					 
					 $("#"+id).val(qty);
					 showNotification(message,'bg-green','bottom','right');
					 var pageURL = $(location).attr("href");
					 if (pageURL.indexOf("getOrdersFromCart") >= 0){
						 calculateAndUpdateRow(id, res, data.price, rate);
					 }
					 $(".removeItem").attr('disabled', false);
						$(".addItem").attr('disabled', false);
				}
            },
			 error: function(res){
				 res = JSON.parse(res);
				 
					 showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
				 
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
            }
		 });
	}
	
	$("#confirmDel").on("click",function(){
		 $("#confirmationModal").modal('hide');
		 if(delPrevItems == false){
			 delPrevItems = true;
			 callAPI();
		 }
	 });
	
	function calculateAndUpdateRow(classSel,res, price, rate){
		if(classSel !== null){
			$("."+classSel).text('$'+res.price);
		}
		
		var subtot = $("#subtotal").text();
		subtot = subtot.replace("$", "");
		if(rate === "inc"){
			subtot = parseFloat(subtot) + price; 
		}else{
			subtot = parseFloat(subtot) - price; 
		}
		
		 
		var tax = parseFloat(subtot)*(9.25/100);
		
		var con = parseFloat(subtot)*(0.5/100);
	
		var total = subtot + tax + con;
		
		$("#subtotal").text('$'+subtot.toFixed(2));
		$("#tax").text('$'+tax.toFixed(2));
		$("#con").text('$'+con.toFixed(2));
		$("#total").text('$'+total.toFixed(2));
		
	}
	
	$('.store_delete_btn').click(function(){
		
        var store_row = $(this).parent().parent();
        var store_id = $(this).attr('data-id');
        var price = $(this).attr('data-price');

        $.ajax({
            url: "/removeItemFromCart/" + store_id ,
            type: "DELETE",
            contentType: "application/json",
            success: function (res) {
            	calculateAndUpdateRow(null,null,price,"dsc");
                showNotification("Item deleted successfully",'bg-green','bottom','right');
                store_row.slideUp('slow').remove();
            },
            error: function(res){
                showNotification("Cannot Delete Item.",'bg-red','bottom','right');
            },
            failure: function (res) {
                console.log(res);
                showNotification("Error Deleting Item.",'bg-red','bottom','right');
            }
        });
    });
	
});