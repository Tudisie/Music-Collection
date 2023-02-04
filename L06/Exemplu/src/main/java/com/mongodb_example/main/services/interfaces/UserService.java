package com.mongodb_example.main.services.interfaces;

import com.mongodb_example.main.exceptions.UserExistsException;
import com.mongodb_example.main.exceptions.UserMissingFieldException;
import com.mongodb_example.main.exceptions.UserNotFoundException;
import com.mongodb_example.main.steam_users.models.Game;
import com.mongodb_example.main.steam_users.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    String getIdByUsername(String username);

    List<User> getAllUsers();

    User getUserById(String id);

    User getUserByUsername(String name);

    ResponseEntity<User> createAUser(User user) throws UserExistsException, UserMissingFieldException;

    ResponseEntity<User> updateUser(User newUser, String id)
            throws UserMissingFieldException, UserNotFoundException, UserExistsException;

    ResponseEntity<User> updatePartiallyUser(User newUser, String id)
            throws  UserNotFoundException, UserExistsException;

    User removeUser(String id) throws UserNotFoundException;
}
