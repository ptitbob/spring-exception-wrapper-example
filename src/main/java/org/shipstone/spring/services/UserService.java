package org.shipstone.spring.services;

import org.shipstone.spring.model.User;
import org.shipstone.spring.model.UserModel;
import org.shipstone.spring.services.exception.EntityNotFoundException;
import org.shipstone.spring.services.exception.UpdateUserException;
import org.shipstone.spring.services.exception.UserCreationException;
import org.springframework.stereotype.Service;

/**
 * @author François Robert
 */
@Service
public interface UserService {

  User getUserById(Long userId) throws EntityNotFoundException;

  User createUser(UserModel userModel) throws UserCreationException;

  void updateUser(UserModel userModel) throws UpdateUserException;

}
