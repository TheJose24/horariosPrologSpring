package me.devbyjose.prolog.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrologConfigService {
    
    @Value("${prolog.enabled:true}")
    private boolean prologEnabled;
    
    @Value("${prolog.fallback.enabled:true}")
    private boolean fallbackEnabled;
    
    public boolean isPrologEnabled() {
        return prologEnabled;
    }
    
    public boolean isFallbackEnabled() {
        return fallbackEnabled;
    }
    
    public void setPrologEnabled(boolean enabled) {
        this.prologEnabled = enabled;
    }
    
    public void setFallbackEnabled(boolean enabled) {
        this.fallbackEnabled = enabled;
    }
} 