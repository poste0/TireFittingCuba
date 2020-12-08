package com.company.tirefitting.service;

import com.company.tirefitting.entity.Request;

import java.time.LocalDateTime;

public interface RequestService {
    String NAME = "tirefitting_RequestService";

    double getDuration(Request request);

    LocalDateTime getEndTime(Request request);
}