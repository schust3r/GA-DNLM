$(function() {
    	$('img').on('click', function() {
			$('.enlargeImageModalSource').attr('src', $(this).attr('src'));
			$('#enlargeImageModal').modal('show');
		});
});

document.getElementById("ground_file").onchange = function() {
    startUpload();
};

function startUpload() {	
	document.getElementById("dice_res").innerHTML = "Espere mientras se procesa la imagen...";	
    var xhr = new XMLHttpRequest();
    var fd = new FormData();
    var file = document.getElementById('ground_file').files[0];
  	
    fd.append("multipartFile", file);
	fd.append("id", document.getElementById('_id').innerHTML);
	fd.append("ival", document.getElementById('_ival').innerHTML);
	    
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
	      	if (xhr.status == 200) {
	      		document.getElementById("dice_res").innerHTML = xhr.responseText;
	       		document.getElementById("dice_res").className = "alert alert-success";
	      	}
		}
	}
	    
	xhr.open("POST", "evaluarGroundtruth");
	xhr.send(fd);
}