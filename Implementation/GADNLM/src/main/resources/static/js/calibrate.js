function show_filenames(input_id,label_id) {
  var inp = document.getElementById(input_id);
  var filelist = "";
  var i = 0;
  for (i = 0; i < inp.files.length - 1; i++) {
    var name = inp.files.item(i).name;
    filelist += name + ", ";
  }
  filelist += inp.files.item(i).name;
  $('#' + label_id).val(filelist);
}