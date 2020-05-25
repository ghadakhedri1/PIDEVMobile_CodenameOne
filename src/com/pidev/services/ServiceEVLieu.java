/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.pidev.entities.Animal;
import com.pidev.entities.Evenement;
import com.pidev.entities.Lieu;
import com.pidev.utils.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ghada
 */
public class ServiceEVLieu {
    ArrayList<Lieu> tasksA;
    ArrayList<Evenement> tasks;

    public static ServiceEVLieu instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public ServiceEVLieu() {
        req = new ConnectionRequest();
    }

    public static ServiceEVLieu getInstance() {
        if (instance == null) {
            instance = new ServiceEVLieu();
        }
        return instance;
    }

    public ArrayList<Lieu> parseLieu(String jsonText) {
        try {
            tasksA = new ArrayList<>();

            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
               Lieu l = new Lieu();
                float id = Float.parseFloat(obj.get("id").toString());
                l.setId((int) id);
                l.setNom_lieu(obj.get("nom").toString());
                tasksA.add(l);
            }

        } catch (IOException ex) {

        }
        return tasksA;
    }
public ArrayList<Lieu> getAllTasksLieu() {
        String url = "http://localhost/PIDEV/web/app_dev.php/journale/readLieu";
        ConnectionRequest con  = new ConnectionRequest();
        con.setUrl(url);
        con.setPost(false);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                tasksA = parseLieu(new String(con.getResponseData()));
               con.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return tasksA;
    }
    public ArrayList<Evenement> parseEvenement(String jsonText) {
        try {
            tasks = new ArrayList<>();

            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
               Evenement e = new Evenement();
                float id = Float.parseFloat(obj.get("id").toString());
                e.setId((int) id);
                e.setType_events(Parser.totypeEvents(obj.get("TypeEvents")));
                tasks.add(e);
            }

        } catch (IOException ex) {

        }
        return tasks;
    }
public ArrayList<Evenement> getAllEvenement() {
        String url = "http://localhost/PIDEV/web/app_dev.php/journale/readEvenement";
        ConnectionRequest con  = new ConnectionRequest();
        con.setUrl(url);
        con.setPost(false);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                tasks = parseEvenement(new String(con.getResponseData()));
               con.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return tasks;
    }
}
