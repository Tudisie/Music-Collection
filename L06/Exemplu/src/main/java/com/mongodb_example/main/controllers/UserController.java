package com.mongodb_example.main.controllers;

import com.mongodb_example.main.exceptions.UserExistsException;
import com.mongodb_example.main.exceptions.UserMissingFieldException;
import com.mongodb_example.main.exceptions.UserNotFoundException;
import com.mongodb_example.main.services.interfaces.UserService;
import com.mongodb_example.main.steam_users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/steam")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @GetMapping("/users/name:{username}")
    public User getUserByName(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) throws UserExistsException, UserMissingFieldException {
        return userService.createAUser(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable String id)
            throws UserMissingFieldException, UserNotFoundException, UserExistsException{
        return userService.updateUser(newUser, id);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updatePartiallyUser(@RequestBody User newUser, @PathVariable String id)
            throws UserNotFoundException, UserExistsException{
        return userService.updatePartiallyUser(newUser, id);
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable String id){
        return userService.removeUser(id);
    }

}
