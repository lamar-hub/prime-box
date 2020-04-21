package com.lamar.primebox.web.util;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CapacityUtil {

    private final CapacityProperties capacityProperties;

    public CapacityUtil(CapacityProperties capacityProperties) {
        this.capacityProperties = capacityProperties;
    }

    public long getDefaultStored() {
        return capacityProperties.getDefaultStored();
    }

    public long getLimit(Optional<Long> currentLimit) {
        if (currentLimit.isPresent()) {
            if (currentLimit.get() == capacityProperties.getFreemiumLimit()) {
                return capacityProperties.getPremiumLimit();
            } else {
                if (currentLimit.get() == capacityProperties.getPremiumLimit()) {
                    return capacityProperties.getBusinessLimit();
                }
            }
        }
        return capacityProperties.getFreemiumLimit();
    }
}
