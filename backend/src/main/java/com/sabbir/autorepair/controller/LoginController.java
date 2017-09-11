package com.sabbir.autorepair.controller;

import com.sabbir.autorepair.entity.UserWithPassword;
import com.sabbir.autorepair.model.User;
import com.sabbir.autorepair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registeruser", method = RequestMethod.POST)
    public ResponseEntity<User> registerUser(@RequestBody UserWithPassword userWithPassword) {
        User existingUser = userService.getUserByUsername(userWithPassword.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        try {
            User newUser = userService.createUser(userWithPassword);
            return new ResponseEntity<User>(newUser, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<User> loginUser(@RequestBody UserWithPassword userWithPassword, Principal principal) {
        User existingUser = userService.getUserByUsername(userWithPassword.getUsername());
        User currentUser = userService.getUserByUsername(principal.getName());
        if (existingUser == null || existingUser.getId() != currentUser.getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            return new ResponseEntity<User>(existingUser, HttpStatus.OK);
        }
    }
}
