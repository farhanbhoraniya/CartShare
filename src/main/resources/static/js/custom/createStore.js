$(document).ready(function(e){
	$.fn.createStore = function(){
		var data = {
				name: $("#name").val(),
				streetname: $("#stname").val(),
				streetnumber: parseInt($("#stnumber").val()),
				city: $("#city").val(),
				state: $("#state").val(),
				zip: $("#zipcode").val()
		}
		 $.ajax({
			url: "/admin/store",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
                console.log(res);
            },
            failure: function (res) {
            	console.log(res);
            }
		 });
	}
});