<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="jvarm.css">
<title>jvARM</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script>
	$(document).ready(function() {
		$('#compile').click(function(event) {
			var source = $('#ARMsource').val();
			$.post('jvarm', {
				ARMsource : source
			}, function(responseText) {
				$('#console').text(responseText);
			});
		});
	});
	$(document).ready(function() {
		$('#step').click(function(event) {
				$.get('jvarm', function(state) {
					$('#console').text("");
					var table = document.createElement("table");
					table.className = "table table-condensed table-hover";
					registers.innerHTML = "";
					for (var i = state.registers.length-1; i >= 0 ; i--) {
						var register = state.registers[i];
						var row = table.insertRow(0);
						var cell1 = row.insertCell(0);
						var cell2 = row.insertCell(1);
						var regName = Object.keys(register);
						cell1.innerHTML = regName;
						cell2.innerHTML = register[regName];
					}
					document.getElementById("registers").appendChild(table);
				});
		});
	});
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();   
	});
</script>
</head>
<body>
<div id="main" class="container">
<div class="row">
	<div class="col-sm-4">
		<form role="form" id="form1">
			<h1>jvARM</h1>
			<a href="#" data-toggle="tooltip" title="Enter source code here.">
			<textarea class="form-control" rows="25" cols="50" type="text" id="ARMsource">
MOV r0, #0
MOV r1, #1
MOV r12, #10
loop
ADD r2, r1, r0
MOV r0, r1
MOV r1, r2
SUB r12, r12, #1
CMP r12, #0
BNE loop</textarea>
</a>
		<a href="#" data-toggle="tooltip" title="Click this first to compile your code.">
			<input type="button" class="btn btn-default" id="compile" value="Start Debug" /> 
		</a>
		<a href="#" data-toggle="tooltip" title="Execute a single line of code.">
			<input type="button" class="btn btn-default" id="step" value="Step" />
		</a>
			<br />
		</form>
	</div>
	<div id="registers" class="col-sm-4"></div>
	<div id=memory class="col-sm-4"></div>
</div>
	<div id="console"></div>
</div>
<div class="navbar navbar-fixed-bottom" role="navigation">
	<div class="container">
		<p>jvArm is open source and is hosted on github.com. 
			Bugs/feature requests may be added <a href="https://github.com/guygrigsby/jvarm-web/issues" >here</a>.
			Source code for the jvARM core can be found 
			<a href="https://github.com/guygrigsby/jvarm">here</a>
			and source code for the webapp can be found 
			<a href="https://github.com/guygrigsby/jvarm-web">here</a>. <br>© 2015 Guy J Grigsby
		</p>
	</div>
</div>

</body>
</html>