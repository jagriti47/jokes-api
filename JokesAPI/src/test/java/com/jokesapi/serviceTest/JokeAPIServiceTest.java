package com.jokesapi.serviceTest;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.mapper.JokeMapper;
import com.jokesapi.model.Joke;
import com.jokesapi.service.JokesAPIService;
import com.jokesapi.service.JokesDBService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JokesAPIServiceTest {

    @InjectMocks
    private JokesAPIService jokesAPIService;

    @Mock
    private JokesDBService jokesDBService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
    }

    @Test
    void testFetchAndSaveJokes_Success() {
        int count = 10;
        JokeAPIResponseDTO jokeAPIResponseDTO = new JokeAPIResponseDTO(1,"Setup", "Punchline");
        JokeResponseDTO savedJokeResponseDTO = new JokeResponseDTO(1, "Setup", "Punchline");

        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokeResponseDTO.class)).thenReturn(Mono.just(savedJokeResponseDTO));
        when(jokesDBService.saveJoke(any(Joke.class))).thenReturn(Mono.just(savedJokeResponseDTO));

        Flux<JokeResponseDTO> result = jokesAPIService.fetchAndSaveJokes(count);

        assertEquals(1, result.count().block());
        verify(jokesDBService, times(1)).saveJoke(any(Joke.class));
    }

    @Test
    void testFetchAndSaveJokes_FetchError() {
       
        int count = 10;

        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(new WebClientResponseException(
                HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null));

        Flux<JokeResponseDTO> result = jokesAPIService.fetchAndSaveJokes(count);

        result.subscribe(jokeResponse -> {
            assertEquals(0, jokeResponse.getId());
            assertEquals("Error fetching joke", jokeResponse.getSetup());
        });
    }

    @Test
    void testFetchAndSaveJokes_SaveError() {
        
        int count = 10;
        JokeAPIResponseDTO jokeAPIResponseDTO = new JokeAPIResponseDTO(1,"Setup", "Punchline");
        JokeResponseDTO savedJokeResponseDTO = new JokeResponseDTO(1, "Setup", "Punchline");
        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokeResponseDTO.class)).thenReturn(Mono.just(savedJokeResponseDTO));
        when(jokesDBService.saveJoke(any(Joke.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

     
        Flux<JokeResponseDTO> result = jokesAPIService.fetchAndSaveJokes(count);

        result.subscribe(jokeResponse -> {
            assertEquals(0, jokeResponse.getId());
            assertEquals("Error saving joke", jokeResponse.getSetup());
        });
    }
}
