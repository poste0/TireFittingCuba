package com.company.tirefitting.core;

import org.springframework.stereotype.Component;

@Component(TireDismount.NAME)
public class TireDismount implements RequestTypeDurationCalculator {
    public static final String NAME = "tirefitting_TireDismount";

    @Override
    public double getDuration(int radius) {
        return 60 * 60 * (1 + ((double)radius - 13) / 5);
    }
}