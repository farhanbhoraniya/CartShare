$(document).ready(function(e){
	$.fn.createStore = function(){

		$('#createStoreForm').validate({
			rules: {
				'name': {
					required: true,
				},
				'stnumber': {
					required: true,
				},
				'city':{
					required: true,
				},
				'state' : {
					required: true,
				},
				'zipcode' : {
					required: true,
				}
			},

			messages: {
				"name" : {
					required: "Store name is required"
				},
				"stnumber" : {
					required : "St. Number is required",
				},
				"city" : {
					required : "City is required",
				},
			},

			highlight: function (input) {
				$(input).parents('.form-line').addClass('error');
			},
			unhighlight: function (input) {
				$(input).parents('.form-line').removeClass('error');
			},
			errorPlacement: function (error, element) {
				$(element).parents('.input-group').append(error);
			}
		});

		var data = {
				name: $("#name").val(),
				streetname: $("#stname").val(),
				streetnumber: $("#stnumber").val(),
				city: $("#city").val(),
				state: $("#state").val(),
				zip: $("#zipcode").val()
		};

		 $.ajax({
			url: "/admin/store",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {

				showNotification("Store created successfully",'bg-green','bottom','right');
                window.location = "/storeList";
            },
			 error: function(res){
				 showNotification("Error Creating Store. Please try again.",'bg-red','bottom','right');
			 },
            failure: function (res) {
            	console.log(res);
				showNotification("Error Creating Store. Please try again.",'bg-red','bottom','right');
            }
		 });
	}
});