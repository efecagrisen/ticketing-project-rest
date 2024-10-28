package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Manager","Admin","ADMIN","ROLE_ADMIN"})
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOList = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users are retrieved successfully",userDTOList, HttpStatus.OK));
    }

    @GetMapping("/{username}")
    @RolesAllowed({"Manager","Admin"})
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable ("username") String username){
        return ResponseEntity.ok(new ResponseWrapper("User with username : " + username + " retrieved successfully",userService.findByUserName(username), HttpStatus.FOUND));
    }

    @PostMapping
    @RolesAllowed({"Admin","ROLE_ADMIN"})
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper("New User created",HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
        userService.update(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User updated",HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable ("username") String username){
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("User Deleted",HttpStatus.OK));

    }



}
