 var loteName, totalFileLength, totalUploaded, fileCount, filesUploaded;

 //Will be called when upload is completed
 function onUploadComplete(e) {
     totalUploaded += document.getElementById('files').files[filesUploaded].size;
     filesUploaded++;
     if (filesUploaded < fileCount) {
         uploadNext();
     } else {
         var bar = document.getElementById('bar');
         bar.style.width = '100%';
         bar.innerHTML = '100% completado';
     }
 }

 //Will be called when user select the files in file control
 function onFileSelect(e) {
     var files = e.target.files; // FileList object
     var output = [];
     fileCount = files.length;
     totalFileLength = 0;
     for (var i = 0; i < fileCount; i++) {
         var file = files[i];
         output.push(file.name, ' (', Math.round(file.size / 1000), ' kB, ', file.lastModifiedDate.toLocaleDateString(), ')');
         output.push('<br/>');
         debug('add ' + file.size);
         totalFileLength += file.size;
     }
     document.getElementById('selectedFiles').innerHTML = output.join('');
 }

 //This will continueously update the progress bar
 function onUploadProgress(e) {
     if (e.lengthComputable) {
         var percentComplete = parseInt((e.loaded + totalUploaded) * 100 / totalFileLength);
         var bar = document.getElementById('bar');
         bar.style.width = percentComplete + '%';
         bar.innerHTML = percentComplete + ' % completado';
     }
 }

 //the Ouchhh !! moments will be captured here
 function onUploadFailed(e) {
     document.getElementById("upload_res").innerHTML = "Error al subir los archivos";
     document.getElementById("upload_res").className = "alert alert-danger";
 }

 //Pick the next file in queue and upload it to remote server
 function uploadNext() {
     var xhr = new XMLHttpRequest();
     var fd = new FormData();
     var file = document.getElementById('files').files[filesUploaded];
     fd.append("multipartFile", file);
     fd.append("loteNombre", loteName);
     xhr.upload.addEventListener("progress", onUploadProgress, false);
     xhr.addEventListener("load", onUploadComplete, false);
     xhr.addEventListener("error", onUploadFailed, false);
     xhr.onreadystatechange = function() {
         if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
             var texto = xhr.responseText.substring(2);
             document.getElementById("upload_res").innerHTML = texto;
             if (xhr.responseText[0] == 's') {
                 document.getElementById("upload_res").className = "alert alert-success";
             } else {
                 var barraProgreso = document.getElementById("progreso");
                 barraProgreso.classList.add("hidden");
                 document.getElementById("upload_res").className = "alert alert-danger";
             }

         }
     }
     xhr.open("POST", "upload");
     xhr.send(fd);
 }

 //Let's begin the upload process
 function startUpload() {
     totalUploaded = 0;
     loteName = document.getElementById('loteNombre').value;
     if (loteName.length > 5 && loteName.match("^[ a-zA-Z0-9]*$")) {
         if (fileCount > 0) {
             var barraProgreso = document.getElementById("progreso");
             barraProgreso.classList.remove("hidden");
             filesUploaded = 0;
             uploadNext();
         } else {
             document.getElementById("upload_res").innerHTML = "No hay archivos para subir";
             document.getElementById("upload_res").className = "alert alert-danger";
         }
     } else {
         document.getElementById("upload_res").innerHTML = "El nombre del lote debe ser alfanumérico y tener al menos 5 caracteres";
         document.getElementById("upload_res").className = "alert alert-danger";
     }
 }

 //Event listeners for button clicks
 window.onload = function() {
     document.getElementById('files').addEventListener('change', onFileSelect, false);
     document.getElementById('uploadButton').addEventListener('click', startUpload, false);
 }