package br.com.asneu.award.application.service.impl;

import br.com.asneu.award.application.service.ProducerIntervalService;
import br.com.asneu.award.domain.entity.Movie;
import br.com.asneu.award.domain.repository.MovieRepository;
import br.com.asneu.award.presentation.dto.ProducerIntervalDto;
import br.com.asneu.award.presentation.dto.ProducerIntervalResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação do serviço para calcular intervalos entre prêmios consecutivos de produtores, incluindo empates.
 * Algoritmo:
 * 1. Busca todos os filmes vencedores
 * 2. Extrai e agrupa produtores por seus anos de vitória
 * 3. Calcula intervalos entre vitórias consecutivas
 * 4. Identifica menores e maiores intervalos (incluindo empates)
 */
@Service
public class ProducerIntervalServiceImpl implements ProducerIntervalService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Override
    public ProducerIntervalResponseDto getProducerIntervals() {
        // Etapa 1: Buscar apenas os filmes vencedores (winner = true)
        List<Movie> winnerMovies = movieRepository.findByWinnerTrue();
        
        // Etapa 2: Criar mapa para agrupar anos de vitória por produtor
        Map<String, List<Integer>> producerWins = new HashMap<>();
        
        // Etapa 3: Processar cada filme vencedor
        for (Movie movie : winnerMovies) {
            // Separar produtores usando vírgula ou " and " como delimitadores
            String[] producers = movie.getProducers().split(",|\\sand\\s");
            for (String producer : producers) {
                producer = producer.trim();
                if (!producer.isEmpty()) {
                    // Agrupar anos de vitória por produtor
                    producerWins.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie.getYear());
                }
            }
        }
        
        // Etapa 4: Lista para armazenar todos os intervalos calculados
        List<ProducerIntervalDto> intervals = new ArrayList<>();
        
        // Etapa 5: Calcular intervalos para produtores com múltiplas vitórias
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            
            // Só processar produtores que ganharam mais de uma vez
            if (years.size() > 1) {
                // Ordenar anos para calcular intervalos consecutivos
                Collections.sort(years);
                
                // Calcular intervalo entre cada par de vitórias consecutivas
                for (int i = 0; i < years.size() - 1; i++) {
                    int interval = years.get(i + 1) - years.get(i);
                    intervals.add(new ProducerIntervalDto(
                            producer,
                            interval,
                            years.get(i),
                            years.get(i + 1)
                    ));
                }
            }
        }
        
        // Etapa 6: Verificar se há intervalos para processar
        if (intervals.isEmpty()) {
            // Nenhum produtor ganhou mais de uma vez
            return new ProducerIntervalResponseDto(new ArrayList<>(), new ArrayList<>());
        }
        
        // Etapa 7: Encontrar menor e maior intervalos
        int minInterval = intervals.stream().mapToInt(ProducerIntervalDto::getInterval).min().orElse(0);
        int maxInterval = intervals.stream().mapToInt(ProducerIntervalDto::getInterval).max().orElse(0);
        
        // Etapa 8: Filtrar todos os produtores com intervalos mínimos
        List<ProducerIntervalDto> minIntervals = intervals.stream()
                .filter(interval -> interval.getInterval() == minInterval)
                .collect(Collectors.toList());
        
        // Etapa 9: Filtrar todos os produtores com intervalos máximos
        List<ProducerIntervalDto> maxIntervals = intervals.stream()
                .filter(interval -> interval.getInterval() == maxInterval)
                .collect(Collectors.toList());
        
        // Etapa 10: Retornar resultado com menores e maiores intervalos
        return new ProducerIntervalResponseDto(minIntervals, maxIntervals);
    }
}