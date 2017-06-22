package org.shipstone.spring.ws;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.UserService;
import org.shipstone.spring.services.UserServiceImpl;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.ws.error.wrapper.CommonExceptionWrapper;
import org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils;
import org.shipstone.spring.ws.error.wrapper.UserExceptionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.COMMON_ERROR_CODE_PREFIXE;

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
    )
    ;
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
  public void updateUser() throws Exception {
  }

}