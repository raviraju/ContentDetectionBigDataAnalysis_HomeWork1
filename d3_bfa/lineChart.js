function bfa_fingerprint(jsonFile, svg_id){

	
	//Width and height
	var w = 500;
	var h = 300;
	var padding = 50;

	//scale functions
	var xScale = d3.scale.linear()
						 .domain([0, 255])
						 .range([padding, w - padding ]);

	var yScale = d3.scale.linear()
						 .domain([0, 1])
						 .range([h - padding, padding]);
						 
	var tempScale = d3.scale.linear()
                     .domain([50, 450])
                     .range([0, 255]);
					 
	//axis functions
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom");

	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("left");

	function mag_tooltip(d){
		//console.log(this);
		var mouseX = d3.mouse(this)[0]; 
		var byteValue = Math.round(tempScale(mouseX)); 
		//console.log(mouseX," : ", byteValue, " : ", String.fromCharCode(byteValue) );
		var svg_string = "#mag_" + svg_id;
		//console.log(svg_string);
		var current_svg = d3.select(svg_string);
		var title = d3.select(svg_string).select("title");
		title.text(String.fromCharCode(byteValue));
		console.log(String.fromCharCode(byteValue));
	}

	function corr_tooltip(d){
		//console.log(this);
		var mouseX = d3.mouse(this)[0]; 
		var byteValue = Math.round(tempScale(mouseX)); 
		//console.log(mouseX," : ", byteValue, " : ", String.fromCharCode(byteValue) );
		var svg_string = "#corr_" + svg_id;
		//console.log(svg_string);
		var current_svg = d3.select(svg_string);
		var title = d3.select(svg_string).select("title");
		title.text(String.fromCharCode(byteValue));
		console.log(String.fromCharCode(byteValue));
	}
	//Create SVG element
	var svg = d3.select("body")
				.append("svg")
				.attr("width", w)
				.attr("height", h)


	var magnitudeLine = d3.svg.line()
							.x(function(d,i) { /*console.log("i : ", i," xScale(i) : ", xScale(i));*/ return xScale(i); })
							.y(function(d) { /*console.log("d.magnitude : ", d.magnitude," yScale(d.magnitude) : ", yScale(d.magnitude));*/ return yScale(d.magnitude); });

	var correlateLine = d3.svg.line()
							.x(function(d,i) { /*console.log("i : ", i," xScale(i) : ", xScale(i));*/ return xScale(i); })
							.y(function(d) { /*console.log("d.corr_strength : ", d.corr_strength," yScale(d.corr_strength) : ", yScale(d.corr_strength));*/ return yScale(d.corr_strength); });
							
	var xAxisRef = svg.append("g")
		.attr("class", "axis")
		.attr("transform", "translate(" + 0 + "," + (h - padding) + ")")

		xAxisRef.append("text")
		  .attr("transform", "rotate(0)")
		  .attr("x", 250)
		  .attr("y", 40)
		  .attr("dx", ".71em")
		  .style("text-anchor", "middle")
		  .text("Byte Value");	
		xAxisRef.call(xAxis);

	var yAxisRef = svg.append("g")
					.attr("class", "axis")
					.attr("transform", "translate(" + padding + ",0" + ")");
						
		yAxisRef.append("text")
		  .attr("transform", "rotate(-90)")
		  .attr("x", -150)
		  .attr("y", -40)
		  .attr("dy", ".71em")
		  .style("text-anchor", "middle")
		  .text("Frequency");
		yAxisRef.call(yAxis);

	var freq_no_of_files;
	var corr_no_of_files;
	d3.json(jsonFile, function(error, jsonData) {
		if (error) {
			console.log(error);
		} else {
			freq_no_of_files = jsonData["freq_no_of_files"];
			corr_no_of_files = jsonData["corr_no_of_files"];
			
			svg.append("text")
				.text("BFA fingerprint for : " + jsonFile)
				.attr("transform", "translate(" + 100 + "," + 12 + ")")
				.attr("text-decoration-style", "solid")
				.classed("textClass", true)
			svg.append("text")
				.text("Avg frequency distribution on " + freq_no_of_files + " files")
				.attr("transform", "translate(" + 150 + "," + 30 + ")")
				.attr("text-decoration-style", "solid")
				.classed("textClass", true)
			svg.append("text")
				.text("Correlation strength on " + corr_no_of_files + " files")
				.attr("transform", "translate(" + 150 + "," + 48 + ")")
				.attr("text-decoration-style", "solid")
				.classed("textClass", true)
				

			//console.log("Avg freq distribution on : ", freq_no_of_files);
			//console.log("Corelation strength on : ", corr_no_of_files);

			var magnitudeGrp = svg.append("g");
			magnitudeGrp.append("path")
						.datum(jsonData["data"])
						.attr("id", "mag_"+svg_id)
						.attr("class", "blueline")
						.attr("d", magnitudeLine)
						.on("mousemove", mag_tooltip)
						.append("title");
						
			var correlateGrp = svg.append("g");
			correlateGrp.append("path")
						.datum(jsonData["data"])
						.attr("id", "corr_"+svg_id)
						.attr("class", "redline")
						.attr("d", correlateLine)
						.on("mousemove", corr_tooltip)
						.append("title");
		}
	});
}
/*
C:\wamp64\www\d3_bfa\json>dir
 Volume in drive C is Windows
 Volume Serial Number is 6C80-0B67

 Directory of C:\wamp64\www\d3_bfa.json",

02/26/2016  12:32 AM    <DIR>          .
02/26/2016  12:32 AM    <DIR>          ..
02/25/2016  11:54 PM            13,002 application_atom+xml.json",
02/25/2016  11:54 PM            11,807 application_dif+xml.json",
02/26/2016  12:09 AM            13,732 application_gzip.json",
02/26/2016  12:10 AM            13,147 application_rdf+xml.json",
02/26/2016  12:15 AM            13,611 application_rss+xml.json",
02/26/2016  12:18 AM            14,009 application_vnd.ms-excel.json",
02/26/2016  12:19 AM            11,331 application_x-sh.json",
02/26/2016  12:22 AM            13,794 application_x-tika-msoffice.json",
02/26/2016  12:25 AM            13,062 application_xml.json",
02/25/2016  11:51 PM             9,780 application_zip.json",
02/25/2016  11:51 PM             9,780 image_vnd.microsoft.icon.json",
02/25/2016  11:51 PM             9,780 text_plain.json",
02/25/2016  11:51 PM             9,780 text_x-matlab.json",
02/25/2016  11:51 PM             9,780 video_quicktime.json",
              14 File(s)        166,395 bytes
               2 Dir(s)  383,219,367,936 bytes free

C:\wamp64\www\d3_bfa\json>
*/
var jsonFiles = [
"json/application_atom+xml.json",
"json/application_dif+xml.json",
"json/application_gzip.json",
"json/application_rdf+xml.json",
"json/application_rss+xml.json",
"json/application_vnd.ms-excel.json",
"json/application_x-sh.json",
"json/application_x-tika-msoffice.json",
"json/application_xml.json",
"json/application_zip.json",
"json/image_vnd.microsoft.icon.json",
"json/text_plain.json",
"json/text_x-matlab.json",
"json/video_quicktime.json",
"json/application_octet-stream.json",
];
for(var i=0; i < jsonFiles.length; i++)
{
	bfa_fingerprint(jsonFiles[i], i+1);
}