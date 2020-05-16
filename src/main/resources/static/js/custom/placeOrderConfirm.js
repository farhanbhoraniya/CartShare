$(document).ready(function(){
	
	$.fn.placeOrder = function(){
		//$("#pickUpOrderModal").modal('show');
	} 
	
	$("#pickOrder").on("click",function(){
		var data = {
			"selfPick": false
		};
		$.ajax({
			url: "/order/place",
			type: "POST",
			data: JSON.stringify(data),
			contentType: "application/json",
			success: function(req){
				if(req.status === "PLACED"){
					showNotification("Order Placed successfully",'bg-green','bottom','right');

					//location.reload(true);

					$("#pickUpOrderModal").modal('show');
					//$("#pickUpOrderModal").modal('hide');
					//$("#noOfOrders").modal('show');
					

				}
			},
			error: function(req){
				console.log(req);
			},
			failure: function(req){
				console.log(req);
			}
		});
	});
		
	$("#notpickOrder").on("click",function(){
		location.reload(true);
	});
	
	$("#pickOrderConfirm").on("click",function(){
		
	});

});

	
//	$.fn.getOrderDetailsforPickUp = function(){
//		var data = $("#num").val();
//		$.ajax({
//			url: "",
//			type: "GET",
//			success: 
//		});
//	}
//});

