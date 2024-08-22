package br.com.bilangieri.security.controller;


import br.com.bilangieri.security.controller.dto.CreateUserDto;
import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.entities.User;
import br.com.bilangieri.security.repository.RoleRepository;
import br.com.bilangieri.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDB = userRepository.findByUsername(dto.username());

        if (userFromDB.isPresent()) {
            //return ResponseEntity.badRequest().build();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUserName(dto.username());
        user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listAllUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
