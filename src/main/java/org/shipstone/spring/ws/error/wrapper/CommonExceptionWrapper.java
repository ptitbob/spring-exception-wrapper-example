package org.shipstone.spring.ws.error.wrapper;

import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.ws.error.exception.IncoherentResourceIdFormException;
import org.shipstone.spring.ws.error.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE;

/**
 * @author François Robert
 */
@ControllerAdvice
public class CommonExceptionWrapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionWrapper.class);

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMessage> reflectedEntityNotFoundException(
      HttpServletRequest httpServletRequest,
      EntityNotFoundException e
  ) {
    ErrorMessage errorMessage = new ErrorMessage(
        COMMON_ERROR_CODE_PREFIXE.concat("404"),
        e.getMessage()
    );
    LOGGER.warn("Erreur de création de ressource {} - id : {} - URL : {}", e.getaClass().getCanonicalName(), e.getUserId(), httpServletRequest.getRequestURL());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Exception : ", e);
    }
    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IncoherentResourceIdFormException.class)
  public ResponseEntity<ErrorMessage> reflectiveIncoherentResourceIdForm(
      HttpServletRequest httpServletRequest,
      IncoherentResourceIdFormException e
  ) {
    LOGGER.warn("Incohérence d'appel - message : {} - URL : {}", e.getMessage(), httpServletRequest.getRequestURL());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Exception : ", e);
    }
    ErrorMessage errorMessage = new ErrorMessage(COMMON_ERROR_CODE_PREFIXE.concat("406"), e.getMessage());
    return new ResponseEntity<>(errorMessage, HttpStatus.PRECONDITION_FAILED);
  }

}
