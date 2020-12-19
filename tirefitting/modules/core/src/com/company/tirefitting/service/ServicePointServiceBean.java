package com.company.tirefitting.service;

import com.company.tirefitting.entity.ServicePoint;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service(ServicePointService.NAME)
public class ServicePointServiceBean implements ServicePointService {
    @Inject
    private DataManager dataManager;

    @Override
    public UUID delete(UUID id) {
        try {
            dataManager.remove(Id.of(id, ServicePoint.class));
            return id;
        }
        catch (Exception e){
            return null;
        }

    }
}