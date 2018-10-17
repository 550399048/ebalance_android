package com.eui.ebalance.business.home.tendency.model;

/**
 * Created by letv on 17-11-14.
 */

public class ChangeDataEntity {

    private String name;
    private String unit;
    private String value;


    public ChangeDataEntity(String name, String value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
