package br.com.bilangieri.security.services;

import br.com.bilangieri.security.controller.dto.CreateTweetDto;
import br.com.bilangieri.security.controller.dto.FeedDto;
import br.com.bilangieri.security.controller.dto.FeedItemDto;
import br.com.bilangieri.security.entities.Role;
import br.com.bilangieri.security.entities.Tweet;
import br.com.bilangieri.security.repository.TweetRepository;
import br.com.bilangieri.security.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }


    public void createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken token) {
        //var user = userRepository.findByUsername(jwtAuthenticationToken.getName());
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.content());
        tweetRepository.save(tweet);
    }

    public void deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {

        var tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (tweetRepository.findById(tweetId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.delete(tweet);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    public FeedDto feed(int page, int pageSize) {


        var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet ->
                        new FeedItemDto(
                                tweet.getTweetId(),
                                tweet.getContent(),
                                tweet.getUser().getUserName()));
        return new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements());
    }
}


