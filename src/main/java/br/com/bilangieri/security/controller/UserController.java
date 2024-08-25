package br.com.bilangieri.security.controller;


import br.com.bilangieri.security.controller.dto.CreateUserDto;
import br.com.bilangieri.security.entities.User;
import br.com.bilangieri.security.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<String> newUser(@RequestBody CreateUserDto dto) {
        try {
            userService.newUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listAllUsers() {
        var user = userService.listUsers();
        return ResponseEntity.ok(user);
    }

}
