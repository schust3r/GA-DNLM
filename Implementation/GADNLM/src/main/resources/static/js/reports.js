$(document).ready(function(){
    columnChart();
    
    function columnChart(){
        var item = $('.chart', '.column-chart').find('.item'),
        itemWidth = 100 / item.length;
        item.css('width', itemWidth + '%');
        
        $('.column-chart').find('.item-progress').each(function(){
            var itemProgress = $(this),
            itemProgressHeight = $(this).parent().height() * ($(this).data('percent') / 100);
            itemProgress.css('height', itemProgressHeight);
        });
    };
});

function export_svg(button_id, chart_id) {    
	  $("#" + chart_id + " svg").attr({ version: '1.1' , xmlns:"http://www.w3.org/2000/svg"});
	  var svg = $("#" + chart_id + " svg").parent().html();
	  document.getElementById(button_id + "-aux").value = svg; 	  
}