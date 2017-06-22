package org.shipstone.spring.services.exception;

/**
 * @author François Robert
 */
public class EntityNotFoundException extends Exception {

  private final Long userId;
  private final Class<?> aClass;

  /**
   * constructeir
   * @param aClass classe de l'entité
   * @param entityId Id(PK) de l'entité
   */
  public EntityNotFoundException(Class<?> aClass, Long entityId) {
    super(
        String.format("La ressource %s d'Id %d n'a pu être localisé", aClass.getSimpleName(), entityId)
    );
    this.userId = entityId;
    this.aClass = aClass;
  }

  public Long getUserId() {
    return userId;
  }

  public Class<?> getaClass() {
    return aClass;
  }

}
