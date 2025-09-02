package br.com.asneu.award.application.service.impl;

import br.com.asneu.award.application.service.CsvImportService;
import br.com.asneu.award.domain.entity.Movie;
import br.com.asneu.award.domain.repository.MovieRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportServiceImpl implements CsvImportService, CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(CsvImportServiceImpl.class);
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Override
    public void run(String... args) throws Exception {
        importCsvData();
    }
    
    @Override
    public void importCsvData() {
        try {
            ClassPathResource resource = new ClassPathResource("static/movielist.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withSkipLines(1)
                    .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(';').build())
                    .build();
            
            List<Movie> movies = new ArrayList<>();
            String[] nextLine;
            
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length >= 5) {
                    try {
                        Integer year = Integer.parseInt(nextLine[0].trim());
                        String title = nextLine[1].trim();
                        String studios = nextLine[2].trim();
                        String producers = nextLine[3].trim();
                        Boolean winner = "yes".equalsIgnoreCase(nextLine[4].trim());
                        
                        Movie movie = new Movie(year, title, studios, producers, winner);
                        movies.add(movie);
                        
                    } catch (NumberFormatException e) {
                        logger.warn("Erro ao processar linha do CSV: {}", String.join(";", nextLine));
                    }
                }
            }
            
            csvReader.close();
            movieRepository.saveAll(movies);
            logger.info("Importação concluída. {} filmes foram importados.", movies.size());
            
        } catch (Exception e) {
            logger.error("Erro ao importar dados do CSV: ", e);
            throw new RuntimeException("Erro ao importar dados do CSV", e);
        }
    }
}