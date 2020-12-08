package com.company.tirefitting.core;

import org.springframework.stereotype.Component;

@Component(RubberFix.NAME)
public class RubberFix implements RequestTypeDurationCalculator {
    public static final String NAME = "tirefitting_RubberFix";

    @Override
    public double getDuration(int radius) {
        return 30 * 60 * (1 + ((double)radius - 13) / 5);
    }
}