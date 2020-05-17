$(document).ready(function(){
	
	$.fn.placeOrder = function(){
		//$("#pickUpOrderModal").modal('show');
	} 
	
	$("#pickOrder").on("click",function(){
		
		$("#selfPickup").modal('show');
	});
		
	$("#submitSelf").on("click",function(){	
		var radioValue = $("input[name='self']:checked").val();
		var selfPick = false;
		if(radioValue === "Self"){
			selfPick = true;
		}
		var data = {
			"selfPick": selfPick
		};
		$.ajax({
			url: "/order/place",
			type: "POST",
			data: JSON.stringify(data),
			contentType: "application/json",
			success: function(req){
				if(req.status === "PLACED"){
					showNotification("Order Placed successfully",'bg-green','bottom','right');

					if(selfPick != false){
						$("#selfPickup").modal('hide');
						$("#pickUpOrderModal").modal('show');
					}else{
						location.href = "/myOrders";
					}
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
		location.href = "/myOrders";
	});

});

