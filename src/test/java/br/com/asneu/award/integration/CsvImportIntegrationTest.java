package br.com.asneu.award.integration;

import br.com.asneu.award.AwardApplication;
import br.com.asneu.award.application.service.CsvImportService;
import br.com.asneu.award.domain.entity.Movie;
import br.com.asneu.award.domain.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AwardApplication.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:csvtestdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.profiles.active=test"
})
public class CsvImportIntegrationTest {
    
    @Autowired
    private CsvImportService csvImportService;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Test
    public void testCsvImport() {
        csvImportService.importCsvData();
        
        List<Movie> allMovies = movieRepository.findAll();
        assertFalse(allMovies.isEmpty(), "Deve ter importado filmes do CSV");
        
        List<Movie> winnerMovies = movieRepository.findByWinnerTrue();
        assertFalse(winnerMovies.isEmpty(), "Deve ter filmes vencedores");
        
        Movie firstWinner = winnerMovies.get(0);
        assertNotNull(firstWinner.getYear());
        assertNotNull(firstWinner.getTitle());
        assertNotNull(firstWinner.getStudios());
        assertNotNull(firstWinner.getProducers());
        assertTrue(firstWinner.getWinner());
        
        long winnerCount = allMovies.stream().mapToLong(movie -> movie.getWinner() ? 1 : 0).sum();
        assertTrue(winnerCount > 0, "Deve ter pelo menos um vencedor");
        
        boolean hasExpectedMovie = allMovies.stream()
                .anyMatch(movie -> "Can't Stop the Music".equals(movie.getTitle()) && movie.getYear().equals(1980));
        assertTrue(hasExpectedMovie, "Deve conter o filme esperado do CSV");
    }
    
    @Test
    public void testMovieDataIntegrity() {
        csvImportService.importCsvData();
        
        List<Movie> movies = movieRepository.findAll();
        
        for (Movie movie : movies) {
            assertNotNull(movie.getYear(), "Ano não deve ser nulo");
            assertNotNull(movie.getTitle(), "Título não deve ser nulo");
            assertNotNull(movie.getStudios(), "Estúdios não devem ser nulos");
            assertNotNull(movie.getProducers(), "Produtores não devem ser nulos");
            assertNotNull(movie.getWinner(), "Status de vencedor não deve ser nulo");
            
            assertTrue(movie.getYear() > 1900, "Ano deve ser válido");
            assertFalse(movie.getTitle().trim().isEmpty(), "Título não deve estar vazio");
            assertFalse(movie.getProducers().trim().isEmpty(), "Produtores não devem estar vazios");
        }
    }
}