package de.hohenheim.soprademo.controller;

import de.hohenheim.soprademo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @Autowired
  private UserService userService;

  /**
   * Request-Mapping für die Startseite.
   * @return home-Seite.
   */
  @GetMapping("/")
  public String showHome(Model model) {
    model.addAttribute("allUsers", userService.getAllUsers());
    return "home";
  }

  /**
   * Request-Mapping für die Login-Seite.
   *
   * @return login-Seite.
   */
  @GetMapping("/login")
  public String showLogin() {
    return "login";
  }

}
