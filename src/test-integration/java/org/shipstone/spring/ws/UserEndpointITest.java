package org.shipstone.spring.ws;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.UserServiceImpl;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author François Robert
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserEndpointITest implements UserFactory {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @MockBean
  private UserServiceImpl userService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        //.standaloneSetup(userEndpoint, commonExceptionWrapper, userExceptionWrapper)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();
  }

  @Test
  public void it_return_resource_when_exist_and_OK() throws Exception {
    try {
      User user = getUser(1L, "login", "firstname", "lastname");
      Mockito.when(userService.getUserById(1L)).thenReturn(user);
    } catch (EntityNotFoundException e) {
    }
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/1").accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.login").exists()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.login", Matchers.is("login"))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("firstname"))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("lastname"))
    );
  }

  @Test
  public void it_return_error_message_and_404_when_user_not_exist() throws Exception {
    try {
      Mockito.when(userService.getUserById(2L)).thenCallRealMethod();
    } catch (EntityNotFoundException e) {
    }
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/2").accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE.concat("404")))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.message", Matchers.is(String.format("La ressource %s d'Id %d n'a pu être localisé", User.class.getSimpleName(), 2)))
    );
  }

  @Test
  public void createUser_return_response_with_no_body_and_location() throws Exception {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("toto");
    // Il faut penser a implementer le equals et le hashcode
    Mockito.when(userService.createUser(userModel)).thenReturn(getUser(1L, "toto", "firstname", "lastname"));
    mockMvc.perform(
        MockMvcRequestBuilders.post("/users")
            .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("firstname", "firstname")
            .param("lastname", "lastname")
            .param("login", "toto")
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.CREATED.value())
    ).andExpect(
        MockMvcResultMatchers.header().string("location", "http://localhost/users/1")
    ).andExpect(
        MockMvcResultMatchers.content().string("")
    );
  }

  @Test
  public void createUser_return_errorMessage_when_exception() throws Exception {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("error");
    Mockito.when(userService.createUser(userModel)).thenCallRealMethod();
    mockMvc.perform(
        MockMvcRequestBuilders.post("/users")
            .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("firstname", "firstname")
            .param("lastname", "lastname")
            .param("login", "error")
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.NOT_ACCEPTABLE.value())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(ExceptionWrapperUtils.USER_ERROR_CODE_PREFIXE.concat("09876")))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Impossible de créer l'utilisateur de valeur [NULL]"))
    );
  }

  @Test
  public void updateUser_return_ok_when_updated_success() throws Exception, UpdateUserException {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("toto");
    userModel.setUserId(2L);
    User user = getUser(2L, "login", "firstname", "lastname");
    Mockito.when(userService.getUserById(2L)).thenReturn(user);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/2")
            .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("firstname", "firstname")
            .param("lastname", "lastname")
            .param("login", "toto")
            .param("userId", "2")
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.OK.value())
    ).andExpect(
        MockMvcResultMatchers.content().string("")
    );
  }

  @Test
  public void updateUser_return_errorMessage_when_user_notExist() throws Exception, UpdateUserException {
    UserModel userModel = new UserModel();
    userModel.setFirstname("firstname");
    userModel.setLastname("lastname");
    userModel.setLogin("error");
    userModel.setUserId(3L);
    Mockito.doCallRealMethod().when(userService).updateUser(userModel);
    Mockito.when(userService.getUserById(3L)).thenCallRealMethod();
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/3")
            .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("firstname", "firstname")
            .param("lastname", "lastname")
            .param("login", "error")
            .param("userId", "3")
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(ExceptionWrapperUtils.USER_ERROR_CODE_PREFIXE.concat("01234")))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Erreur de mise à jour utilisateur : Mise à jour impossible - user : 3"))
    );
  }

  @Test
  public void updateUser_return_errorMessage_when_path_userId_different_form_userId() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/4")
            .content(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("firstname", "firstname")
            .param("lastname", "lastname")
            .param("login", "error")
            .param("userId", "3")
    ).andExpect(
        MockMvcResultMatchers.status().is(HttpStatus.PRECONDITION_FAILED.value())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE.concat("406")))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Ressource User - incohérence entre id (path - 4) et id (formulaire - 3)"))
    );
  }

}