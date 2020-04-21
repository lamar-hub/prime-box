package com.lamar.primebox.web.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "capacity")
public class CapacityProperties {
    
    private long defaultStored;
    private long freemiumLimit;
    private long premiumLimit;
    private long businessLimit;
    
}
