package com.jokesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.mapper.JokeMapper;
import com.jokesapi.model.Joke;
import com.jokesapi.repository.JokesRepository;

@Service
public class JokesDBService {

    private final JokesRepository jokeRepository;

    @Autowired
    public JokesDBService(JokesRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    public Mono<JokeResponseDTO> saveJoke(Joke joke) {

        return jokeRepository.findById(joke.getId())
            .flatMap(existingJoke -> {
                return Mono.just(new JokeResponseDTO(existingJoke.getId(), 
                    "Joke already exists in db", ""));
            })
            .switchIfEmpty(jokeRepository.save(joke) 
                .map(savedJoke -> JokeMapper.toResponseDTO(savedJoke))
            )
            .onErrorResume(ex -> {
                return Mono.error(new RuntimeException("Database error: " + ex.getMessage()));
            });
    }

    public Flux<JokeAPIResponseDTO> getAllJokes() {
        return jokeRepository.findAll() 
            .map(JokeMapper::toAPIResponseDTO) 
            .onErrorResume(ex -> {
                return Flux.error(new RuntimeException("Database error: " + ex.getMessage()));
            });
    }
}
