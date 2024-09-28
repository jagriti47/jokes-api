package com.example.jokesapi.controller;

import com.example.jokesapi.model.Joke;
import com.example.jokesapi.service.JokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class JokeController {

    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }
    @GetMapping("/jokes")
    public ResponseEntity<Flux<Joke>> getJokes(@RequestParam int count) {
        if (count <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Flux<Joke> jokes = jokeService.getJokes(count);
        return ResponseEntity.ok(jokes);
    }
}
