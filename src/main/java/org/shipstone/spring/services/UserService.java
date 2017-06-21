package org.shipstone.spring.services;

import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.services.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author François Robert
 */
@Service
public class UserService {

  public User getUserById(Long userId) throws UserNotFoundException {
    throw new UserNotFoundException(userId);
  }

  public User createUser(UserModel userModel) throws UserCreationException {
    throw new UserCreationException(null);
  }

}
