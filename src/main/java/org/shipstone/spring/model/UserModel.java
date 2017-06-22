package org.shipstone.spring.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserModel)) return false;
    UserModel userModel = (UserModel) o;
    return Objects.equals(getUserId(), userModel.getUserId()) &&
        Objects.equals(getLogin(), userModel.getLogin()) &&
        Objects.equals(getPassword(), userModel.getPassword()) &&
        Objects.equals(getLastname(), userModel.getLastname()) &&
        Objects.equals(getFirstname(), userModel.getFirstname());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getLogin(), getPassword(), getLastname(), getFirstname());
  }
}
