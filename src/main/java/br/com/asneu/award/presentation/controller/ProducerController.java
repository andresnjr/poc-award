package br.com.asneu.award.presentation.controller;

import br.com.asneu.award.application.service.ProducerIntervalService;
import br.com.asneu.award.presentation.dto.ProducerIntervalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producers")
@Tag(name = "Produtores", description = "Endpoints relacionados aos produtores dos filmes do Golden Raspberry Awards")
public class ProducerController {
    
    @Autowired
    private ProducerIntervalService producerIntervalService;
    
    @Operation(
        summary = "Obter intervalos de prêmios dos produtores",
        description = "Retorna o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido. " +
                     "Os dados são calculados com base nos filmes vencedores da categoria Pior Filme do Golden Raspberry Awards."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Intervalos calculados com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProducerIntervalResponseDto.class),
                examples = @ExampleObject(
                    name = "Exemplo de resposta",
                    value = """
                    {
                        "min": [
                            {
                                "producer": "Producer 1",
                                "interval": 1,
                                "previousWin": 2008,
                                "followingWin": 2009
                            }
                        ],
                        "max": [
                            {
                                "producer": "Producer 2",
                                "interval": 99,
                                "previousWin": 1900,
                                "followingWin": 1999
                            }
                        ]
                    }"""
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Exemplo de erro",
                    value = """
                    {
                        "timestamp": "2023-12-01T10:30:00",
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "Erro ao processar intervalos de produtores",
                        "path": "/api/producers/prize-intervals"
                    }"""
                )
            )
        )
    })
    @GetMapping("/prize-intervals")
    public ResponseEntity<ProducerIntervalResponseDto> getProducerPrizeIntervals() {
        ProducerIntervalResponseDto response = producerIntervalService.getProducerIntervals();
        return ResponseEntity.ok(response);
    }
}