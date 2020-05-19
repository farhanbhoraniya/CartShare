$(document).ready(function(){
	
	$.fn.placeOrder = function(){
		//$("#pickUpOrderModal").modal('show');
	} 
	var concredit = 0;
	$("#pickOrder").on("click",function(){
		$("#selfPickup").modal('show');
		$.ajax({
			url: "/order/getCredit",
			type: "GET",
			success: function(req){
				req= JSON.parse(req);
				concredit = req.credit;
				$("#cc").remove();
				$("#bc").remove();
				var span =$("<span>");
				span.attr("id","cc");
				$("#creditBody").append(span.append("Your Contribution credit is: "));
				var button = $("<button>");
				button.attr("id","bc");
				if(req.credit <= -6){
					button.addClass("btn btn-danger waves-effect");
					
					//show yellow
				}else if(req.credit <= -4){
					//show red
					button.addClass("btn bg-yellow waves-effect");
				}else{
					//green
					button.addClass("btn btn-primary waves-effect");
				}
				button.text(req.credit);
				$("#creditBody").append(button);
			},
			failue: function(req){
				
			}
		});
	});
		
	var count = 0;
	
	$("#submitSelfYes").on("click",function(){
		$("#selfPickupConfirmation").modal('hide');
		$(".loader").show();
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

						
							location.href = "/myOrders";
						
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
	$("#submitSelf").on("click",function(){	
		
		var radioValue = $("input[name='self']:checked").val();
		var selfPick = false;
		if(radioValue === "Self"){
			selfPick = true;
		}else if(count == 0 && concredit <= -4){
			$("#selfPickup").modal('hide');
			$("#selfPickupConfirmation").modal('show');
			$("#warningmsg").remove();
			var span =$("<span>");
			span.attr("id","warningmsg");
			var status="";
			if(concredit <= -6){
				status="red";
			}else if(concredit <= -4){
				status="yellow";
			}
			$("#creditConfirmBody").append(span
					.append("WARNING!! You are in the "+status+" zone. Please consider your option and " +
					"select self pickup to maintain your contribution credits. " +
					"\nAre you sure you want to continue with Fellow Pooler Pickup?"));
			return;
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

