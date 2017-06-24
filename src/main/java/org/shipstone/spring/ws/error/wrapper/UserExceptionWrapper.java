package org.shipstone.spring.ws.error.wrapper;

import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.ws.error.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.USER_ERROR_CODE_PREFIXE;

/**
 * @author François Robert
 */
@ControllerAdvice
public class UserExceptionWrapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserExceptionWrapper.class);

  @ExceptionHandler(UserCreationException.class)
  public ResponseEntity<ErrorMessage> reflectedUserCreationException(
      HttpServletRequest httpServletRequest,
      UserCreationException e
  ) {
    ErrorMessage errorMessage = new ErrorMessage(
        USER_ERROR_CODE_PREFIXE.concat("09876"),
        e.getMessage()
    );
    LOGGER.warn("Erreur de création de user - URL : {}", httpServletRequest.getRequestURL());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Exception : ", e);
    }
    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
  }

}
