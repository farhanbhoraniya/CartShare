$(document).ready(function(e){

	$("#sendMail").click(function(){

		var to = $('#chatID').val();
		var message = $('#message').val();
		var subject = $('#subject').val();

		if(to == "")
		{
			showNotification("Please select a pooler to message",'bg-red','bottom','right');
			return false;
		}

		if(subject == "")
		{
			showNotification("Please enter a subject.",'bg-red','bottom','right');
			return false;
		}

		if(message == "")
		{
			showNotification("Please enter a message.",'bg-red','bottom','right');
			return false;
		}
		
		var data ={
			to: to,
			subject: $("#subject").val(),
			message: $("#message").val()
		}
		
		$.ajax({
			url: "/pooler/sendChatEmail",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {

				showNotification("Email Sent Successfully",'bg-green','bottom','right');
				$("#subject").val('');
				$("#message").val('');
				$('#chatID').val('').change();
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