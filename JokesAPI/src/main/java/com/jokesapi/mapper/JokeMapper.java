package com.jokesapi.mapper;

import com.jokesapi.dto.JokeAPIResponseDTO;
import com.jokesapi.dto.JokeResponseDTO;
import com.jokesapi.dto.JokeSaveDTO;
import com.jokesapi.model.Joke;

public class JokeMapper {

    public static JokeAPIResponseDTO toAPIResponseDTO(Joke joke) {
        return new JokeAPIResponseDTO(joke.getId(), joke.getSetup(), joke.getPunchline());
    }
    public static JokeResponseDTO toResponseDTO(Joke joke) {
        return new JokeResponseDTO(joke.getId(), joke.getSetup(), joke.getPunchline());
    }

    public static Joke toEntity(JokeSaveDTO jokeSaveDTO) {
        return new Joke(jokeSaveDTO.getSetup(), jokeSaveDTO.getPunchline());
    }

    public static JokeSaveDTO toSaveDTO(JokeResponseDTO jokeResponse) {
        return new JokeSaveDTO(jokeResponse.getId(),jokeResponse.getSetup(), jokeResponse.getPunchline());
    }
}
