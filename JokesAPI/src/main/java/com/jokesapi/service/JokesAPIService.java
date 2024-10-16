package com.jokesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.mapper.JokeMapper;
import com.jokesapi.model.Joke;

@Service
public class JokesAPIService {

    private final JokesDBService jokesDBService;
    private final WebClient webClient;

    @Autowired
    public JokesAPIService(JokesDBService jokesDBService, WebClient.Builder webClientBuilder) {
        this.jokesDBService = jokesDBService;
        this.webClient = webClientBuilder.baseUrl("https://official-joke-api.appspot.com").build();
    }

    public Flux<JokeResponseDTO> fetchAndSaveJokes(int count) {
        return Flux.range(0, (count + 9) / 10) 
            .flatMap(i -> {
                return Flux.range(0, 10) 
                    .flatMap(j -> webClient.get()
                        .uri("/random_joke")
                        .retrieve()
                        .onStatus(HttpStatus::isError, response -> 
                            Mono.error(new RuntimeException("Failed to fetch joke: " + response.statusCode())))
                        .bodyToMono(JokeResponseDTO.class)
                    )
                    .collectList()
                    .flatMapMany(jokesList -> {
                        return Flux.fromIterable(jokesList)
                            .map(JokeMapper::toSaveDTO)
                            .map(JokeMapper::toEntity)
                            .flatMap(jokesDBService::saveJoke)
                            .collectList()
                            .flatMapMany(savedJokes -> Flux.fromIterable(savedJokes));
                    });
            })
            .doOnError(e -> System.out.println("Error saving jokes: " + e.getMessage()));
    }



    public Flux<JokeAPIResponseDTO> getAllSavedJokes(int count) {
        return jokesDBService.getAllJokes()
            .take(count);
    }

}
