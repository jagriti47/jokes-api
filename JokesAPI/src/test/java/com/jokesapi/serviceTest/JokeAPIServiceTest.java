package com.jokesapi.serviceTest;
import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.mapper.JokeMapper;
import com.jokesapi.model.Joke;
import com.jokesapi.model.WebClientConfig;
import com.jokesapi.service.JokesAPIService;
import com.jokesapi.service.JokesDBService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JokeAPIServiceTest {
    @Mock
    private JokesDBService jokesDBService;

    @Mock
    private Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private JokesAPIService jokesAPIService;

    private JokeResponseDTO jokeResponseDTO;
    private Joke joke;

    @BeforeEach
    public void setUp() {
        jokeResponseDTO = new JokeResponseDTO(1, "What did the ocean say to the shore?", "Nothing, it just waved.");
        joke = new Joke(jokeResponseDTO.getSetup(), jokeResponseDTO.getPunchline());

        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    public void testFetchAndSaveJokes_Success() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokeResponseDTO.class)).thenReturn(Mono.just(jokeResponseDTO));
        when(jokesDBService.saveJoke(any(Joke.class))).thenReturn(Mono.just(JokeMapper.toResponseDTO(joke)));

        Flux<JokeResponseDTO> result = jokesAPIService.fetchAndSaveJokes(1);

        StepVerifier.create(result)
                .expectNext(JokeMapper.toResponseDTO(joke))
                .verifyComplete();
    }

    @Test
    public void testFetchAndSaveJokes_Failure_FetchError() {
       
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokeResponseDTO.class)).thenReturn(Mono.error(new RuntimeException("Fetch error")));
        Flux<JokeResponseDTO> result = jokesAPIService.fetchAndSaveJokes(1);

        StepVerifier.create(result)
                .expectNext(new JokeResponseDTO(0, "Error fetching joke", "Fetch error"))
                .verifyComplete();
    }

    @Test
    public void testGetAllSavedJokes_Success() {
       
        when(jokesDBService.getAllJokes()).thenReturn(Flux.just(JokeMapper.toAPIResponseDTO(joke)));

        Flux<JokeAPIResponseDTO> result = jokesAPIService.getAllSavedJokes(1);

        StepVerifier.create(result)
                .expectNext(JokeMapper.toAPIResponseDTO(joke))
                .verifyComplete();
    }
}