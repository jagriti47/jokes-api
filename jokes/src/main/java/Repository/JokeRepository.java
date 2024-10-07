package Repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import Model.Joke;

@Repository
public interface JokeRepository extends ReactiveCrudRepository<Joke, Long> {
}
