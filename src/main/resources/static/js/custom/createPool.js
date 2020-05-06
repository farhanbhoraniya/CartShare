$(document).ready(function(e){
	$.fn.createPool = function(){
		var data = {
				name: $("#poolid").val(),
				streetname: $("#name").val(),
				streetnumber: parseInt($("#nname").val()),
				city: $("#description").val(),
				zip: $("#zipcode").val()
		}
		//call to create Pool API
//		 $.ajax({
//			url: "/admin/store",
//			type: "POST",
//			contentType: "application/json",
//			data: JSON.stringify(data),
//			success: function (res) {
//                console.log(res);
//            },
//            failure: function (res) {
//            	console.log(res);
//            }
//		 });
	}
});