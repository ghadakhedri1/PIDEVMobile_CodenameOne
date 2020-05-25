/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.utils;

import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.pidev.entities.Animal;
import com.pidev.entities.Evenement;
import com.pidev.entities.Lieu;
import com.pidev.entities.TypeEvents;
import com.pidev.entities.User;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ghada
 */
public class Parser {
    public static Animal toAnimal(Object obj){
        Animal A = new Animal();
        Map<String , Object> JsonAbonnement = (Map<String , Object>) obj ;
        A.setNom_animal(JsonAbonnement.get("nom").toString());
        A.setId((int) Float.parseFloat(JsonAbonnement.get("id").toString()));
        return A ;
    }
    public static Lieu toLieu(Object obj){
        Lieu l = new Lieu();
        Map<String , Object> JsonAbonnement = (Map<String , Object>) obj ;
         l.setNom_lieu(JsonAbonnement.get("nom").toString());
        l.setId((int) Float.parseFloat(JsonAbonnement.get("id").toString()));
        return l;
    }
    public static TypeEvents totypeEvents(Object obj){
       TypeEvents et = new TypeEvents();
        Map<String , Object> JsonAbonnement = (Map<String , Object>) obj ;
         et.setType(JsonAbonnement.get("type").toString());
        et.setId((int) Float.parseFloat(JsonAbonnement.get("id").toString()));
        return et;
    }
     public static Evenement toEvenement(Object obj){
       Evenement e = new Evenement();
        Map<String , Object> JsonAbonnement = (Map<String , Object>) obj ;
         e.setType_events(totypeEvents(JsonAbonnement.get("typeEvents")));
        e.setId((int) Float.parseFloat(JsonAbonnement.get("id").toString()));
        return e;
    }
    
     public static User toUser(Object obj) {
        User u = new User();
        Map<String, Object> jsonUser = (Map<String, Object>) obj;
        u.setId((int) Float.parseFloat(jsonUser.get("id").toString()));
        List<String> roles = (List<String> )jsonUser.get("roles");
        String roleString = roles.get(0);    
        u.setUsername(jsonUser.get("username").toString());
        u.setEmail(jsonUser.get("email").toString());
        u.setPassword(jsonUser.get("password").toString());
        return u;
    }

}
