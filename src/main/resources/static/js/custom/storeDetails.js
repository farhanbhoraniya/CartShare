$.fn.storeDetails = function(){ 
	window.location.href = "/storeDetails"; 
}

$(document).ready(function(){
	
	$.ajax({
		url: "/stores",
		type: "GET",
		contentType: "application/json",
		success: function (res) {
			var $dropdown = $("#searchStore");
			var $ul = $("ul.dropdown-menu.inner");
			var i=0;
			$.each(res, function() {
			    $dropdown.append($("<option/>").val(this.id).text(this.name));
			    $ul.append($("</li>").text("abc"));
			    //var $li = ;
			    //$li.append($("</a>").addClass("waves-effect waves-block"));
			    i+=1;
			});
			
        },
        failure: function (res) {
        	console.log(res);
        }
	 });
	
	
	var data = {
			name: $("#name").val(),
			streetname: $("#stname").val(),
			streetnumber: parseInt($("#stnumber").val()),
			city: $("#city").val(),
			state: $("#state").val(),
			zip: $("#zipcode").val()
	}
//	 $.ajax({
//		url: "/store/",//+storeId,
//		type: "POST",
//		contentType: "application/json",
//		data: JSON.stringify(data),
//		success: function (res) {
//            console.log(res);
//        },
//        failure: function (res) {
//        	console.log(res);
//        }
//	 });
	
	var tr = $("<tr>");
	tr.append("<td>col1</td>");
	tr.append("<td>col2</td>");
	tr.append("<td>col3</td>");
	tr.append("<td>col4</td>");
	tr.append("<td>col5</td>");
	tr.append("<td>col6</td>");
	tr.append("<td>col7</td>");
	
	var button = $("<button>");
	button.attr("type","button");
	button.addClass("btn bg-red btn-circle waves-effect waves-circle waves-float");
	
	var i = $("<i>");
	i.addClass("material-icons");
	i.text("delete");
	button.append(i);
	
	var td = $("<td>");
	td.css("width","2%");
	td.addClass("delRow")
	td.attr("id","col1"); //needs to be changed 
	td.append(button);
	tr.append(td);
	$(".storeDet").append(tr);
	
	button = $("<button>");
	button.attr("type","button");
	button.addClass("btn btn-info btn-circle waves-effect waves-circle waves-float");
	
	i = $("<i>");
	i.addClass("material-icons");
	i.text("edit");
	button.append(i);
	
	td = $("<td>");
	td.css("width","2%");
	td.addClass("editRow");
	td.attr("id","col1"); //needs to be changed
	td.append(button);
	tr.append(td);
	
	$(".storeDet").append(tr);
	
	$(".editRow").on("click",function(){
		var storeID = $(this).attr("id");
		//send ajax request to edit this row
		//if success
		$("#"+storeID).parent().each(function (i, el) {
	        var $tds = $(this).find('td'),
	            no = $tds.eq(0).text(),
	            storeID = $tds.eq(1).text(),
	            storeName = $tds.eq(2).text(),
	        	streetName = $tds.eq(3).text(),
	        	city = $tds.eq(4).text(),
	        	state = $tds.eq(5).text(),
	        	zipcode = $tds.eq(6).text();
	        	
	        var editObj = new Object();
	        editObj.no = no;
	        editObj.storeID = storeID;
	        editObj.storeName = storeName;
	        editObj.streetName = streetName;
	        editObj.city=city;
	        editObj.state=state;
	        editObj.zipcode=zipcode;
	        
	        console.log(editObj);
	        sessionStorage.editDetails = JSON.stringify(editObj);
	    });
		
		window.location.href = "/editStoreDetails";
	});

	$(".delRow").on("click",function(){
		var storeID = $(this).attr("id");
		//send ajax request to delete this row
		//if success
		$("#"+storeID).parent().remove();
	});
	
});