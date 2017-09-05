package tec.psa.model;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile {	  
    
    private MultipartFile multipartFile;
    
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }
    
    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
    
}