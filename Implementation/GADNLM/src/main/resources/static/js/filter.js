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
  if (input_id == 'text_file') {
    read_text();
  }
}

function load_from_select() {
  var select = document.getElementById('configs');
  var par = select.value;
  if (par != null && par != "") {
    par = par.split(',')
    set_params(par[0], par[1], par[2]);
  } else {
    set_params("","","");
  }
}

function read_text() {
  var fileInput = document.getElementById('text_file');
  var file = fileInput.files[0];
  var reader = new FileReader();
  reader.readAsText(file);
  reader.onload = function(event) {
    var par = reader.result.split(',');
    set_params(par[0], par[1], par[2]);  
  };    
}

function set_params(w, w_n, s_r) {
  document.getElementById('w_value').value = w;
  document.getElementById('w_n_value').value = w_n;
  document.getElementById('sigma_r_value').value = s_r;
}