package com.company.tirefitting.core;

import com.company.tirefitting.entity.RequestType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface RequestTypeDurationCalculator {
    double getDuration(int radius);

}
