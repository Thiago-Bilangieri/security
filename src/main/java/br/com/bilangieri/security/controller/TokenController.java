package br.com.bilangieri.security.controller;

import br.com.bilangieri.security.controller.dto.LoginRequest;
import br.com.bilangieri.security.controller.dto.LoginResponse;
import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("User or password is invalid!");
        }
        var expiresIn = 300L;
        var now = Instant.now();

//        var scopes = user.get().getRoles()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                .collect(Collectors.toList());

//
        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));


        var claims = JwtClaimsSet.builder()
                .issuer("SecurityBackEnd")
                .subject(user.get().getUserId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
//                .claim("scope", scopes)
                .issuedAt(now).build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtValue,expiresIn));
    }
}
