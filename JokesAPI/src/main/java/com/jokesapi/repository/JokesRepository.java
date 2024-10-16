package com.jokesapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.jokesapi.model.Joke;


@Repository
public interface JokesRepository extends ReactiveCrudRepository<Joke, Integer> {
    
}
