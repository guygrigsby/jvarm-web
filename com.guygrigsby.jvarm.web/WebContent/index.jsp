<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>jvARM</title>
<script src="http://code.jquery.com/jquery-latest.js">
	
</script>
<script>
	$(document).ready(function() {
		$('#compile').click(function(event) {
			var source = $('#ARMsource').val();
			$.post('Jvarm/Compile', {
				ARMsource : source
			}, function(responseText) {
				$('#console').text(responseText);
			});
		});
	});
	$(document).ready(function() {
		$('#step').click(function(event) {
				$.get('Jvarm/Step', function(registersObject) {
					var table = document.createElement("table");
					registers.innerHTML = "";
					for ( var register in registersObject) {
						if (registersObject.hasOwnProperty(register)) {
							var row = table.insertRow(0);
							var cell1 = row.insertCell(0);
							var cell2 = row.insertCell(1);
							cell1.innerHTML = register;
							cell2.innerHTML = registersObject[register];
						}
					}
					document.getElementById("registers").appendChild(table);
				});
		});
	});
</script>
</head>
<body>
<div id="main">
	<div id="registers" style="float: right;"></div>
	<div style="float: left;">
		<form id="form1">
			<h1>jvARM : compile ARM source</h1>
			<textarea rows="25" cols="50" type="text" id="ARMsource">
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
	
			<input type="button" id="compile" value="Start Debug" /> <input
				type="button" id="step" value="Step" />
			<br />
		</form>
		<div id="console"></div>
	</div>
</div>
</body>
</html>