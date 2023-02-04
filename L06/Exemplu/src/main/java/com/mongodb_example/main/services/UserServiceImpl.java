package com.mongodb_example.main.services;

import org.springframework.dao.DuplicateKeyException;
import com.mongodb_example.main.exceptions.UserExistsException;
import com.mongodb_example.main.exceptions.UserMissingFieldException;
import com.mongodb_example.main.exceptions.UserNotFoundException;
import com.mongodb_example.main.repositories.SteamUserRepository;
import com.mongodb_example.main.services.interfaces.UserService;
import com.mongodb_example.main.steam_users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SteamUserRepository userRepository;
    @Override
    public String getIdByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id){
        User user =  userRepository.findUserById(id);
        if(user == null)
            throw new UserNotFoundException(id);
        return user;
    }

    @Override
    public User getUserByUsername(String name){
        User user = userRepository.findUserByUsername(name);
        if(user == null)
            throw new UserNotFoundException(name);
        return user;
    }

    @Override
    public ResponseEntity<User> createAUser(User user) throws UserExistsException, UserMissingFieldException{
        if(user.getUsername() == null)
            throw new UserMissingFieldException();
        try {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch(DuplicateKeyException ex){
            throw new UserExistsException(user.getId());
        }
    }

    @Override
    public ResponseEntity<User> updateUser(User newUser, String id)
            throws UserMissingFieldException, UserNotFoundException, UserExistsException{

        if(newUser.getUsername() == null)
            throw new UserMissingFieldException();

        try{
            User user = userRepository.findUserById(id);
            if(user == null)
                throw new UserNotFoundException(id);

            // Update an entry
            user.setAchievements(newUser.getAchievements());
            user.setGames(newUser.getGames());
            user.setUsername(newUser.getUsername());
            user.setJoin_year(newUser.getJoin_year());

            try{
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } catch(Exception exinner){
                throw new UserExistsException(user.getId());
            }

        } catch(UserNotFoundException ex){

            // Create a new entry
            newUser.setId(id);
            try{
                return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
            } catch(Exception exinner){
                System.out.println(ex.toString());
                throw new UserExistsException(newUser.getId());
            }
        }
    }

    @Override
    public ResponseEntity<User> updatePartiallyUser(User newUser, String id)
            throws UserNotFoundException, UserExistsException{

        User user = userRepository.findUserById(id);
        if(user == null)
            throw new UserNotFoundException(id);

        if(newUser.getAchievements() != null)
            user.setAchievements(newUser.getAchievements());
        if(newUser.getGames() != null)
            user.setGames(newUser.getGames());
        if(newUser.getUsername() != null)
            user.setUsername(newUser.getUsername());
        if(newUser.getJoin_year() != null)
            user.setJoin_year(newUser.getJoin_year());

        try{
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch(Exception exinner){
            throw new UserExistsException(user.getId());
        }
    }

    @Override
    public User removeUser(String id) throws UserNotFoundException {
        User user = userRepository.findUserById(id);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return user;
    }
}
