package de.hohenheim.soprademo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private IliasAuthenticationProvider iliasAuthenticationProvider;

  @Autowired
  private IliasLogoutSuccessHandler iliasLogoutSuccessHandler;


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
        authorizeRequests()
        // alle Requests die ohne Login erreichbar sind
        .antMatchers("/login", "/register", "/console/**").permitAll()
        // definiere alle URLs die nur für eine bestimmte Rolle zugänglich sind
        // Achtung: Spring Security fügt automatisch das Prefix "ROLE_" für die Überprüfung ein. Daher verwenden wir
        // hier nicht "ROLE_ADMIN", wie bspw. im DataLoader angegeben.
        .antMatchers("/admin/**").hasRole("ADMIN")
        // alle weiteren Requests erfordern Authentifizierung
        .anyRequest().authenticated()
        // füge CSRF token ein, welches evtl. für AJAX-requests benötigt wird
        .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .ignoringAntMatchers("/console/**")
        // Request zum Aufruf der Login-Seite
        .and().formLogin().loginPage("/login").failureUrl("/login?error=true").permitAll()
        .defaultSuccessUrl("/")
        .usernameParameter("username")
        .passwordParameter("password")
        // jeder kann sich ausloggen
        .and().logout().permitAll()
        // logout via /logout Request möglich
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login?logout")
        .logoutSuccessHandler(iliasLogoutSuccessHandler);

    // Deaktiviert header security. Ermöglicht Nutzung der H2 Console.
    http.headers().frameOptions().sameOrigin().disable();
  }

  /**
   * Verwende eigene Authentifizierungsmethode.
   *
   * @param auth the authentication-builder.
   * @throws Exception exception.
   */
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(iliasAuthenticationProvider);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }

}
