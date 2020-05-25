/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.entities;

import java.util.Date;

/**
 *
 * @author ghada
 */
public class TypeEvents {
  
    int id;
    String type;
    Date date;

    public TypeEvents() {
    }

    public TypeEvents(String type) {
        this.type = type;
    }

    public TypeEvents(int id, String type, Date date) {
        this.id = id;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TypeEvents{" + "id=" + id + ", type=" + type + ", date=" + date + '}';
    }
  
}
