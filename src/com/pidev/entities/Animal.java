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
public class Animal {

    int id;
    String nom_animal;

    public Animal() {
    }

    public Animal(int id, String nom_animal) {
        this.id = id;
        this.nom_animal = nom_animal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_animal() {
        return nom_animal;
    }

    public void setNom_animal(String nom_animal) {
        this.nom_animal = nom_animal;
    }

    public Animal(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Animal{" + "id=" + id + ", nom_animal=" + nom_animal + '}';
    }

}
