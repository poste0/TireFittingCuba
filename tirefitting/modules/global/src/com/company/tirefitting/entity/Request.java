package com.company.tirefitting.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;

@Table(name = "TIREFITTING_REQUEST")
@Entity(name = "tirefitting_Request")
public class Request extends StandardEntity {
    private static final long serialVersionUID = -5868800352015799809L;

    @Column
    @NotNull
    private String requestType;

    @Column
    @NotNull
    @Min(13)
    @Max(18)
    private Integer wheelRadius;

    @Column(name = "time")
    @NotNull
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ServicePoint.class)
    @JoinColumn(name = "servicePoint")
    private ServicePoint servicePoint;

    public RequestType getRequestType() {
        return requestType == null ? null : RequestType.fromId(requestType);
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType == null ? null : requestType.getId();
    }

    public Integer getWheelRadius() {
        return wheelRadius;
    }

    public void setWheelRadius(Integer wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}