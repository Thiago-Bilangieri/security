package br.com.bilangieri.security.repository;

import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
