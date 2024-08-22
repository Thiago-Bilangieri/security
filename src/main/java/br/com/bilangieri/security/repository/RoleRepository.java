package br.com.bilangieri.security.repository;

import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
