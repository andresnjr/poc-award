package br.com.asneu.award.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "studios", nullable = false)
    private String studios;
    
    @Column(name = "producers", nullable = false)
    private String producers;
    
    @Column(name = "is_winner", nullable = false)
    private Boolean winner;
    
    public Movie() {}
    
    public Movie(Integer year, String title, String studios, String producers, Boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getStudios() {
        return studios;
    }
    
    public void setStudios(String studios) {
        this.studios = studios;
    }
    
    public String getProducers() {
        return producers;
    }
    
    public void setProducers(String producers) {
        this.producers = producers;
    }
    
    public Boolean getWinner() {
        return winner;
    }
    
    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
}