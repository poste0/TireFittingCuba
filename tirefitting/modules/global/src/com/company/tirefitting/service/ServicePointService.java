package com.company.tirefitting.service;

import java.util.UUID;

public interface ServicePointService {
    String NAME = "tirefitting_ServicePointService";

    UUID delete(UUID id);
}