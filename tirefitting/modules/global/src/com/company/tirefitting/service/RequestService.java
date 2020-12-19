package com.company.tirefitting.service;

import com.company.tirefitting.entity.Request;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RequestService {
    String NAME = "tirefitting_RequestService";

    double getDuration(Request request);

    LocalDateTime getEndTime(Request request);

    UUID add(Request request);

    boolean isBusy(Request request, LocalDateTime addedRequestTime, LocalDateTime addedRequestEndTime);

    UUID add(String id, String requestType, String time, String servicePoint, Integer wheelRadius);
}