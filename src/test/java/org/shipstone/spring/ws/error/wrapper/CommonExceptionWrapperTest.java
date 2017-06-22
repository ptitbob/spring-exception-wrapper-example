package org.shipstone.spring.ws.error.wrapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.shipstone.spring.model.User;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.ws.error.exception.IncoherentResourceIdFormException;
import org.shipstone.spring.ws.error.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE;

/**
 * @author François Robert
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonExceptionWrapperTest implements HttpServletRequestFactory {

  private CommonExceptionWrapper commonExceptionWrapper;

  @Before
  public void setUp() throws Exception {
    this.commonExceptionWrapper = new CommonExceptionWrapper();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void reflectedUserNotFoundException() throws Exception {
    HttpServletRequest httpServletRequest = getHttpServletRequest();
    EntityNotFoundException entityNotFoundException = new EntityNotFoundException(User.class, 1L);
    ResponseEntity<ErrorMessage> errorMessageResponseEntity = commonExceptionWrapper.reflectedUserNotFoundException(httpServletRequest, entityNotFoundException);
    assertNotNull(errorMessageResponseEntity);
    assertEquals(HttpStatus.NOT_FOUND, errorMessageResponseEntity.getStatusCode());
    assertNotNull(errorMessageResponseEntity.getBody());
    assertTrue(ErrorMessage.class.isAssignableFrom(errorMessageResponseEntity.getBody().getClass()));
    ErrorMessage errorMessage = errorMessageResponseEntity.getBody();
    assertEquals(COMMON_ERROR_CODE_PREFIXE.concat("404"), errorMessage.getCode());
    assertEquals(entityNotFoundException.getMessage(), errorMessage.getMessage());
  }

  @Test
  public void reflectiveIncoherentResourceIdForm() throws Exception {
    IncoherentResourceIdFormException incoherentResourceIdFormException = new IncoherentResourceIdFormException(2L, 1L, User.class);
    ResponseEntity<ErrorMessage> errorMessageResponseEntity = commonExceptionWrapper.reflectiveIncoherentResourceIdForm(getHttpServletRequest(), incoherentResourceIdFormException);
    assertEquals(HttpStatus.PRECONDITION_FAILED, errorMessageResponseEntity.getStatusCode());
    assertNotNull(errorMessageResponseEntity.getBody());
    assertTrue(ErrorMessage.class.isAssignableFrom(errorMessageResponseEntity.getBody().getClass()));
    ErrorMessage errorMessage = errorMessageResponseEntity.getBody();
    assertEquals(COMMON_ERROR_CODE_PREFIXE.concat("406"), errorMessage.getCode());
    assertEquals(incoherentResourceIdFormException.getMessage(), errorMessage.getMessage());
  }

}