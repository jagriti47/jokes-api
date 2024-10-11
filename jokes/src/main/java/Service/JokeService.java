package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import DTO.JokeResponseDTO;
import Model.Joke;
import Repository.JokeRepository;
import reactor.core.publisher.Flux;

@Service
public class JokeService {

    private final JokeRepository jokeRepository;
    private final WebClient webClient;

    @Autowired
    public JokeService(JokeRepository jokeRepository, WebClient.Builder webClientBuilder) {
        this.jokeRepository = jokeRepository;
        this.webClient = webClientBuilder.baseUrl("https://official-joke-api.appspot.com").build();
    }

    public Flux<Object> fetchAndSaveJokes(int count) {
        return Flux.range(0, count / 10) 
            .flatMap(i -> webClient.get()
                .uri("/random_joke")
                .retrieve()
                .bodyToMono(Joke.class) 
                .flatMap(joke -> jokeRepository.save(joke) // Saves joke to database
                    .map(savedJoke -> new JokeResponseDTO(savedJoke.getId(), savedJoke.getSetup(), savedJoke.getPunchline()))
                )
            );
    }

    public Flux<JokeResponseDTO> getSavedJokes() {
        return jokeRepository.findAll()
            .map(joke -> new JokeResponseDTO(joke.getId(), joke.getSetup(), joke.getPunchline()));
    }
}
