package br.com.asneu.award.integration;

import br.com.asneu.award.AwardApplication;
import br.com.asneu.award.domain.entity.Movie;
import br.com.asneu.award.domain.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AwardApplication.class)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.profiles.active=test"
})
public class ProducerControllerIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        
        List<Movie> testMovies = Arrays.asList(
            new Movie(1980, "Can't Stop the Music", "Associated Film Distribution", "Allan Carr", true),
            new Movie(1990, "Ghosts Can't Do It", "Triumph Releasing", "Bo Derek", true),
            new Movie(1999, "Wild Wild West", "Warner Bros", "Jon Peters", true),
            new Movie(2002, "Swept Away", "Screen Gems", "Matthew Vaughn", true),
            new Movie(1984, "Bolero", "Cannon Films", "Bo Derek", true),
            new Movie(1985, "Rambo: First Blood Part II", "Columbia Pictures", "Buzz Feitshans", true),
            new Movie(1986, "Howard the Duck", "Universal Studios", "Gloria Katz", true),
            new Movie(1987, "Leonard Part 6", "Columbia Pictures", "Bill Cosby", true),
            new Movie(1988, "Cocktail", "Touchstone Pictures", "Ted Field and Robert W. Cort", true),
            new Movie(2004, "Catwoman", "Warner Bros", "Denise Di Novi and Edward McDonnell", true),
            new Movie(2005, "Son of the Mask", "New Line Cinema", "Erica Huggins", true),
            new Movie(2015, "Fifty Shades of Grey", "Universal Pictures", "Michael De Luca, Dana Brunetti and E. L. James", true),
            new Movie(2019, "The Haunting of Sharon Tate", "Skyline Entertainment", "Lucas Jarach, Vanessa Pereira and Daniel Farrands", true),
            new Movie(2021, "Diana the Musical", "Netflix", "Beth Williams", true)
        );
        
        movieRepository.saveAll(testMovies);
    }
    
    @Test
    public void testGetProducerPrizeIntervals() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/producers/prize-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(jsonResponse, Map.class);
        
        assertNotNull(response);
        assertTrue(response.containsKey("min"));
        assertTrue(response.containsKey("max"));
        
        List<Map<String, Object>> minIntervals = (List<Map<String, Object>>) response.get("min");
        List<Map<String, Object>> maxIntervals = (List<Map<String, Object>>) response.get("max");
        
        assertNotNull(minIntervals);
        assertNotNull(maxIntervals);
        
        assertFalse(minIntervals.isEmpty(), "Intervalos mínimos não devem estar vazios");
        
        for (Map<String, Object> interval : minIntervals) {
            assertTrue(interval.containsKey("producer"));
            assertTrue(interval.containsKey("interval"));
            assertTrue(interval.containsKey("previousWin"));
            assertTrue(interval.containsKey("followingWin"));
            
            assertNotNull(interval.get("producer"));
            assertNotNull(interval.get("interval"));
            assertNotNull(interval.get("previousWin"));
            assertNotNull(interval.get("followingWin"));
        }
        
        for (Map<String, Object> interval : maxIntervals) {
            assertTrue(interval.containsKey("producer"));
            assertTrue(interval.containsKey("interval"));
            assertTrue(interval.containsKey("previousWin"));
            assertTrue(interval.containsKey("followingWin"));
            
            assertNotNull(interval.get("producer"));
            assertNotNull(interval.get("interval"));
            assertNotNull(interval.get("previousWin"));
            assertNotNull(interval.get("followingWin"));
        }
        
        if (!minIntervals.isEmpty() && !maxIntervals.isEmpty()) {
            Integer minInterval = (Integer) minIntervals.get(0).get("interval");
            Integer maxInterval = (Integer) maxIntervals.get(0).get("interval");
            assertTrue(minInterval <= maxInterval, "Intervalo mínimo deve ser <= intervalo máximo");
        }
    }
    
    @Test
    public void testEndpointReturnsValidJsonStructure() throws Exception {
        mockMvc.perform(get("/api/producers/prize-intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray());
    }
    
    @Test
    public void testIntervalCalculationLogic() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/producers/prize-intervals"))
                .andExpect(status().isOk())
                .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(jsonResponse, Map.class);
        
        List<Map<String, Object>> minIntervals = (List<Map<String, Object>>) response.get("min");
        List<Map<String, Object>> maxIntervals = (List<Map<String, Object>>) response.get("max");
        
        if (!minIntervals.isEmpty()) {
            Map<String, Object> minInterval = minIntervals.get(0);
            assertNotNull(minInterval.get("interval"));
            assertNotNull(minInterval.get("previousWin"));
            assertNotNull(minInterval.get("followingWin"));
            
            Integer interval = (Integer) minInterval.get("interval");
            Integer previous = (Integer) minInterval.get("previousWin");
            Integer following = (Integer) minInterval.get("followingWin");
            
            assertEquals(interval, following - previous, "Intervalo deve ser igual ao ano seguinte menos o ano anterior");
        }
        
        if (!maxIntervals.isEmpty()) {
            Map<String, Object> maxInterval = maxIntervals.get(0);
            assertNotNull(maxInterval.get("interval"));
            assertNotNull(maxInterval.get("previousWin"));
            assertNotNull(maxInterval.get("followingWin"));
            
            Integer interval = (Integer) maxInterval.get("interval");
            Integer previous = (Integer) maxInterval.get("previousWin");
            Integer following = (Integer) maxInterval.get("followingWin");
            
            assertEquals(interval, following - previous, "Intervalo deve ser igual ao ano seguinte menos o ano anterior");
        }
    }
}