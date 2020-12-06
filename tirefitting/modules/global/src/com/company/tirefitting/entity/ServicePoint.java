package com.company.tirefitting.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.annotation.Nonnegative;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "TIREFITTING_SERVICE_POINT")
@Entity(name = "tirefitting_ServicePoint")
public class ServicePoint extends StandardEntity {
    private static final long serialVersionUID = -4728837249734768058L;

    @Column
    @NotNull
    private String address;

    @Column
    @NotNull
    @Nonnegative
    private Integer countOfStuff;

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
}