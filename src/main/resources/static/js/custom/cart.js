$(document).ready(function(e){
	$(".addItem").click(function(){
		var id = $(this).attr("data-sku");
		
		var val = $("#"+id).val();
		
		callAPI($(this), parseInt(val)+1, "Item Added to Cart", id, parseInt(val)+1,"inc");
	});
	
	$(".removeItem").click(function(){
		var id = $(this).attr("data-sku");
		
		var val = $("#"+id).val();
		
	
		callAPI($(this), parseInt(val)-1, "Item Removed from Cart", id, parseInt(val)-1,"dsc");
	});
	
	function callAPI(selector, qty, message, id, val, rate){
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
				var pageURL = $(location).attr("href");
				if (pageURL.indexOf("getOrdersFromCart") >= 0){
					calculateAndUpdateRow(id, res, data.price, rate);
				}
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
	
	function calculateAndUpdateRow(classSel,res, price, rate){
		if(classSel !== null){
			$("."+classSel).text(res.price);
		}
		
		var subtot = $("#subtotal").text();
		if(rate === "inc"){
			subtot = parseFloat(subtot) + price; 
		}else{
			subtot = parseFloat(subtot) - price; 
		}
		
		 
		var tax = parseFloat(subtot)*(9.25/100);
		
		var con = parseFloat(subtot)*(0.5/100);
	
		var total = subtot + tax + con;
		
		$("#subtotal").text(subtot.toFixed(2));
		$("#tax").text(tax.toFixed(2));
		$("#con").text(con.toFixed(2));
		$("#total").text(total.toFixed(2));
		
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
                showNotification("Store deleted successfully",'bg-green','bottom','right');
                store_row.slideUp('slow').remove();
            },
            error: function(res){
                showNotification("Cannot Deleting Store. Store already linked to products",'bg-red','bottom','right');
            },
            failure: function (res) {
                console.log(res);
                showNotification("Error Deleting Store.",'bg-red','bottom','right');
            }
        });
    });
	
});