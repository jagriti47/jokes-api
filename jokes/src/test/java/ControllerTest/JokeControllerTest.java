package ControllerTest;

import DTO.JokeResponseDTO;
import Service.JokeService;
import jokesAPiController.JokeController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

class JokeControllerTest {

    @InjectMocks
    private JokeController jokeController;

    @Mock
    private JokeService jokeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetJokes() {
        // Arrange
        JokeResponseDTO jokeResponse = new JokeResponseDTO(1L, "Setup", "Punchline");
        when(jokeService.fetchAndSaveJokes(anyInt())).thenReturn(Flux.just(jokeResponse));

        // Act
        Object response = jokeController.getJokes(1).blockFirst();

        // Assert
        assert response != null;
    }

    @Test
    void testGetSavedJokes() {
        // Arrange
        JokeResponseDTO jokeResponse = new JokeResponseDTO(1L, "Setup", "Punchline");
        when(jokeService.getSavedJokes()).thenReturn(Flux.just(jokeResponse));

        // Act
        var response = jokeController.getSavedJokes().collectList().block();

        // Assert
        assert response != null;
        assert response.size() == 1;
    }
}

