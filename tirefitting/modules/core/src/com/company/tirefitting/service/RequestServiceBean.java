package com.company.tirefitting.service;

import com.company.tirefitting.core.ChangeWheel;
import com.company.tirefitting.core.RequestTypeDurationCalculator;
import com.company.tirefitting.core.RubberFix;
import com.company.tirefitting.core.TireDismount;
import com.company.tirefitting.entity.Request;
import com.company.tirefitting.entity.RequestType;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

@Service(RequestService.NAME)
public class RequestServiceBean implements RequestService {

    @Override
    public double getDuration(Request request) {
        RequestTypeDurationCalculator calculator = getCalculator(request.getRequestType());
        return calculator.getDuration(request.getWheelRadius());
    }

    @Override
    public LocalDateTime getEndTime(Request request) {
        return request.getTime().plus(
                Math.round(getDuration(request)),
                ChronoUnit.SECONDS
        );
    }

    private RequestTypeDurationCalculator getCalculator(RequestType requestType){
        String requestTypeId = requestType.getId();
        if(requestTypeId.equals(RequestType.TIRE_DISMOUNT.getId())){
            return AppBeans.get(TireDismount.NAME);
        }
        else if(requestTypeId.equals(RequestType.RUBBER_FIX.getId())){
            return AppBeans.get(RubberFix.NAME);
        }
        else if(requestTypeId.equals(RequestType.CHANGE_WHEEL.getId())){
            return AppBeans.get(ChangeWheel.NAME);
        }
        else{
            throw new NotImplementedException("Calculator is not implemented");
        }
    }
}