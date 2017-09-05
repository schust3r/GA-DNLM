package tec.psa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tec.psa.model.User;
import tec.psa.service.SecurityService;
import tec.psa.service.UserService;
import tec.psa.validator.UserValidator;

@Controller
public class LoginController {
  @Autowired
  private UserService userService;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UserValidator userValidator;

  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public String registration(Model model) {
    model.addAttribute("user", new User());

    return "registration";
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  public String registration(@ModelAttribute("user") User userForm, 
      BindingResult bindingResult, Model model) {

    userValidator.validate(userForm, bindingResult);

    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "No se pudo crear la cuenta.");
      return "registration";
    } else {
      model.addAttribute("success", "Se ha creado la cuenta de usuario.");
    }

    userService.save(userForm);

    return "login";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(@ModelAttribute("user") User userForm, BindingResult bindingResult, Model model) {

    userValidator.validate(userForm, bindingResult);

    if (bindingResult.hasErrors()) {
      return "login";
    }

    securityService.autologin(userForm.getUsername(), userForm.getPassword());

    return "home";
  }

  @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
  public String login(Model model, String error, String logout) {
    if (error != null) {
      model.addAttribute("error", "Usuario o contraseña inválida.");
    }
    if (logout != null) {
      model.addAttribute("message", "Ha salido del sistema con éxito.");
    }
    return "login";
  }

  @RequestMapping(value = "/home", method = RequestMethod.GET)
  public String dashboard(@ModelAttribute("user") User userForm, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("usuario", auth.getName());

    return "home";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logoutPage(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      auth.setAuthenticated(false);
    }
    return "redirect:/login";
  }

}
