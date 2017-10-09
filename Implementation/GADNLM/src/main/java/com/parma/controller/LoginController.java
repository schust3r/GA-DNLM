package com.parma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.parma.model.User;
import com.parma.service.SecurityService;
import com.parma.service.UserService;
import com.parma.validator.UserValidator;

@Controller
public class LoginController {
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private SecurityService securityService;
  
  @Autowired
  private UserValidator userValidator;
  
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String registration(Model model) {
    model.addAttribute("user", new User());

    return "register";
  }
  
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String registration(@ModelAttribute("user") User userForm, 
      BindingResult bindingResult, Model model) {
    
    userValidator.validate(userForm, bindingResult);
    
    if (bindingResult.hasErrors()) {
      model.addAttribute("message", "Unable to create your account.");      
    } else {
      userService.save(userForm);
      model.addAttribute("message", "Your account has been registered.");
      return "login";
    }
    
    return "register";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(@ModelAttribute("user") User userForm, BindingResult bindingResult, Model model) {

    userValidator.validate(userForm, bindingResult);

    if (bindingResult.hasErrors()) {
      return "login";
    }
    securityService.autologin(userForm.getUsername(), userForm.getPassword());
    
    model.addAttribute("username", userForm.getUsername());
    return "calibrate";
  }

  @RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
  public String login(Model model, String error, String logout) {
    if (error != null) {
      model.addAttribute("message", "Invalid username or password.");
    }
    if (logout != null) {
      model.addAttribute("message", "You have logged out successfully.");
    }   
    
    return "login";
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
