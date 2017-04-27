package tec.psa.controller;

import tec.psa.model.User;
import tec.psa.service.SecurityService;
import tec.psa.service.UserService;
import tec.psa.validator.UserValidator;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

	@Value("${upload.lote.path}")
    private String loteDir;
    
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("user") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);
        
        // Crear carpeta de almacenamiento para el usuario (temporal)
        new File(loteDir + userForm.getUsername()).mkdirs();
        
        securityService.autologin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/upload";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "login";
        }

        securityService.autologin(userForm.getUsername(), userForm.getPassword());

        return "upload";
    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload(Model model) {
        return "upload";
    }
}
