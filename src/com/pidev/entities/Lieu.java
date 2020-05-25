/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.entities;

/**
 *
 * @author ghada
 */
public class Lieu {

    int id;
    String nom_lieu;

    public Lieu() {
    }

    public Lieu(int id, String nom_lieu) {
        this.id = id;
        this.nom_lieu = nom_lieu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_lieu() {
        return nom_lieu;
    }

    public void setNom_lieu(String nom_lieu) {
        this.nom_lieu = nom_lieu;
    }

    public Lieu(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Lieu{" + "id=" + id + ", nom_lieu=" + nom_lieu + '}';
    }

}
