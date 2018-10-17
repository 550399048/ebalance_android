package com.eui.ebalance.business.history.model;

import com.eui.ebalance.business.home.dynamic.model.Entity;

import java.util.List;

/**
 * Created by letv on 17-11-15.
 */

public class DataDetailEntity {
    private int icon;
    private String date;
    private String time;
    private List<Entity> entities;

    public DataDetailEntity(int icon, String date, String time, List<Entity> entities) {
        this.icon = icon;
        this.date = date;
        this.time = time;
        this.entities = entities;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
