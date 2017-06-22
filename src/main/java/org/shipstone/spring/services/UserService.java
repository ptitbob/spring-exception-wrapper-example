package org.shipstone.spring.services;

import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author François Robert
 */
@Service
public class UserService {

  public User getUserById(Long userId) throws EntityNotFoundException {
    throw new EntityNotFoundException(User.class, userId);
  }

  public User createUser(UserModel userModel) throws UserCreationException {
    throw new UserCreationException(null);
  }

  public void updateUser(UserModel userModel) throws UpdateUserException {
    User user = null;
    try {
      if ((user = getUserById(userModel.getUserId())) != null) {
        // faire quelque chose
      }
    } catch (EntityNotFoundException entityNotFound) {
      throw new UpdateUserException("Mise à jour impossible", userModel.getUserId(), entityNotFound);
    }
  }
}
