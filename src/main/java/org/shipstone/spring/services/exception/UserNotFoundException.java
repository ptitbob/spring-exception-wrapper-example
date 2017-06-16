package org.shipstone.spring.services.exception;

/**
 * @author François Robert
 */
public class UserNotFoundException extends Exception {

  private final Long userId;

  public UserNotFoundException(Long userId) {
    super(
        String.format("L'utilisateur d'Id %d n'a pu être localisé", userId)
    );
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

}
