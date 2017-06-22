package org.shipstone.spring.model;

import javax.validation.constraints.NotNull;

/**
 * @author François Robert
 */
public class UserModel {

  private Long userId;

  @NotNull(message = "Le login est obligatoire")
  private String login;

  private String password;

  private String lastname;

  private String firstname;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
}
