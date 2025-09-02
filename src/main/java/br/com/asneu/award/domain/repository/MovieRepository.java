package br.com.asneu.award.domain.repository;

import br.com.asneu.award.domain.entity.Movie;
import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    List<Movie> findByWinnerTrue();
    Movie save(Movie movie);
    void saveAll(List<Movie> movies);
}