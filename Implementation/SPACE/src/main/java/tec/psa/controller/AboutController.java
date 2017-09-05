package tec.psa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController {

  /**
   * Retorna la pagina de Acerca de.
   * 
   * @param model modelo de Spring
   * @return la pagina del acerca de
   */
  @RequestMapping(value = "/about", method = RequestMethod.GET)
  public String dashboard(HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("usuario", auth.getName());
    return "about";
  }
  
}
