package org.shipstone.spring.ws.model;

import javax.validation.constraints.NotNull;

/**
 * @author François Robert
 */
public class UserModel {

  @NotNull
  private String login;

  private String password;

  private String lastname;

  private String firstname;

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
