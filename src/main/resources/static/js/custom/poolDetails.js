$(document).ready(function(){
	//ajax call to get store details
	var tr = $("<tr>");
	tr.append("<td>poolid</td>");
	tr.append("<td>name</td>");
	tr.append("<td>neighborhoodname</td>");
	tr.append("<td>desc</td>");
	tr.append("<td>zipcode</td>");
	
	var button = $("<button>");
	button.attr("type","button");
	button.attr("id","poolid");
	button.addClass("btn bg-primary btn-circle waves-effect waves-circle waves-float add");
	
	var i = $("<i>");
	i.addClass("material-icons");
	i.text("add");
	button.append(i);
	
	var td = $("<td>");
	td.css("width","2%");
	td.addClass("delRow")
	td.attr("id","col1"); //needs to be changed 
	td.append(button);
	tr.append(td);
	$(".poolDetails").append(tr);
	
	button = $("<button>");
	button.attr("type","button");
	button.attr("id","poolid");
	button.addClass("btn btn-danger btn-circle waves-effect waves-circle waves-float remove");
	
	i = $("<i>");
	i.addClass("material-icons");
	i.text("remove");
	button.append(i);
	
	td = $("<td>");
	td.css("width","2%");
	td.addClass("editRow");
	td.attr("id","col1"); //needs to be changed
	td.append(button);
	tr.append(td);
	
	$(".poolDetails").append(tr);
	
	$(".add").on("click", function(){
		//ajax call to join pooler
	});
	
	$(".remove").on("click", function(){
		//ajax call to remove pooler
	});
});