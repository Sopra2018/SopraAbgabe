package de.hohenheim.soprademo.service;

import de.hohenheim.soprademo.model.Role;
import de.hohenheim.soprademo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse kann zum Aufsetzen von Testdaten für die Datenbank verwendet werden. Die Klasse wird immer dann
 * ausgeführt, wenn der Spring Kontext initialisiert wurde, d.h. wenn Sie Ihren Server (neu-)starten.
 */
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    logger.info("Erstelle Testdaten...");
    logger.debug("DEBUG");

    Role userRole = new Role("ROLE_USER");
    Role adminRole = new Role("ROLE_ADMIN");
    roleService.saveRole(userRole);
    roleService.saveRole(adminRole);

    Set<Role> userRoles = new HashSet<>();
    userRoles.add(userRole);

    Set<Role> adminRoles = new HashSet<>();
    adminRoles.add(adminRole);

    /*
    User student = new User();
    student.setUsername("max");
    student.setPassword(passwordEncoder.encode("password"));
    student.setEmail("example@uni-hohenheim.de");
    student.setRoles(userRoles);
    userService.saveUser(student);

    User admin = new User();
    admin.setUsername("admin");
    admin.setPassword(passwordEncoder.encode("admin"));
    admin.setEmail("admin@uni-hohenheim.de");
    admin.setRoles(adminRoles);
    userService.saveUser(admin);
    */

  }
}
