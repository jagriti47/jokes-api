package com.jokesapi.serviceTest;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.model.Joke;
import com.jokesapi.repository.JokesRepository;
import com.jokesapi.service.JokesDBService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JokesDBServiceTest {

    @InjectMocks
    private JokesDBService jokesDBService;

    @Mock
    private JokesRepository jokesRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSaveJoke_DatabaseError() {
        Joke joke = new Joke("Setup", "Punchline");

        when(jokesRepository.findById(joke.getId())).thenReturn(Mono.empty());
        when(jokesRepository.save(joke)).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<JokeResponseDTO> result = jokesDBService.saveJoke(joke);

        result.onErrorResume(e -> {
            assertEquals("Database error: Database error", e.getMessage());
            return Mono.empty();
        }).block();
    }

    @Test
    void testGetAllJokes_DatabaseError() {
        
        when(jokesRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));

        Flux<JokeAPIResponseDTO> result = jokesDBService.getAllJokes();

        result.onErrorResume(e -> {
            assertEquals("Database error: Database error", e.getMessage());
            return Flux.empty();
        }).blockFirst();
    }
}
