package JokeServiceTest;

import DTO.JokeResponseDTO;
import Model.Joke;
import Repository.JokeRepository;
import Service.JokeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JokeServiceTest {

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private JokeService jokeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testFetchAndSaveJokes() {
        Joke joke = new Joke(); 
        joke.setId(1L);
        joke.setSetup("Why did the chicken cross the road?");
        joke.setPunchline("To get to the other side!");

    
        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
        when(webClient.get().uri(any(String.class)).retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
        when(webClient.get().uri(any(String.class)).retrieve().bodyToMono(Joke.class)).thenReturn(Mono.just(joke));
        when(jokeRepository.save(any(Joke.class))).thenReturn(Mono.just(joke));

        Flux<Object> result = jokeService.fetchAndSaveJokes(10);

        result.subscribe(jokeResponseDTO -> {
            assert ((JokeResponseDTO) jokeResponseDTO).getId().equals(joke.getId());
            assert ((JokeResponseDTO) jokeResponseDTO).getSetup().equals(joke.getSetup());
            assert ((JokeResponseDTO) jokeResponseDTO).getPunchline().equals(joke.getPunchline());
        });

        verify(jokeRepository, times(1)).save(any(Joke.class));
    }

    @Test
    void testGetSavedJokes() {
        Joke joke = new Joke();
        joke.setId(1L);
        joke.setSetup("Why did the chicken cross the road?");
        joke.setPunchline("To get to the other side!");

        when(jokeRepository.findAll()).thenReturn(Flux.just(joke));

        Flux<JokeResponseDTO> result = jokeService.getSavedJokes();

        result.subscribe(jokeResponseDTO -> {
            assert jokeResponseDTO.getId().equals(joke.getId());
            assert jokeResponseDTO.getSetup().equals(joke.getSetup());
            assert jokeResponseDTO.getPunchline().equals(joke.getPunchline());
        });
    }
}
