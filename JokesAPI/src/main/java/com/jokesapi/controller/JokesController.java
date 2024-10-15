package com.jokesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.exception.InvalidCountException;
import com.jokesapi.service.JokesAPIService;

@RestController
public class JokesController {

    private final JokesAPIService jokesAPIService;

    @Autowired
    public JokesController(JokesAPIService jokesAPIService) {
        this.jokesAPIService = jokesAPIService;
    }
    @GetMapping("/jokes")
    public Flux<JokeAPIResponseDTO> getJokes(@RequestParam int count) {
        if (count <= 0 || count > 100) {
            throw new InvalidCountException("Count must be between 1 to 100");
        }

        return jokesAPIService.fetchAndSaveJokes(count)
            .thenMany(jokesAPIService.getAllSavedJokes()); 
    }
}
