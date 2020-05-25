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
public class Trophy {
    int idchasseur;
    int nbrechasse;

    public Trophy(int idchasseur, int nbrechasse) {
        this.idchasseur = idchasseur;
        this.nbrechasse = nbrechasse;
    }

    public Trophy() {
    }

    public int getIdchasseur() {
        return idchasseur;
    }

    public void setIdchasseur(int idchasseur) {
        this.idchasseur = idchasseur;
    }

    public int getNbrechasse() {
        return nbrechasse;
    }

    public void setNbrechasse(int nbrechasse) {
        this.nbrechasse = nbrechasse;
    }
    
}
