$(document).ready(function(e){
	$.fn.updateProfile = function()  {
		var addId = $("#update").attr("data-id");
		
		var data = {
			streetNo : $("#streetno").val(),
			streetName : $("#streetname").val(),
			city : $("#city").val(),
			state : $("#state").val(),
			zip : $("#zipcode").val()
		}
		
		$.ajax({
			url: "/useraddress/"+addId,
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				
				showNotification("Information Updated",'bg-green','bottom','right');
				
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