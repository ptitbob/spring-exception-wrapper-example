package org.shipstone.spring.ws;

import org.shipstone.spring.model.User;
import org.shipstone.spring.services.UserService;
import org.shipstone.spring.services.UserServiceImpl;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.ws.error.exception.IncoherentResourceIdFormException;
import org.shipstone.spring.ws.error.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.shipstone.spring.ws.error.wrapper.ExceptionWrapperUtils.USER_ERROR_CODE_PREFIXE;

/**
 * @author François Robert
 */
@RestController
@RequestMapping("users")
public class UserEndpoint {

  private final static Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

  private final UserService userService;

  @Autowired
  public UserEndpoint(UserServiceImpl userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<User> getUserList() {
    return new ArrayList<>();
  }

  @GetMapping("{userId:[0-9]*}")
  public User getUserById(
      @PathVariable("userId") Long userId
  ) throws EntityNotFoundException {
    User user = userService.getUserById(userId);
    return user;
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @Valid @ModelAttribute("user") UserModel userModel,
      UriComponentsBuilder uriComponentsBuilder
  ) throws UserCreationException {
    User user = userService.createUser(userModel);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(
        uriComponentsBuilder.path(
            "users/{id}"
        ).buildAndExpand(
            user.getId()
        ).toUri()
    );
    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping("{userId:[0-9]*}")
  public ResponseEntity<User> updateUser(
      @PathVariable("userId") Long userId,
      @Valid @ModelAttribute("user") UserModel userModel
  ) throws IncoherentResourceIdFormException, UpdateUserException {
    if (Objects.equals(userId, userModel.getUserId())) {
      userService.updateUser(userModel);
    } else {
      throw new IncoherentResourceIdFormException(userId, userModel.getUserId(), User.class);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @ExceptionHandler(UpdateUserException.class)
  public ResponseEntity<ErrorMessage> reflectiveUpdateUserException(UpdateUserException e) {
    LOGGER.warn("Erreur de mise à jour du user {} - message : {}", e.getUserId(), e.getMessage());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Exception : ", e);
    }
    return new ResponseEntity<>(new ErrorMessage(USER_ERROR_CODE_PREFIXE.concat("01234"), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
