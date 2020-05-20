$(document).ready(function(){
	$(function () {

		$('#sign_up').validate({
	        rules: {
	            'confirm': {
	                equalTo: '[name="password"]'
	            }
	        },
	        highlight: function (input) {
	            console.log(input);
	            $(input).parents('.form-line').addClass('error');
	        },
	        unhighlight: function (input) {
	            $(input).parents('.form-line').removeClass('error');
	        },
	        errorPlacement: function (error, element) {
	            $(element).parents('.input-group').append(error);
	            $(element).parents('.form-group').append(error);
	        }
	    });
	});

	$('#social_register_complete').validate({
		rules: {
			'confirm': {
				equalTo: '[name="password"]'
			}
		},
		highlight: function (input) {
			console.log(input);
			$(input).parents('.form-line').addClass('error');
		},
		unhighlight: function (input) {
			$(input).parents('.form-line').removeClass('error');
		},
		errorPlacement: function (error, element) {
			$(element).parents('.input-group').append(error);
			$(element).parents('.form-group').append(error);
		}
	});

	$.fn.login = function(){ 
		window.location.href = "/verification"; 
	};


	$.fn.verificationPage = function(){ 
		
		var name = $("#name").val();
		var email = $("#email").val();
		var nickname = $("#nickname").val();
		var password = $("#password").val();
		
		var data = {
				"screenname": name,
				"email": email,
				"nickname": nickname,
				"password": password
		}
		var json_data = JSON.stringify(data);
		$.ajax({
			url: "/register",
			contentType: "application/json",
			type: "POST",
			data: json_data,
			success: function(result){
				console.log("Success");
				window.location.href = "/register/success";
			},
			error: function(result){
				console.log("Error");
				console.log(result);
				showNotification(result.responseJSON.message,'bg-red','bottom','right');
			},
			failure: function(result){
				// console.log("Failure");
				// console.log(result);
				showNotification(result.responseJSON.message,'bg-red','bottom','right');
			}
		});
	}

	$.fn.updateSocialRegister = function(){

		var name = $("#name").val();
		var email = $('#email').attr('data-email');
		var nickname = $("#nickname").val();
		var password = $("#password").val();

		var data = {
			"screenname": name,
			"nickname": nickname,
			"password": password,
			"email" : email
		}
		var json_data = JSON.stringify(data);
		$.ajax({
			url: "/register/social/update",
			contentType: "application/json",
			type: "POST",
			data: json_data,
			success: function(result){
				console.log("Success");
				console.log(result);
				window.location.href = "/register/success";
			},
			error: function(result){
				console.log("Error");
				console.log(result);
				showNotification(result.responseJSON.message,'bg-red','bottom','right');
			},
			failure: function(result){
				// console.log("Failure");
				// console.log(result);
				showNotification(result.responseJSON.message,'bg-red','bottom','right');
			}
		});
	}


});