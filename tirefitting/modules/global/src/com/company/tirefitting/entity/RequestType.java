package com.company.tirefitting.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum RequestType implements EnumClass<String> {

    TIRE_DISMOUNT("tire_dismount"), RUBBER_FIX("rubber_fix"), CHANGE_WHEEL("change_wheel");

    private final String id;

    RequestType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RequestType fromId(String id) {
        for (RequestType at : RequestType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}