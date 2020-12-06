package com.company.tirefitting.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @Column
    @NotNull
    private DateTime time;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ServicePoint.class)
    @JoinColumn(name = "servicePoint")
    private ServicePoint servicePoint;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }
}