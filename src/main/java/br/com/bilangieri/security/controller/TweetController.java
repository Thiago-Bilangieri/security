package br.com.bilangieri.security.controller;


import br.com.bilangieri.security.controller.dto.CreateTweetDto;
import br.com.bilangieri.security.controller.dto.FeedDto;
import br.com.bilangieri.security.services.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TweetController {

    private final TweetService tweetService;


    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/feeds")
    public ResponseEntity<FeedDto> feed(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        var feeds = tweetService.feed(page, pageSize);
        return ResponseEntity.ok(feeds);
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken jwtAuthenticationToken) {
        tweetService.createTweet(createTweetDto, jwtAuthenticationToken);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {
        try {
            tweetService.deleteTweet(tweetId, token);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            HttpStatusCode status = e.getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            } else if (status == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                return ResponseEntity.status(status).build();
            }
        }
    }

}
