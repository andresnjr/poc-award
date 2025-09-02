package br.com.asneu.award.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa um intervalo entre dois prêmios consecutivos de um produtor")
public class ProducerIntervalDto {
    
    @Schema(description = "Nome do produtor", example = "Joel Silver")
    private String producer;
    
    @Schema(description = "Intervalo em anos entre os dois prêmios", example = "1")
    private Integer interval;
    
    @Schema(description = "Ano do prêmio anterior", example = "1990")
    private Integer previousWin;
    
    @Schema(description = "Ano do prêmio seguinte", example = "1991")
    private Integer followingWin;
    
    public ProducerIntervalDto() {}
    
    public ProducerIntervalDto(String producer, Integer interval, Integer previousWin, Integer followingWin) {
        this.producer = producer;
        this.interval = interval;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }
    
    public String getProducer() {
        return producer;
    }
    
    public void setProducer(String producer) {
        this.producer = producer;
    }
    
    public Integer getInterval() {
        return interval;
    }
    
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
    
    public Integer getPreviousWin() {
        return previousWin;
    }
    
    public void setPreviousWin(Integer previousWin) {
        this.previousWin = previousWin;
    }
    
    public Integer getFollowingWin() {
        return followingWin;
    }
    
    public void setFollowingWin(Integer followingWin) {
        this.followingWin = followingWin;
    }
}