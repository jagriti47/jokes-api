package com.jokesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
            .flatMap(i -> webClient.get()
                .uri("/random_joke")
                .retrieve()
                .onStatus(HttpStatus::isError, response -> 
                    Mono.error(new RuntimeException("Failed to fetch joke: " + response.statusCode())))
                .bodyToMono(JokeResponseDTO.class)
                .flatMap(jokeResponse -> {
                    Joke joke = JokeMapper.toEntity(JokeMapper.toSaveDTO(jokeResponse));
                    return jokesDBService.saveJoke(joke)
                        .onErrorResume(ex -> 
                            Mono.just(new JokeResponseDTO(0, "Error saving joke", ex.getMessage()))
                        );
                })
                .onErrorResume(ex -> 
                    Mono.just(new JokeResponseDTO(0, "Error fetching joke", ex.getMessage()))
                )
            );
    }

    public Flux<JokeAPIResponseDTO> getAllSavedJokes() {
        return jokesDBService.getAllJokes(); 
    }
}
