function show_file_orig() {
  var filename = $('#origfile').val().split('\\').pop();
  $("#orig").val(filename);
}

function show_file_ground() {
  var filename = $('#groundfile').val().split('\\').pop();
  $("#ground").val(filename);
}