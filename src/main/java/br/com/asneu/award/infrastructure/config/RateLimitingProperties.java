package br.com.asneu.award.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limiting")
public class RateLimitingProperties {
    
    private boolean enabled = true;
    private int requestsPerMinute = 60;
    private int burstCapacity = 10;
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }
    
    public void setRequestsPerMinute(int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }
    
    public int getBurstCapacity() {
        return burstCapacity;
    }
    
    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }
}