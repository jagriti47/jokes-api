package com.jokesapi.controllerTest;
import com.jokesapi.controller.JokesController;
import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.exception.InvalidCountException;
import com.jokesapi.service.JokesAPIService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JokeControllerTest {

    @Mock
    private JokesAPIService jokesAPIService;

    @InjectMocks
    private JokesController jokesController;

    @Test
    public void testGetJokes_ValidCount() {
        
        int count = 10;
        JokeAPIResponseDTO joke = new JokeAPIResponseDTO(1, "What did the ocean say to the shore?", "Nothing, it just waved.");
        JokeResponseDTO jokeResponse = new JokeResponseDTO(1,"What did the ocean say to the shore?", "Nothing, it just waved.");
        
        when(jokesAPIService.fetchAndSaveJokes(count)).thenReturn(Flux.just(jokeResponse));
        when(jokesAPIService.getAllSavedJokes(count)).thenReturn(Flux.just(joke));

       
        Flux<JokeAPIResponseDTO> result = jokesController.getJokes(count);

        
        StepVerifier.create(result)
                .expectNext(joke)
                .verifyComplete();
    }

    @Test
    public void testGetJokes_InvalidCount_Zero() {
       
        int invalidCount = 0;

        
        StepVerifier.create(Flux.error(() -> new InvalidCountException("Count must be between 1 to 100")))
                .expectError(InvalidCountException.class)
                .verify();
    }

    @Test
    public void testGetJokes_InvalidCount_Negative() {
       
        int invalidCount = -5;

        
        StepVerifier.create(Flux.error(() -> new InvalidCountException("Count must be between 1 to 100")))
                .expectError(InvalidCountException.class)
                .verify();
    }

    @Test
    public void testGetJokes_InvalidCount_OverLimit() {
       
        int invalidCount = 101;

       
        StepVerifier.create(Flux.error(() -> new InvalidCountException("Count must be between 1 to 100")))
                .expectError(InvalidCountException.class)
                .verify();
    }
}
