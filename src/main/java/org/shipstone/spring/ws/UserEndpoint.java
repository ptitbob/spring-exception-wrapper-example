package org.shipstone.spring.ws;

import org.shipstone.spring.model.User;
import org.shipstone.spring.services.Exception.UserCreationException;
import org.shipstone.spring.services.Exception.UserNotFoundException;
import org.shipstone.spring.ws.model.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author François Robert
 */
@RestController
@RequestMapping("users")
public class UserEndpoint {

  @GetMapping
  public List<User> getUserList() {
    return new ArrayList<>();
  }

  @GetMapping("{userId:[0-9]*}")
  public User getUserById(
      @PathVariable("userId") Long userId
  ) throws UserNotFoundException {
    User user = null;
    if (user == null) {
      throw new UserNotFoundException(userId);
    }
    return user;
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @Valid @ModelAttribute("user") UserModel userModel,
      UriComponentsBuilder uriComponentsBuilder
  ) throws UserCreationException {
    User user = new User();
    user.setId(1L);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(
        uriComponentsBuilder.path(
            "users/{id}"
        ).buildAndExpand(
            user.getId()
        ).toUri()
    );
    if (user.getLogin() == null) {
      // faut bien le faire planter :)
      throw new UserCreationException(null);
    }
    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

}
