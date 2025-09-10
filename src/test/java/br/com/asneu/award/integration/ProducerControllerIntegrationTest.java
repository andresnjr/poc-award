package br.com.asneu.award.integration;

import br.com.asneu.award.AwardApplication;
import br.com.asneu.award.application.service.CsvImportService;
import br.com.asneu.award.infrastructure.repository.MovieJpaRepository;
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
    private MovieJpaRepository movieJpaRepository;
    
    @Autowired
    private CsvImportService csvImportService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        
        // Limpa o banco e importa dados do arquivo CSV padrão
        movieJpaRepository.deleteAll();
        csvImportService.importCsvData();
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
        
        // Validação da estrutura básica
        assertNotNull(response);
        assertTrue(response.containsKey("min"));
        assertTrue(response.containsKey("max"));
        
        List<Map<String, Object>> minIntervals = (List<Map<String, Object>>) response.get("min");
        List<Map<String, Object>> maxIntervals = (List<Map<String, Object>>) response.get("max");
        
        assertNotNull(minIntervals);
        assertNotNull(maxIntervals);
        assertFalse(minIntervals.isEmpty(), "Intervalos mínimos não devem estar vazios");
        assertFalse(maxIntervals.isEmpty(), "Intervalos máximos não devem estar vazios");
        
        // Validação da resposta esperada exata para o arquivo padrão
        // Min: Joel Silver com intervalo de 1 ano (1990-1991)
        assertEquals(1, minIntervals.size(), "Deve haver exatamente 1 produtor com intervalo mínimo");
        Map<String, Object> minInterval = minIntervals.get(0);
        assertEquals("Joel Silver", minInterval.get("producer"));
        assertEquals(1, minInterval.get("interval"));
        assertEquals(1990, minInterval.get("previousWin"));
        assertEquals(1991, minInterval.get("followingWin"));
        
        // Max: Matthew Vaughn com intervalo de 13 anos (2002-2015)
        assertEquals(1, maxIntervals.size(), "Deve haver exatamente 1 produtor com intervalo máximo");
        Map<String, Object> maxInterval = maxIntervals.get(0);
        assertEquals("Matthew Vaughn", maxInterval.get("producer"));
        assertEquals(13, maxInterval.get("interval"));
        assertEquals(2002, maxInterval.get("previousWin"));
        assertEquals(2015, maxInterval.get("followingWin"));
    }
    
    @Test
    public void testEndpointReturnsValidJsonStructure() throws Exception {
        mockMvc.perform(get("/api/producers/prize-intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray());
    }
    
    @Test
    public void testResponseMustMatchDefaultFileContent() throws Exception {
        // Este teste garante que a resposta da API está de acordo com o arquivo padrão movielist.csv
        // Se o arquivo for modificado, este teste deve falhar
        
        MvcResult result = mockMvc.perform(get("/api/producers/prize-intervals"))
                .andExpect(status().isOk())
                .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> response = objectMapper.readValue(jsonResponse, Map.class);
        
        // Valida resposta completa conforme especificação
        String expectedJson = """
            {
              "min": [
                {
                  "producer": "Joel Silver",
                  "interval": 1,
                  "previousWin": 1990,
                  "followingWin": 1991
                }
              ],
              "max": [
                {
                  "producer": "Matthew Vaughn",
                  "interval": 13,
                  "previousWin": 2002,
                  "followingWin": 2015
                }
              ]
            }""";
        
        Map<String, Object> expectedResponse = objectMapper.readValue(expectedJson, Map.class);
        
        // Compara estruturas completas - qualquer alteração no arquivo fará o teste falhar
        assertEquals(expectedResponse, response, 
            "A resposta da API deve corresponder exatamente aos dados do arquivo padrão movielist.csv. " +
            "Se este teste falhou, o arquivo padrão foi modificado e afetou o resultado.");
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
        
        // Valida cálculo dos intervalos
        for (Map<String, Object> interval : minIntervals) {
            Integer intervalValue = (Integer) interval.get("interval");
            Integer previous = (Integer) interval.get("previousWin");
            Integer following = (Integer) interval.get("followingWin");
            
            assertEquals(intervalValue, following - previous, 
                "Intervalo deve ser igual ao ano seguinte menos o ano anterior");
        }
        
        for (Map<String, Object> interval : maxIntervals) {
            Integer intervalValue = (Integer) interval.get("interval");
            Integer previous = (Integer) interval.get("previousWin");
            Integer following = (Integer) interval.get("followingWin");
            
            assertEquals(intervalValue, following - previous, 
                "Intervalo deve ser igual ao ano seguinte menos o ano anterior");
        }
    }
}