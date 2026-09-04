package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/getAllUsers")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @PostMapping("/createUser")
  public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
  }
}