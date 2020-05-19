$(document).ready(function(e){
	var v="";
	$("#chatID").change(function(){
        v = $(this).children("option:selected").val();
	});
	$("#sendMail").click(function(){
		
		
		var data ={
			to: v,
			subject: $("#subject").val(),
			message: $("#message").val()
		}
		
		$.ajax({
			url: "/sendChatEmail",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				showNotification("Email Sent Successfully",'bg-green','bottom','right'); 
            },
			error: function(res){
				showNotification("Error in sending Email.",'bg-red','bottom','right'); 
			},
            failure: function (res) {
            	console.log(res);
				showNotification("Error in sending Email",'bg-red','bottom','right');
            }
		 });
	});
});