package com.company.tirefitting.service;

import com.company.tirefitting.core.ChangeWheel;
import com.company.tirefitting.core.RequestTypeDurationCalculator;
import com.company.tirefitting.core.RubberFix;
import com.company.tirefitting.core.TireDismount;
import com.company.tirefitting.entity.Request;
import com.company.tirefitting.entity.RequestType;
import com.company.tirefitting.entity.ServicePoint;
import com.google.gson.Gson;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service(RequestService.NAME)
public class RequestServiceBean implements RequestService {
    @Inject
    private DataManager dataManager;

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

    @Override
    public UUID add(Request request) {
        List<Request> requests = dataManager.loadList(
                LoadContext.create(Request.class).setQuery(LoadContext.createQuery(
                        "SELECT r FROM tirefitting_Request r WHERE r.servicePoint.id = :servicePointId"
                        ).setParameter("servicePointId", request.getServicePoint().getId())
                )
        );

        long busyWorkers = requests.stream().filter(request1 -> {
            return isBusy(request1, request.getTime(), getEndTime(request));
        }).count();
        long workers = request.getServicePoint().getCountOfStuff();

        if(workers - busyWorkers <= 0){
            return null;
        }

        try {
            dataManager.commit(request);
        }
        catch (Exception e){
            return null;
        }

        return request.getId();
    }

    @Override
    public boolean isBusy(Request request, LocalDateTime addedRequestTime, LocalDateTime addedRequestEndTime) {
        LocalDateTime requestTime = request.getTime();
        LocalDateTime requestEndTime = getEndTime(request);

        return (isAfterOrEqual(addedRequestTime, requestTime) && isBeforeOrEqual(addedRequestTime, requestEndTime)) ||
                (isAfterOrEqual(addedRequestEndTime, requestTime) && isBeforeOrEqual(addedRequestEndTime, requestEndTime)) ||
                (isBeforeOrEqual(addedRequestTime, requestTime) && isAfterOrEqual(addedRequestEndTime, requestEndTime));

    }

    @Override
    public UUID add(String id, String requestType, String time, String servicePoint, Integer wheelRadius) {
        Request request = dataManager.create(Request.class);
        request.setId(UUID.fromString(id));
        request.setRequestType(RequestType.fromId(requestType));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        request.setTime(LocalDateTime.parse(time, formatter));
        request.setWheelRadius(wheelRadius);
        ServicePoint service = dataManager.load(
                LoadContext.create(ServicePoint.class).setId(UUID.fromString(servicePoint))
        );
        request.setServicePoint(service);

        List<Request> requests = dataManager.loadList(
                LoadContext.create(Request.class).setQuery(LoadContext.createQuery(
                        "SELECT r FROM tirefitting_Request r WHERE r.servicePoint.id = :servicePointId"
                        ).setParameter("servicePointId", request.getServicePoint().getId())
                )
        );

        long busyWorkers = requests.stream().filter(request1 -> {
            return isBusy(request1, request.getTime(), getEndTime(request));
        }).count();
        long workers = request.getServicePoint().getCountOfStuff();

        if(workers - busyWorkers <= 0){
            return null;
        }

        try{
            dataManager.commit(request);
        }
        catch (Exception e){
            return null;
        }


        return request.getId();
    }

    private boolean isAfterOrEqual(LocalDateTime f, LocalDateTime s){
        return f.isAfter(s) || f.equals(s);
    }

    private boolean isBeforeOrEqual(LocalDateTime f, LocalDateTime s){
        return f.isBefore(s) || f.equals(s);
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