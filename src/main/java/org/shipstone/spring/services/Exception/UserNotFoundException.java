package org.shipstone.spring.services.Exception;

/**
 * @author François Robert
 */
public class UserNotFoundException extends Exception {

  public UserNotFoundException(Long userId) {
    super(
        String.format("L'utilisateur d'Id %d n'a pu être localisé", userId)
    );
  }

}
