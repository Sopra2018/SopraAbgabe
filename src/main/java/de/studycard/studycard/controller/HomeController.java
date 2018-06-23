package de.studycard.studycard.controller;

import de.studycard.studycard.Validator.UserValidator;
import de.studycard.studycard.model.User;
import de.studycard.studycard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.xml.validation.Validator;

@org.springframework.stereotype.Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @ModelAttribute("user")
    public User userErstellen() {
        return new User();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @GetMapping("/registrationIsComplete")
    public String getregistrationComplete() {
        return "registrationComplete";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHome() {
        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(User user, Model model) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registrieren(Model model, @ModelAttribute("user") User user, BindingResult result) {

        try {
            userValidator.validate(user, result);
            if (result.hasErrors()) {

                return "register";

            } else {
                userService.saveUser(user);
                return "registrationComplete";
            }

        } catch (NullPointerException e) {

            return "register";
        }


    }
}