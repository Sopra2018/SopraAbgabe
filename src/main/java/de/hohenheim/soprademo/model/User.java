package de.hohenheim.soprademo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class User {

  @Id
  @GeneratedValue
  private Integer id;

  private String username;

  private String email;

  private String password;

  private boolean enabled = true;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  // die SessionId wird für weitere SOAP-Requests benötigt, siehe https://www.ilias.de/test52/webservice/soap/server.php
  private String iliasSessionId;

  // ILIAS user_id wird ebenfalls für manche SOAP-Requests benötigt
  private int iliasUserId;

  /**
   * No-Argument Konstruktor für Hibernate.
   */
  public User() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getIliasSessionId() {
    return iliasSessionId;
  }

  public void setIliasSessionId(String iliasSessionId) {
    this.iliasSessionId = iliasSessionId;
  }

  public int getIliasUserId() {
    return iliasUserId;
  }

  public void setIliasUserId(int iliasUserId) {
    this.iliasUserId = iliasUserId;
  }
}
