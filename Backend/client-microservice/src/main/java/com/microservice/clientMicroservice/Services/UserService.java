package com.microservice.clientMicroservice.Services;

import com.microservice.clientMicroservice.DTOS.UserRequestDataDTO;
import com.microservice.clientMicroservice.DTOS.UserRequestDataResponseDTO;
import com.microservice.clientMicroservice.Entities.User;
import com.microservice.clientMicroservice.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public List<User> getUser(){
        return userRepository.findAll();
    }

    public UserRequestDataResponseDTO getUserByData(UserRequestDataDTO userRequestDataDTO){
        String FirstName = userRequestDataDTO.getFirstName();
        String LastName = userRequestDataDTO.getLastName();
        String Rut = userRequestDataDTO.getRut();
        Optional<User> getterUser = userRepository.findByFirstNameAndLastNameAndRut(FirstName, LastName, Rut);

        if(getterUser.isPresent()){
            UserRequestDataResponseDTO userRequestDataResponse = UserRequestDataResponseDTO.builder()
                    .id(getterUser.get().getId())
                    .firstName(getterUser.get().getFirstName())
                    .lastName(getterUser.get().getLastName())
                    .build();

            return userRequestDataResponse;
        }
        return null;
    }


//    Feign services
    public Optional<User> findUserById(UUID id){
        return  userRepository.findById(id);
    }
}
