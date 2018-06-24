package de.hohenheim.soprademo.security;

import de.hohenheim.soprademo.model.Role;
import de.hohenheim.soprademo.model.User;
import de.hohenheim.soprademo.iliasConnectors.IliasLoginService;
import de.hohenheim.soprademo.service.RoleService;
import de.hohenheim.soprademo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Eigene Implementation der Login-Methode via ILIAS.
 */
@Component
public class IliasAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private IliasLoginService iliasLoginService;

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    String sessionId = iliasLoginService.login(username, password);

    if (Objects.nonNull(sessionId)) {
      logger.info("Login erfolgreich");
      // versuche User in eigener Datenbank zu finden
      User user = userService.getUserByUsername(username);
      if (Objects.nonNull(user)) {
        // user existiert
        logger.info("User existiert bereits in lokaler Datenbank");
        user.setIliasSessionId(sessionId);
        userService.saveUser(user);
      } else {
        // user existiert noch nicht, lege Basisdaten an
        logger.info("User existiert noch nicht in lokaler Datenbank - erstelle neuen User");
        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleService.getRoleByName("ROLE_USER"));
        user.setRoles(userRoles);

        // speichere ILIAS SessionId. Diese wird für weitere SOAP-Requests benötigt.
        // siehe: https://www.ilias.de/test52/webservice/soap/server.php
        user.setIliasSessionId(sessionId);
        userService.saveUser(user);
      }

      // baue User-Objekt für die Spring Security Authentifizierung zusammen
      User userLogin = userService.getUserByUsername(username);
      Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
      grantedAuthorities.addAll(userService.getUserAuthorities(userLogin.getRoles()));

      UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
          username, passwordEncoder.encode(password), grantedAuthorities);
      return upat;

    } else {
      // falls Anmeldung auch ohne ILIAS-Account möglich kann hier die entsprechende Logik eingefügt werden
    }

    return null;


  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
