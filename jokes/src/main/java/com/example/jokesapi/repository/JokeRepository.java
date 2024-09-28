package com.example.jokesapi.repository;

import com.example.jokesapi.model.Joke;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeRepository extends ReactiveCrudRepository<Joke, String> {
}
