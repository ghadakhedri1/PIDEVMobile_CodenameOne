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
public class Journal {
    private int id;
   public User user_id;
    private Evenement evenement;
    public int nbChasse;
    public Animal animal;
   public Lieu lieu;
    public Date date;
    public String description;
    public String image;
    private int id_chasseur;

    public Journal() {
    }

    public Journal(int id) {
        this.id = id;
    }

    public Journal(Date date ) {
        this.date = date;
    }

    public Journal(User user_id, Evenement evenement, int nbChasse, Animal animal, Lieu lieu,Date date,String image,String description,int id_chasseur) {
        this.user_id = user_id;
        this.evenement = evenement;
        this.nbChasse = nbChasse;
        this.animal = animal;
        this.lieu = lieu;
        this.date=date;
        this.image=image;
        this.description=description;
        this.id_chasseur=id_chasseur;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
    public int getNbChasse() {
        return nbChasse;
    }

    public void setNbChasse(int nbChasse) {
        this.nbChasse = nbChasse;
    }

    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_chasseur() {
        return id_chasseur;
    }

    public void setId_chasseur(int id_chasseur) {
        this.id_chasseur = id_chasseur;
    }

    @Override
    public String toString() {
        return "Journal{" + "id=" + id + ", user_id=" + user_id + ", evenement=" + evenement + ", nbChasse=" + nbChasse + ", animal=" + animal + ", lieu=" + lieu + ", date=" + date + ", description=" + description + ", image=" + image + ", id_chasseur=" + id_chasseur + '}';
    }

    
    
    
    
}
