package br.com.asneu.award.infrastructure.repository;

import br.com.asneu.award.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieJpaRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByWinnerTrue();
}