package de.studycard.studycard.Validator;

import de.studycard.studycard.model.User;
import de.studycard.studycard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    //    public boolean emailInDatabase(String email){
//        if(userService.getUserByEmail(email)!=null){
//            return true;
//        }
//    }


    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Bitte geben sie einen nichtleeren Nutzernamen ein");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Bitte geben sie ein nichtleeres Passwort ein!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Bitte geben sie eine gueltige E-Mail-Adresse ein!");
        if (userService.getUserByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "email.inUse");
        } else if (userService.getUserByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "username.inUse");
        }
        if (user.getUsername().contains(" ")) {
            errors.rejectValue("username", "username.contains.whiteSpace");
        }
        if (user.getPassword().contains(" ")) {
            errors.rejectValue("password", "password.contains.whiteSpace");
        }
        if (user.getEmail().contains(" ")) {
            errors.rejectValue("email", "email.contains.whiteSpace");
        }
    }
}



