package br.com.bilangieri.security.controller;


import br.com.bilangieri.security.controller.dto.CreateTweetDto;
import br.com.bilangieri.security.entities.Tweet;
import br.com.bilangieri.security.repository.TweetRepository;
import br.com.bilangieri.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;



    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken jwtAuthenticationToken) {
//        var user = userRepository.findByUsername(jwtAuthenticationToken.getName());
        var user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName()));
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.content());
        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }
}
