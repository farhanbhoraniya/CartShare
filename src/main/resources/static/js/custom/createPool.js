$(document).ready(function(e){
	$.fn.createPool = function(){
		var data = {
				id: $("#poolid").val(),
				name: $("#name").val(),
				neighborhood: $("#nname").val(),
				leader: 5,//parseInt($("#leader").val()),add the user id here
				description: $("#description").val(),
				zip: $("#zipcode").val()
		}
		//call to create Pool API
		 $.ajax({
			url: "/pool",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {

				showNotification("Pool created successfully",'bg-green','bottom','right');
                window.location = "/poolList";
            },
			 error: function(res){
				 showNotification("Error Creating Pool. Please try again.",'bg-red','bottom','right');
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Creating Pool. Please try again.",'bg-red','bottom','right');
            }
		 });
	}
});