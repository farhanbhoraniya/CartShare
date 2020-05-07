$(document).ready(function(){

	$('#updateStore').click(function(){
		var store_id = $('#editStoreForm').attr('data-id');
		var data = {
			name: $("#name").val(),
			streetname: $("#stname").val(),
			streetnumber: $("#stnumber").val(),
			city: $("#city").val(),
			state: $("#state").val(),
			zip: $("#zipcode").val()
		};

		$.ajax({
			url: "/admin/store/" + store_id ,
			type: "PUT",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {

				showNotification("Store updated successfully",'bg-green','bottom','right');
				window.location = "/storeList";
			},
			error: function(res){
				showNotification("Error Updating Store. Please try again.",'bg-red','bottom','right');
			},
			failure: function (res) {
				console.log(res);
				showNotification("Error Updating Store. Please try again.",'bg-red','bottom','right');
			}
		});
	});


});