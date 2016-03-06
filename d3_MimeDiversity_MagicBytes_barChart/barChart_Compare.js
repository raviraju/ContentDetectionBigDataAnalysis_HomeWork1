
var margin = {top: 20, right: 30, bottom: 40, left: 30},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

var x = d3.scale.linear()
    .range([0, width]);

var y = d3.scale.ordinal()
    .rangeRoundBands([0, height], 0.1);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickSize(0)
    .tickPadding(6);

var svg = d3.select("body").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.json("comparison.json", function(data){
  //console.log(data);
  x.domain(d3.extent(data, function(d) { return d.count; })).nice();
  y.domain(data.map(function(d) { return d.mimeType; }));

  svg.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", function(d) { return "bar bar--" + (d.count < 0 ? "negative" : "positive"); })
      .attr("x", function(d) { return x(Math.min(0, d.count)); })
      .attr("y", function(d) { return y(d.mimeType); })
      .attr("width", function(d) { return Math.abs(x(d.count) - x(0)); })
      .attr("height", y.rangeBand());

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(" + x(0) + ",0)")
      .call(yAxis);
	  
var tooltip = d3.select('body').append('div')
	.style('position', 'absolute')
	.style('padding', '0 10px')
	.style('background', 'white')
	.style('opacity', 1)
	.style('display', 'none');
	
var tempColor;

var text = svg.selectAll('.bar')
	.on('mouseover', function(d) {
		//console.log(d);
		tooltip.transition()
			.style('opacity', .9)
			.style('display', 'block')
			.style('z-index', 10);
		//console.log(d.mimeType);
		tooltip.html(d.count)
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