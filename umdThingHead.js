function loadTable(){
	writeTable(sortTable(getTable()));
}

function readTable(){
	var table = JSON.parse(tableString);
}

function evaluateTable(array) {
	var selection = document.getElementById("genEdSelector").value;

	for (var i = array.length - 1; i>=0; i--) {
		var a = array[i];
		var aCheck = 0;
		for (var j = 0;j<a.genEds.length;j++){
			if (a.genEds[j] === selection) {
				aCheck++;
			}
		}
		array[i].score = aCheck*10 + array[i].genEds.length;
	}
	return array;
}

function sortTable(array){
	array = evaluateTable(array);
	return array.sort(function (a,b) {
		return a.score-b.score;
	})
}

function writeTable(array){
var s= "<tr><th class=\"dept\">Department<\/th><th class=\"courseNum\">Course Number<\/th><th class=\"courseTitle\">Title<\/th><th class=\"creds\">Credits<\/th><th class=\"genEds\">Gen Eds<\/th><\/tr>\n";
	var table = document.getElementById("courses");
	for (var i = array.length - 1; i >= 0; i--) {
		s=s+"<tr>";
		s=s+"<td class=\"dept\">"+array[i].department+"</td>";
		s=s+"<td class=\"courseNum\"><a href=\""+array[i].link+"\">"+array[i].courseNumber+"</td>";
		s=s+"<td class=\"courseTitle\">"+array[i].title+"</td>";
		s=s+"<td class=\"creds\">"+array[i].credits+"</td>";	 
		s=s+"<td class=\"genEds \">"+array[i].genEdString+"</td>";
		s=s+"</tr>";

	};
	table.innerHTML = s;
}

//*
function getTable(){
	var s = [
// copy pasta the json file, or you know, read the file externally like you are supposed to
		];
	return s;
}
