package com.company.tirefitting.core;

import org.springframework.stereotype.Component;

@Component(ChangeWheel.NAME)
public class ChangeWheel implements RequestTypeDurationCalculator {
    public static final String NAME = "tirefitting_ChangeWheel";

    @Override
    public double getDuration(int radius) {
        return 15 * 60 * (1 + ((double)radius - 13) / 5);
    }
}