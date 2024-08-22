package br.com.bilangieri.security.config;

import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.entities.User;
import br.com.bilangieri.security.repository.RoleRepository;
import br.com.bilangieri.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                (user)-> {System.out.println("Admin already exists");},
                ()->{
                    var user = new User();
                    user.setUserName("admin");
                    user.setPassword(bCryptPasswordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}
