package com.cydeo.service.impl;

import com.cydeo.config.KeycloakProperties;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;
    private Keycloak keycloakAdmin;

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }


    @PostConstruct
    public void initKeycloakAdmin() {
        keycloakAdmin = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm(keycloakProperties.getAdminRealm())
                .username(keycloakProperties.getAdminUsername())
                .password(keycloakProperties.getAdminPassword())
                .clientId(keycloakProperties.getAdminClient())
                .build();
    }

    public void createUser(String username, String email, String password, String firstName, String lastName) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Set password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Arrays.asList(credential));

        // Get realm users resource
        UsersResource usersResource = getUsersResource();

        try {
            // Create user
            usersResource.create(user);

            // Find the created user
            List<UserRepresentation> searchedUser = usersResource.search(username, true);
            if (!searchedUser.isEmpty()) {
                String userId = searchedUser.get(0).getId();
                // You can add additional setup here (e.g., role assignments)
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user in Keycloak", e);
        }
    }

    public void updateUser(String username, String email, String firstName, String lastName) {
        UsersResource usersResource = getUsersResource();

        try {
            // Find user by username
            List<UserRepresentation> users = usersResource.search(username);
            if (users.isEmpty()) {
                throw new RuntimeException("User not found: " + username);
            }

            UserRepresentation user = users.get(0);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            usersResource.get(user.getId()).update(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user in Keycloak", e);
        }
    }

    public void deleteUser(String username) {
        UsersResource usersResource = getUsersResource();

        try {
            List<UserRepresentation> users = usersResource.search(username);
            if (!users.isEmpty()) {
                usersResource.delete(users.get(0).getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user in Keycloak", e);
        }
    }

    public List<UserRepresentation> getAllUsers() {
        try {
            return getUsersResource().list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch users from Keycloak", e);
        }
    }

    public UserRepresentation getUserByUsername(String username) {
        try {
            List<UserRepresentation> users = getUsersResource().search(username);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user from Keycloak", e);
        }
    }

    private UsersResource getUsersResource() {
        return keycloakAdmin.realm(keycloakProperties.getRealm()).users();
    }

    // Optional: Method to assign roles to user
    public void assignRole(String username, String roleName) {
        try {
            UserRepresentation user = getUserByUsername(username);
            if (user != null) {
                // Get role representation
                var role = keycloakAdmin.realm(keycloakProperties.getRealm())
                        .roles()
                        .get(roleName)
                        .toRepresentation();

                // Assign role to user
                keycloakAdmin.realm(keycloakProperties.getRealm())
                        .users()
                        .get(user.getId())
                        .roles()
                        .realmLevel()
                        .add(Arrays.asList(role));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign role to user in Keycloak", e);
        }
    }

    @Override
    public void delete(String username) {

    }
}