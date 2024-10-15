package com.jokesapi.serviceTest;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.mapper.JokeMapper;
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
    void testSaveJoke_Success() {
       
        Joke joke = new Joke(1, "Setup", "Punchline");
        Joke savedJoke = new Joke(1, "Setup", "Punchline");
        JokeResponseDTO expectedResponse = new JokeResponseDTO(1, "Setup", "Punchline");

        when(jokesRepository.findById(joke.getId())).thenReturn(Mono.empty());
        when(jokesRepository.save(joke)).thenReturn(Mono.just(savedJoke));
        when(JokeMapper.toResponseDTO(savedJoke)).thenReturn(expectedResponse);

        Mono<JokeResponseDTO> result = jokesDBService.saveJoke(joke);

        assertEquals(expectedResponse, result.block());
        verify(jokesRepository, times(1)).save(joke);
    }

    @Test
    void testSaveJoke_AlreadyExists() {
        Joke joke = new Joke(1, "Setup", "Punchline");
        Joke existingJoke = new Joke(1, "Existing Setup", "Existing Punchline");
        JokeResponseDTO expectedResponse = new JokeResponseDTO(1, "Joke already exists in db", "");

        when(jokesRepository.findById(joke.getId())).thenReturn(Mono.just(existingJoke));

        Mono<JokeResponseDTO> result = jokesDBService.saveJoke(joke);

        assertEquals(expectedResponse, result.block());
        verify(jokesRepository, never()).save(joke); 
    }

    @Test
    void testSaveJoke_DatabaseError() {
        Joke joke = new Joke(1, "Setup", "Punchline");

        when(jokesRepository.findById(joke.getId())).thenReturn(Mono.empty());
        when(jokesRepository.save(joke)).thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<JokeResponseDTO> result = jokesDBService.saveJoke(joke);

        result.onErrorResume(e -> {
            assertEquals("Database error: Database error", e.getMessage());
            return Mono.empty();
        }).block();
    }

    @Test
    void testGetAllJokes_Success() {
        
        Joke joke1 = new Joke(1, "Setup1", "Punchline1");
        Joke joke2 = new Joke(2, "Setup2", "Punchline2");
        JokeAPIResponseDTO jokeResponse1 = new JokeAPIResponseDTO(1,"Setup1", "Punchline1");
        JokeAPIResponseDTO jokeResponse2 = new JokeAPIResponseDTO(1,"Setup2", "Punchline2");

        when(jokesRepository.findAll()).thenReturn(Flux.just(joke1, joke2));
        when(JokeMapper.toAPIResponseDTO(joke1)).thenReturn(jokeResponse1);
        when(JokeMapper.toAPIResponseDTO(joke2)).thenReturn(jokeResponse2);

        Flux<JokeAPIResponseDTO> result = jokesDBService.getAllJokes();

        assertEquals(2, result.count().block());
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
