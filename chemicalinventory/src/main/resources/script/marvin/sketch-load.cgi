#!/usr/bin/perl
use CGI qw(param);

$molfile=param('molfile');
$ns2=(substr($ENV{"HTTP_USER_AGENT"},0,11) eq "Mozilla/2.0");

print <<EOF;
Content-type: text/html

<html>
<head>
<title>Open molfile</title>
<script LANGUAGE="JavaScript">
<!--
function init() {
EOF

if($ns2) {
	print "\tvar s = \"\";\n";
} else {
	print "\tvar s = new String(\"\");\n";
}

$i=0;
while (<$molfile>) {
	print "s+=\"";
	s/\r//;
	chomp;
	print;
	print "\\n\";\n";
	++$i;
}

if($i > 0) {
	print <<EOF;
	if(parent != null && parent.msketch != null) {
		var d = parent.msketch.document;
		if(d.MSketch != null) {
			d.MSketch.setMol(s);
		}
	}
EOF
}

print <<EOF;
}
// -->
</script>
</head>
<body BGCOLOR="#ffffff" onLoad="init()">
<form NAME="fileform" ENCTYPE="multipart/form-data" ACTION="sketch-load.cgi" METHOD="post" VALUE="$molfile">
<table BORDER=0 CELLSPACING=0 CELLPADDING=0>
<tr>
<td><font FACE="Helvetica, Arial">Molfile:</font></td>
<td><font FACE="Helvetica, Arial">
    <input TYPE="FILE" SIZE="30" NAME="molfile">
    </font>
    </td>
<td WIDTH=10></td>
<td><font FACE="Helvetica, Arial">
    <input TYPE="SUBMIT" VALUE="Load via web server">
    </font>
    </td>
<!-- <td><font FACE="Helvetica, Arial">
    <input TYPE="BUTTON" VALUE="Try to load directly"
	onClick="parent.msketch.document.MSketch.setMol(molfile.value);return false">
    </font>
    </td>
-->
</tr>
</table>
</form>
</body>
</html>
EOF
