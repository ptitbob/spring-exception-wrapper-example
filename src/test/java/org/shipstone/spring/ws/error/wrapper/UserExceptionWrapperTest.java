package org.shipstone.spring.ws.error.wrapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.shipstone.spring.model.User;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.ws.UserFactory;
import org.shipstone.spring.ws.error.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE;
import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.USER_ERROR_CODE_PREFIXE;

/**
 * @author François Robert
 */
@RunWith(MockitoJUnitRunner.class)
public class UserExceptionWrapperTest implements HttpServletRequestFactory, UserFactory {

  private UserExceptionWrapper userExceptionWrapper;

  @Before
  public void setUp() throws Exception {
    this.userExceptionWrapper = new UserExceptionWrapper();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void reflectedUserCreationException() throws Exception {
    UserCreationException userCreationException = new UserCreationException(getUser(1L, "toto", "firstname", "lastname"));
    ResponseEntity<ErrorMessage> errorMessageResponseEntity = userExceptionWrapper.reflectedUserCreationException(getHttpServletRequest(), userCreationException);
    assertNotNull(errorMessageResponseEntity);
    assertEquals(HttpStatus.NOT_ACCEPTABLE, errorMessageResponseEntity.getStatusCode());
    assertNotNull(errorMessageResponseEntity.getBody());
    assertTrue(ErrorMessage.class.isAssignableFrom(errorMessageResponseEntity.getBody().getClass()));
    ErrorMessage errorMessage = errorMessageResponseEntity.getBody();
    assertEquals(USER_ERROR_CODE_PREFIXE.concat("09876"), errorMessage.getCode());
    assertEquals(userCreationException.getMessage(), errorMessage.getMessage());
  }

  @Test
  public void reflectiveUpdateUserException() throws Exception {
    EntityNotFoundException entityNotFoundException = new EntityNotFoundException(User.class, 1L);
    UpdateUserException updateUserException = new UpdateUserException("Message", 1L, entityNotFoundException);
    ResponseEntity<ErrorMessage> errorMessageResponseEntity = userExceptionWrapper.reflectiveUpdateUserException(updateUserException);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorMessageResponseEntity.getStatusCode());
    assertNotNull(errorMessageResponseEntity.getBody());
    assertTrue(ErrorMessage.class.isAssignableFrom(errorMessageResponseEntity.getBody().getClass()));
    ErrorMessage errorMessage = errorMessageResponseEntity.getBody();
    assertEquals(USER_ERROR_CODE_PREFIXE.concat("01234"), errorMessage.getCode());
    assertEquals(updateUserException.getMessage(), errorMessage.getMessage());
  }

}