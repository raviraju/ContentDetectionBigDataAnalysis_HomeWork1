'use strict'
var margin = {top: 20, right: 10, bottom: 20, left: 10};
var width  = 500 - margin.left - margin.right;
var height = 500 - margin.top - margin.bottom;
var radius = 200
var colors = d3.scale.category20();	//Categorical colors : we dont care abt specific colors : d3.scale.category20(); d3.scale.category20b(); d3.scale.category20c();

function mapColor(color)
{
	//color values used from https://github.com/mbostock/d3/wiki/Ordinal-Scales
	switch(color){
		case "application_atom+xml" : return "#1f77b4";
		case "application_dif+xml"  : return "#1f77b4";
		case "application_dita+xml" : return "#aec7e8";
		case "application_epub+zip" : return "#ff7f0e";
		case "application_fits" : return "#ffbb78";
		case "application_gzip" : return "#2ca02c";
		case "application_java-vm" : return "#98df8a";
		case "application_msword" : return "#d62728";
		case "application_octet-stream" : return "#ff9896";
		case "application_ogg" : return "#9467bd";
		case "application_pdf" : return "#c5b0d5";
		case "application_postscript" : return "#8c564b";
		case "application_rdf+xml" : return "#c49c94";
		case "application_rss+xml" : return "#e377c2";
		case "application_rtf" : return "#f7b6d2";
		case "application_vnd.apple.keynote" : return "#ff7f0e";
		case "application_vnd.google-earth.kml+xml" : return "#7f7f7f";
		case "application/vnd.google-earth.kmz": return "#7f7f7f";
		case "application_vnd.ms-excel" : return "#c7c7c7";
		case "application_vnd.ms-excel.sheet.4" : return "#bcbd22";
		case "application/vnd.ms-excel.sheet.macroenabled.12": return "#7f7f7f";
		case "application_vnd.ms-htmlhelp" : return "#dbdb8d";
		case "application/vnd.ms-powerpoint": return "#7f7f7f";
        case "application/vnd.oasis.opendocument.text": return "#7f7f7f";
        case "application/vnd.openxmlformats-officedocument": return "#7f7f7f";
		case "application_vnd.oasis.opendocument.presentation" : return "#17becf";
		case "application/vnd.openxmlformats-officedocument.presentationml.slideshow": return "#7f7f7f";
		case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": return "#7f7f7f";
        case "application/vnd.openxmlformats-officedocument.wordprocessingml.document": return "#7f7f7f";
        case "application/vnd.openxmlformats-officedocument.wordprocessingml.template": return "#7f7f7f";
		case "application_vnd.oasis.opendocument.text" : return "#9edae5";
		
		case "application_vnd.rn-realmedia" : return "#393b79";
		case "application_vnd.sun.xml.writer" : return "#5254a3";
		case "application_x-7z-compressed" : return "#6b6ecf";
		case "application_x-bibtex-text-file" : return "#9c9ede";
		case "application_x-bittorrent" : return "#637939";
		case "application_x-bzip" : return "#8ca252";
		case "application_x-bzip2" : return "#b5cf6b";
		case "application_x-compress" : return "#cedb9c";
		case "application_x-debian-package" : return "#8c6d31";
		case "application_x-elc" : return "#bd9e39";
		case "application_x-executable" : return "#e7ba52";
		case "application_x-font-ttf" : return "#e7cb94";
		case "application/x-grib": return "#7f7f7f";
		case "application_x-gtar" : return "#843c39";
		case "application_x-hdf" : return "#ad494a";
		case "application_x-java-jnilib" : return "#d6616b";
		case "application_x-lha" : return "#e7969c";
		case "application_x-matroska" : return "#7b4173";
		case "application_x-msdownload" : return "#a55194";
		case "application_x-msdownload" : return "#ce6dbd";
		case "application_x-msdownload" : return "#de9ed6";
		case "application_x-msmetafile" : return "#3182bd";
		case "application/x-mspublisher": return "#7f7f7f";
		case "application_x-rar-compressed" : return "#6baed6";
		case "application_x-rpm" : return "#9ecae1";
		case "application_x-sh" : return "#c6dbef";
		case "application_x-shockwave-flash" : return "#e6550d";
		case "application_x-sqlite3" : return "#fd8d3c";
		case "application_x-stuffit" : return "#fdae6b";
		case "application_x-tar" : return "#fdd0a2";
		case "application_x-tex" : return "#31a354";
		case "application_x-tika-msoffice" : return "#74c476";
		case "application_x-tika-ooxml" : return "#a1d99b";
		case "application/x-tika-ooxml-protected": return "#7f7f7f";
		case "application_x-xz" : return "#c7e9c0";
		case "application_xhtml+xml" : return "#756bb1";
		case "application_xml" : return "#9e9ac8";
		case "application_xslt+xml" : return "#bcbddc";
		case "application_zip" : return "#dadaeb";
		case "audio_basic" : return "#636363";
		case "audio_mp4" : return "#969696";
		case "audio_mpeg" : return "#bdbdbd";
		case "audio_vorbis" : return "#d9d9d9";
		case "audio_x-aiff" : return "#1f77b4";
		case "audio_x-flac" : return "#aec7e8";
		case "audio_x-mpegurl" : return "#ffbb78";
		case "audio_x-ms-wma" : return "#2ca02c";
		case "audio_x-wav" : return "#98df8a";
		case "image_gif" : return "#d62728";
		case "image_jpeg" : return "#ff9896";
		case "image_png" : return "#9467bd";
		case "image_svg+xml" : return "#c5b0d5";
		case "image_tiff" : return "#8c564b";
		case "image_vnd.adobe.photoshop" : return "#c49c94";
		case "image_vnd.dwg" : return "#e377c2";
		case "image_vnd.microsoft.icon" : return "#f7b6d2";
		case "image_x-bpg" : return "#7f7f7f";
		case "image_x-ms-bmp" : return "#c7c7c7";
		case "image_x-xcf" : return "#bcbd22";
		case "message_rfc822" : return "#dbdb8d";
		case "message_x-emlx" : return "#17becf";
		case "text_html" : return "#9edae5";
		case "text_plain" : return "#393b79";
		case "text_troff" : return "#5254a3";
		case "text_x-diff" : return "#6b6ecf";
		case "text_x-jsp" : return "#9c9ede";
		case "text_x-perl" : return "#637939";
		case "text_x-php" : return "#8ca252";
		case "text_x-python" : return "#b5cf6b";
		case "text_x-vcard" : return "#cedb9c";
		case "video_mp4" : return "#8c6d31";
		case "video_mpeg" : return "#bd9e39";
		case "video_quicktime" : return "#e7ba52";
		case "video_x-flv" : return "#e7cb94";
		case "video_x-m4v" : return "#843c39";
		case "video_x-ms-asf" : return "#ad494a";
		case "video_x-ms-wmv" : return "#d6616b";
		case "video_x-msvideo" : return "#e7969c";
		case "xscapplication_zip" : return "#7b4173";

		default : return "#636363";
	}

}



