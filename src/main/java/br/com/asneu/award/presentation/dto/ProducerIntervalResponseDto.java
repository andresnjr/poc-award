package br.com.asneu.award.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resposta contendo os intervalos mínimos e máximos entre prêmios dos produtores")
public class ProducerIntervalResponseDto {
    
    @Schema(description = "Lista de produtores com menor intervalo entre prêmios consecutivos")
    private List<ProducerIntervalDto> min;
    
    @Schema(description = "Lista de produtores com maior intervalo entre prêmios consecutivos")
    private List<ProducerIntervalDto> max;
    
    public ProducerIntervalResponseDto() {}
    
    public ProducerIntervalResponseDto(List<ProducerIntervalDto> min, List<ProducerIntervalDto> max) {
        this.min = min;
        this.max = max;
    }
    
    public List<ProducerIntervalDto> getMin() {
        return min;
    }
    
    public void setMin(List<ProducerIntervalDto> min) {
        this.min = min;
    }
    
    public List<ProducerIntervalDto> getMax() {
        return max;
    }
    
    public void setMax(List<ProducerIntervalDto> max) {
        this.max = max;
    }
}