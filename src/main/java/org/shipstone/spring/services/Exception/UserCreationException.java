package org.shipstone.spring.services.Exception;

import org.shipstone.spring.model.User;

/**
 * @author François Robert
 */
public class UserCreationException extends Exception {

  public UserCreationException(User user) {
    super(
        String.format("Impossible de créer l'utilisateur de valeur %s",
            user == null ? "[NULL]" : user.toString()
        )
    );
  }

}
