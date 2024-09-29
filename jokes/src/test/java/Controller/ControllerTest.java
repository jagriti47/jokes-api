package Controller;

import com.example.jokesapi.controller.JokeController;
import com.example.jokesapi.model.Joke;
import com.example.jokesapi.service.JokeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class JokeControllerTest {

    @Mock
    private JokeService jokeService;

    @InjectMocks
    private JokeController jokeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getJokes_shouldReturnJokes_whenCountIsValid() {
        // Arrange
        Joke joke = new Joke();
        joke.setId(1);
        joke.setSetup("Why did the chicken cross the road?");
        joke.setPunchline("To get to the other side!");

        when(jokeService.getJokes(5)).thenReturn(Flux.just(joke));

        
        ResponseEntity<Flux<Joke>> response = jokeController.getJokes(5);

       
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getJokes_shouldReturnBadRequest_whenCountIsZeroOrNegative() {
        
        ResponseEntity<Flux<Joke>> response = jokeController.getJokes(0);

        
        assertEquals(400, response.getStatusCodeValue());
    }
}
