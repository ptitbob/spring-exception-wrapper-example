package org.shipstone.spring.services.exception;

/**
 * @author François Robert
 */
public class UpdateUserException extends Throwable {
  private final Long userId;

  public UpdateUserException(String message, Long userId, Exception e) {
    super(String.format("Erreur de mise à jour utilisateur : %s - user : %d", message, userId), e);
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
