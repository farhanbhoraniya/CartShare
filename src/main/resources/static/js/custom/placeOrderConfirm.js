$(document).ready(function(){
	
	$.fn.placeOrder = function(){
		$("#pickUpOrderModal").modal('show');
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
					location.reload(true);
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
			var data = {
				"selfPick": true
			};
			$.ajax({
				url: "/order/place",
				type: "POST",
				data: JSON.stringify(data),
				contentType: "application/json",
				success: function(req){
					if(req.status === "PLACED"){
						showNotification("Order Placed successfully",'bg-green','bottom','right');
						location.reload(true);
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
});
