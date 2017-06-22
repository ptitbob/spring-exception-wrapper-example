package org.shipstone.spring.ws;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.UserService;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.ws.error.exception.IncoherentResourceIdFormException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.*;

/**
 * @author François Robert
 */
@RunWith(MockitoJUnitRunner.class)
public class UserEndpointTest implements UserFactory {

  private UserEndpoint userEndPoint;
  private UserService userService;

  @Before
  public void setup() {
    this.userEndPoint = new UserEndpoint(getUserServiceMock());
  }

  @After
  public void tearDown() {
  }

  @Test
  public void getUserById_return_user_when_exist() throws Exception {
    User user = getUser(1L, "toto", "firstname", "lastname");
    Mockito.when(getUserServiceMock().getUserById(1L)).thenReturn(user);
    User userFrom = userEndPoint.getUserById(1L);
    assertNotNull(userFrom);
    assertEquals(new Long(1L), userFrom.getId());
  }

  @Test(expected = EntityNotFoundException.class)
  public void getUserById_throw_exception_when_call_real_method() throws Exception {
    Mockito.when(getUserServiceMock().getUserById(1L)).thenCallRealMethod();
    userEndPoint.getUserById(1L);
  }

  @Test
  public void createUser_return_response_with_no_body_and_location() throws Exception {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("toto");
    User user = getUser(1L, "toto", "firstname", "lastname");
    Mockito.when(getUserServiceMock().createUser(userModel)).thenReturn(user);
    ResponseEntity<User> userResponseEntity = userEndPoint.createUser(userModel, UriComponentsBuilder.fromUriString(""));
    assertNotNull(userResponseEntity);
    assertEquals(HttpStatus.CREATED, userResponseEntity.getStatusCode());
    assertNull(userResponseEntity.getBody());
    assertNotNull(userResponseEntity.getHeaders());
    assertTrue(userResponseEntity.getHeaders().containsKey("location"));
    assertEquals("users/1", userResponseEntity.getHeaders().get("location").get(0));
  }

  @Test(expected = UserCreationException.class)
  public void createUser_throw_exception_when_call_real_method() throws Exception {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("toto");
    Mockito.when(getUserServiceMock().createUser(userModel)).thenCallRealMethod();
    ResponseEntity<User> userResponseEntity = userEndPoint.createUser(userModel, UriComponentsBuilder.fromUriString(""));
  }

  @Test
  public void updateUser_well_done_when_all_ok() throws Exception {
    UserModel userModel = getUserModel();
    User user = getUser(1L, "toto", "firstname", "lastname");
    Mockito.when(getUserServiceMock().createUser(userModel)).thenReturn(user);
    try {
      Mockito.doNothing().when(getUserServiceMock()).updateUser(userModel);
      ResponseEntity<User> userResponseEntity = userEndPoint.updateUser(1L, userModel);
      assertNotNull(userResponseEntity);
      assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
      assertNull(userResponseEntity.getBody());
    } catch (UpdateUserException e) {
      fail();
    }
  }

  @Test(expected = IncoherentResourceIdFormException.class)
  public void updateUser_throw_exception_when_path_not_same_with_form() throws Exception {
    UserModel userModel = getUserModel();
    User user = getUser(1L, "toto", "firstname", "lastname");
    Mockito.when(getUserServiceMock().createUser(userModel)).thenReturn(user);
    try {
      Mockito.doNothing().when(getUserServiceMock()).updateUser(userModel);
      ResponseEntity<User> userResponseEntity = userEndPoint.updateUser(2L, userModel);
      assertNotNull(userResponseEntity);
      assertEquals(HttpStatus.OK, userResponseEntity.getStatusCode());
      assertNull(userResponseEntity.getBody());
    } catch (UpdateUserException e) {
      fail();
    }
  }

  @Test(expected = UpdateUserException.class)
  public void updateUser_throw_exception_when_user_not_exist() throws Exception, UpdateUserException {
    UserModel userModel = getUserModel();
    Mockito.when(getUserServiceMock().getUserById(1L)).thenCallRealMethod();
    try {
      Mockito.doCallRealMethod().when(getUserServiceMock()).updateUser(userModel);
      userEndPoint.updateUser(1L, userModel);
      fail();
    } catch (UpdateUserException e) {
      throw e;
    }
  }

  private UserService getUserServiceMock() {
    if (userService == null) {
      userService = Mockito.mock(UserService.class);
    }
    return userService;
  }

  private UserModel getUserModel() {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("toto");
    userModel.setUserId(1L);
    return userModel;
  }

}