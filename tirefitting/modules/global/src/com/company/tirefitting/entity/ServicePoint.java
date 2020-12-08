package com.company.tirefitting.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.annotation.Nonnegative;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "TIREFITTING_SERVICE_POINT")
@Entity(name = "tirefitting_ServicePoint")
@NamePattern("%s|address")
public class ServicePoint extends StandardEntity {
    private static final long serialVersionUID = -4728837249734768058L;

    @Column
    @NotNull
    private String address;

    @Column
    @NotNull
    @Nonnegative
    private Integer countOfStuff;

    @OneToMany(mappedBy = "servicePoint", cascade = CascadeType.ALL)
    private List<Request> requests;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCountOfStuff() {
        return countOfStuff;
    }

    public void setCountOfStuff(Integer countOfStuff) {
        this.countOfStuff = countOfStuff;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}