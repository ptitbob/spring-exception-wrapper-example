package org.shipstone.spring.ws.error.model;

/**
 * @author François Robert
 */
public class ErrorMessage {

  private String code;

  private String message;

  public ErrorMessage(String code) {
    this.code = code;
  }

  public ErrorMessage(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
