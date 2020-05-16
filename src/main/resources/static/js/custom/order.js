$(document).ready(function(e){
	$("#sendMail").click(function(){
		var nooforder = $("#nooforders").val();
		var storeid = $("#nooforders").attr("data-store-id");
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
		windows.href = "/userStoreDetails";
	});
	
});