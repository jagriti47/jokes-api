package com.jokesapi.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.exception.InvalidCountException;
import com.jokesapi.service.JokesAPIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "*")
@RestController
public class JokesController {

    private final JokesAPIService jokesAPIService;

    @Autowired
    public JokesController(JokesAPIService jokesAPIService) {
        this.jokesAPIService = jokesAPIService;
    }

    @Operation(summary = "Returns a list of jokes", description = "Returns a list of jokes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "A JSON array of jokes"),
        @ApiResponse(responseCode = "400", description = "Invalid count supplied"),
        @ApiResponse(responseCode = "404", description = "Jokes not found")
    })
    @GetMapping("/jokes")
    public Flux<JokeAPIResponseDTO> getJokes(
        @Parameter(description = "Number of jokes to retrieve", required = true, example = "10") 
        @RequestParam int count) {
        
        if (count <= 0 || count > 100) {
            throw new InvalidCountException("Count must be between 1 to 100");
        }

        return jokesAPIService.fetchAndSaveJokes(count)
            .thenMany(jokesAPIService.getAllSavedJokes(count));
    }
}
