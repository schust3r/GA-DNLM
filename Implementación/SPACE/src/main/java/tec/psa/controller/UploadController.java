package tec.psa.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tec.psa.model.Imagen;
import tec.psa.model.UploadedFile;
import tec.psa.segmentacion.imagenes.ImageProcessor;

@Controller
public class UploadController {

	@Value("${upload.lote.path}")
	private String loteDir;
	
	private FileWriter pw;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void saveFile(HttpServletRequest servletRequest, @ModelAttribute UploadedFile uploadedFile,
			@RequestParam("loteNombre") String nombreLote, BindingResult bindingResult, Model model) {

		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		String fileName = multipartFile.getOriginalFilename();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String uniqueDirLote = loteDir + auth.getName() + "/" + nombreLote + "/";

		try {
			File rutaLote = new File(uniqueDirLote);
			rutaLote.mkdirs();

			File file = new File(uniqueDirLote, fileName);
			multipartFile.transferTo(file);

			ImageProcessor ip = new ImageProcessor();
			Imagen img = ip.procesarImagen(file.getAbsolutePath());
			img.setNombreImagen(fileName);
			
			File csv = new File(uniqueDirLote, "1resultados.csv");
			pw = new FileWriter(csv, true);
			construirLineaImagen(img);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void construirLineaImagen(Imagen img) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(img.getNombreImagen());
			sb.append(",");
			sb.append(img.getTao());
			sb.append(",");
			sb.append(img.getNumeroCelulas());
			sb.append(",");
			sb.append(img.getTiempoProcesamiento());
			sb.append("\n");
			pw.write(sb.toString());	
			pw.flush();
			pw.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping(value = "/dashboard")
	public String inputProduct(Model model) {
		return "upload";
	}

}