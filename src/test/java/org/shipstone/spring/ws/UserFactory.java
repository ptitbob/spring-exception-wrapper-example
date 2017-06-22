package org.shipstone.spring.ws;

import org.shipstone.spring.model.User;

/**
 * @author François Robert
 */
public interface UserFactory {

  default User getUser(long userId, String login, String firstname, String lastname) {
    User user = new User();
    user.setId(userId);
    user.setLogin(login);
    user.setFirstname(firstname);
    user.setLastname(lastname);
    return user;
  }


}
