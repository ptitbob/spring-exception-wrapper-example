package org.shipstone.spring.ws.error.exception;

import org.shipstone.spring.model.User;

/**
 * @author François Robert
 */
public class IncoherentResourceIdFormException extends Exception {
  private final Class<User> resourceClass;
  private final Long pathResourceId;
  private final Long formResourceId;

  public IncoherentResourceIdFormException(Long pathResourceId, Long formResourceId, Class<User> resourceClass) {
    super(String.format("Ressource %s - incohérence entre id (path - %d) et id (formulaire - %d)", resourceClass.getSimpleName(), pathResourceId, formResourceId));
    this.resourceClass = resourceClass;
    this.pathResourceId = pathResourceId;
    this.formResourceId = formResourceId;
  }

  public Class<User> getResourceClass() {
    return resourceClass;
  }

  public Long getPathResourceId() {
    return pathResourceId;
  }

  public Long getFormResourceId() {
    return formResourceId;
  }
}
