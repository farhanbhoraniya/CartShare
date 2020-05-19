$(document).ready(function(e){
	$("#sendMail").click(function(){
		var nooforder = $("#nooforders").val();
		if(nooforder === undefined){
			nooforder = "0";
		}
		var storeid = $("#nooforders").attr("data-store-id");
		if(storeid === undefined){
			storeid = "not";
		}
		var data = {
				nooforders: nooforder,
				storeid: storeid
		};
		$.ajax({
			url: "/order/addLinkedOrders",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				
				$("#pickupinfo").modal('show');
            },
			 error: function(res){
				 showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Adding Item. Please try again.",'bg-red','bottom','right');
            }
		 });
		 
	});
	
	$("#no").on("click",function(){
		location.href = "/myOrders";
	});
	
});