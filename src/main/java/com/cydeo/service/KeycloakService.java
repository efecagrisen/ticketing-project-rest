package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.core.Response;

public interface KeycloakService {

    void createUser(String username, String email, String password, String firstName, String lastName);
    void delete (String username);
}
