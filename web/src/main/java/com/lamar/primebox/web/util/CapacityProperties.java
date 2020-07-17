package com.lamar.primebox.web.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:primebox.properties")
@ConfigurationProperties(prefix = "capacity")
public class CapacityProperties {

    private long defaultStored;
    private long freemiumLimit;
    private long premiumLimit;
    private long businessLimit;

}
