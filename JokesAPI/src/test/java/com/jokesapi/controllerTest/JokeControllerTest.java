package com.jokesapi.controllerTest;

import com.jokesapi.controller.JokesController;
import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.exception.InvalidCountException;
import com.jokesapi.service.JokesAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;
import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

class JokesControllerTest {

    @Mock
    private JokesAPIService jokesAPIService;

    @InjectMocks
    private JokesController jokesController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = bindToController(jokesController).build();
    }

    @Test
    void testGetJokes_Success() {
        JokeAPIResponseDTO jokeResponse = new JokeAPIResponseDTO(1, "Setup", "Punchline");
        Flux<JokeAPIResponseDTO> jokeFlux = Flux.just(jokeResponse);
        JokeResponseDTO jokeResponseDTO=new JokeResponseDTO(1,"question","answer");
        Flux<JokeResponseDTO>jokeResponseFlux=Flux.just(jokeResponseDTO);
        
        when(jokesAPIService.fetchAndSaveJokes(anyInt())).thenReturn(jokeResponseFlux);
        when(jokesAPIService.getAllSavedJokes()).thenReturn(jokeFlux);

       
        webTestClient.get()
            .uri("/jokes?count=5")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JokeAPIResponseDTO.class)
            .hasSize(1) 
            .contains(jokeResponse);
    }

    @Test
    void testGetJokes_InvalidCount_LessThanOne() {
        
        webTestClient.get()
            .uri("/jokes?count=0")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .consumeWith(result -> {
                String responseBody = new String(result.getResponseBody());
                assert(responseBody.contains("Count must be between 1 to 100"));
            });
    }

    @Test
    void testGetJokes_InvalidCount_MoreThanHundred() {
        
        webTestClient.get()
            .uri("/jokes?count=101")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .consumeWith(result -> {
                String responseBody = new String(result.getResponseBody());
                assert(responseBody.contains("Count must be between 1 to 100"));
            });
    }
}
