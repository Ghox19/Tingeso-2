package com.microservice.clientMicroservice.Controllers;

import com.microservice.clientMicroservice.DTOS.UserRequestDataDTO;
import com.microservice.clientMicroservice.DTOS.UserRequestDataResponseDTO;
import com.microservice.clientMicroservice.Entities.User;
import com.microservice.clientMicroservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/post")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getUser();
    }

    @PostMapping("/get")
    public UserRequestDataResponseDTO getUserByUserData(@RequestBody UserRequestDataDTO userRequestDataForm) {
        return userService.getUserByData(userRequestDataForm);
    }

    //    Feing endpoints

    @GetMapping("/getById/{id}")
    public Optional<User> findUserById(@PathVariable UUID id) {
        return userService.findUserById(id);
    }
}
