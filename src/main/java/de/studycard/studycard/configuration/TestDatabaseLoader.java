package de.studycard.studycard.configuration;

import de.studycard.studycard.model.User;
import de.studycard.studycard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;



    @Override
    public void run(String... args) throws Exception {

        //create User:
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@admin.com");
        admin.setEnabled(true);

        userService.saveUser(admin);

        User max = new User();
        max.setUsername("max");
        max.setPassword("max");
        max.setEmail("Max@Mustermann.de");
        max.setEnabled(true);
        userService.saveUser(max);



    }
}
