package tec.psa.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
 
import tec.psa.model.UploadedFile;
 
@Controller
public class UploadController {
    
	@Value("${upload.lote.path}")
    private String loteDir;
	
    @RequestMapping(value = "/upload", method = RequestMethod.POST) 
    public void saveFile(HttpServletRequest servletRequest,
            @ModelAttribute UploadedFile uploadedFile,
            BindingResult bindingResult, Model model) {
 
        MultipartFile multipartFile = uploadedFile.getMultipartFile();
        String fileName = multipartFile.getOriginalFilename();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uniqueDirLote = loteDir + auth.getName() + "/" + getCurrentTimeStamp() + "/";
        
        try {
        	File lote = new File(uniqueDirLote);
        	lote.mkdirs();
        	File file = new File(uniqueDirLote, fileName);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    @RequestMapping(value = "/dashboard")
    public String inputProduct(Model model) {
        return "upload";
    }
    
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new Date());
    }
    
}