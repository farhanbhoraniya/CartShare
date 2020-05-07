$(document).ready(function(){

	$('#updateProductBtn').click(function(){

		var store_id = $('#storeID').val();
		var sku = $('#sku').val();

		var data = {
			price: parseFloat($("#price").val()),
			name: $("#name").val(),
			unit: $('#unit').val(),
			description: $("#description").val(),
			imageurl: $("#imageurl").val(),
			brand: $("#brand").val(),
		};

		$.ajax({
			url: "/admin/store/" + store_id + "/product/" + sku,
			type: "PUT",
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function (res) {
				console.log(res);
				showNotification("Products updated successfully",'bg-green','bottom','right');
				window.location = "/productList";
			},
			error: function(res){
				showNotification("Error Updating Product. Please try again.",'bg-red','bottom','right');
			},
			failure: function (res) {
				console.log(res);
				showNotification("Error Creating Product. Please try again.",'bg-red','bottom','right');
			}
		});
	});

});