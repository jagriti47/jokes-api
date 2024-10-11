package jokesAPiController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import DTO.JokeResponseDTO;
import Service.JokeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class JokeController {

    private final JokeService jokeService;

    @Autowired
    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("/jokes")
    public Flux<Object> getJokes(@RequestParam int count) {
        return jokeService.fetchAndSaveJokes(count)
            .onErrorResume(ex -> {
                return Mono.just(new JokeResponseDTO(null, "Error occurred", ex.getMessage()));
            });
    }

    @GetMapping("/jokes/saved")
    public Flux<JokeResponseDTO> getSavedJokes() {
        return jokeService.getSavedJokes();
    }
}
