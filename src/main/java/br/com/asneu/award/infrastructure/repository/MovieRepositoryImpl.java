package br.com.asneu.award.infrastructure.repository;

import br.com.asneu.award.domain.entity.Movie;
import br.com.asneu.award.domain.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MovieRepositoryImpl implements MovieRepository {
    
    @Autowired
    private MovieJpaRepository movieJpaRepository;
    
    @Override
    public List<Movie> findAll() {
        return movieJpaRepository.findAll();
    }
    
    @Override
    public List<Movie> findByWinnerTrue() {
        return movieJpaRepository.findByWinnerTrue();
    }
    
    @Override
    public Movie save(Movie movie) {
        return movieJpaRepository.save(movie);
    }
    
    @Override
    public void saveAll(List<Movie> movies) {
        movieJpaRepository.saveAll(movies);
    }
}