var chartNo = 1;

function mimeDiversityAnalysis(jsonFileName){
	d3.json(jsonFileName, function(objects) {	
		//console.log(objects);
		var piedata = [];
		var key;
		for (key in objects) 
		{
			piedata.push(objects[key]);
		}
		
		var pie = d3.layout.pie()
				.value(function(d) {
						return d.count;
				});
		
		var arc = d3.svg.arc().outerRadius(radius);
				
		var jsonFile = jsonFileName.replace(/(.*)\.json/g, "$1");
		var idJsonFile = jsonFile;
		if(jsonFile == "initialClassification")
			idJsonFile = "aboutInitialClassification"
		var svg = d3.select('#container').append('svg')
			.attr('width', width + margin.left + margin.right)
			.attr('height', height + margin.top + margin.bottom)
			.attr('padding', "40px")
			.attr('id', idJsonFile);

			
		var myChart = svg.append('g')
			.attr('transform', 'translate('+(width-radius)+','+(height-radius)+')')
			.selectAll('path').data(pie(piedata))
			.enter().append('g')
				.attr('class', 'slice');
		
		svg.append("text")
				.text(jsonFile)
				.attr("transform", "translate(" + 200 + "," + 15 + ")")
				.attr("text-decoration-style", "solid")
				.attr("font", "15px");

		var slices = svg.selectAll('g.slice')
				.append('path')
				.attr('fill', function(d, i) {
					//console.log(d.data.mimeType , " : ", mapColor(d.data.mimeType));
					return mapColor(d.data.mimeType);
				})
				.attr('d', arc);
		
		var tooltip = d3.select('body').append('div')
			.style('position', 'absolute')
			.style('padding', '0 10px')
			.style('background', 'white')
			.style('opacity', 1)
			.style('display', 'none')
			.attr('id',idJsonFile);
			
		var tempColor;

		var text = svg.selectAll('g.slice')
			.on('mouseover', function(d) {
				//console.log(d);
				tooltip.transition()
					.style('opacity', .9)
					.style('display', 'block')
					.style('z-index', 10);
				//console.log(d.data.mimeType);
				tooltip.html(d.data.mimeType + " : " + d.data.count)
					.style('left', (d3.event.pageX - 35) + 'px')
					.style('top',  (d3.event.pageY - 30) + 'px')


				tempColor = this.style.fill;
				d3.select(this)
					.style('opacity', .5)
					.style('fill', 'yellow')
			})

			.on('mouseout', function(d) {
					tooltip.html("")
					tooltip.transition()
						.style('opacity', 0)
						.style('display', 'none');
					d3.select(this)
					.style('opacity', 1)
					.style('fill', tempColor)
			})
	});
}

var jsonFiles = ["initialClassification.json","finalClassification.json"];


for(var i=0; i < jsonFiles.length; i++)
{
	//console.log(jsonFiles[i].replace(/(.*)\.json/g, "$1"));
	mimeDiversityAnalysis(jsonFiles[i]);
}

