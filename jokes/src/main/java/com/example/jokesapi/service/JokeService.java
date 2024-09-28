package com.example.jokesapi.service;

import com.example.jokesapi.model.Joke;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
public class JokeService {

    private final WebClient webClient;

    public JokeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://official-joke-api.appspot.com").build();
    }

    public Flux<Joke> getJokes(int count) {
        return Flux.range(0, (count + 9) / 10)
                .flatMap(batch -> webClient.get()
                        .uri("/random_joke")
                        .retrieve()
                        .bodyToMono(Joke.class)
                        .repeat(10)
                        .take(count)
                );
    }
}
