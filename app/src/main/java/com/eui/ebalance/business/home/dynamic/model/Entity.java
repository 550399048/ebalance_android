package com.eui.ebalance.business.home.dynamic.model;

/**
 * Created by letv on 17-11-14.
 */

public class Entity {

    private int icon;
    private String name;
    private Object value;
    private String type;

    public Entity(int icon, String name, Object value, String type) {
        this.icon = icon;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
