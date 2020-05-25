/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.messaging.Message;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import com.pidev.entities.Animal;
import com.pidev.entities.Bareme;
import com.pidev.entities.Evenement;
import com.pidev.entities.Journal;
import com.pidev.entities.Lieu;
import com.pidev.entities.User;
import com.pidev.gui.ListJournauxForm;
import com.pidev.utils.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ghada
 */
public class ServiceJournal {

    public ArrayList<Journal> tasks;
    public ArrayList<Animal> tasksA;
    public static ServiceJournal instance = null;
    public boolean resultOK;
    private ConnectionRequest req;

    public ServiceJournal() {
        req = new ConnectionRequest();
    }

    public static ServiceJournal getInstance() {
        if (instance == null) {
            instance = new ServiceJournal();
        }
        return instance;
    }

    public ArrayList<Journal> parseTasks(String jsonText) throws ParseException {
        try {
            tasks = new ArrayList<>();

            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                Journal t = new Journal();
                Animal m = Parser.toAnimal(obj.get("animal"));
                t.setAnimal(m);
                Lieu l = Parser.toLieu(obj.get("lieu"));
                t.setLieu(l);
                Evenement e = Parser.toEvenement(obj.get("evenement"));
                t.setEvenement(e);
                User user = Parser.toUser(obj.get("user"));
                t.setUser_id(user);
                float id = Float.parseFloat(obj.get("id").toString());
                float nbchasse = Float.parseFloat(obj.get("nbchasse").toString());
                float note = Float.parseFloat(obj.get("idchasseur").toString());
                t.setId_chasseur((int) note);
                t.setNbChasse((int) nbchasse);
                t.setImage((String) obj.get("image"));
                t.setId((int) id);
                t.setDescription(obj.get("description").toString());
                String s = obj.get("date").toString();
                int position = s.indexOf("timestamp");
                int pos1 = s.length();
                String sousChaine = s.substring(position + 10, pos1 - 1);
                double d = Double.parseDouble(sousChaine);
                long batch_date = (long) d;
                Date dt = new Date(batch_date * 1000);
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
                String g = sfd.format(dt);
                try {
                    t.setDate(sfd.parse(g));
                } catch (ParseException ex) {
                }

                tasks.add(t);
            }

        } catch (IOException ex) {

        }
        return tasks;
    }

    public ArrayList<Journal> getAllTasks(int id) {
        String url = "http://localhost/PIDEV/web/app_dev.php/journale/readJournalByUser/" + id;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    tasks = parseTasks(new String(req.getResponseData()));
                } catch (ParseException ex) {

                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

        return tasks;
    }

    public void supprimerJournal(Journal e) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/PIDEV/web/app_dev.php/journale/delete/" + e.getId();
        con.setUrl(Url);
        con.addResponseListener((ee) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);
            Dialog.show("Succés", "Journal supprimé", "ok", null);

            ListJournauxForm a = new ListJournauxForm();

        });
        NetworkManager.getInstance().addToQueueAndWait(con);

    }

    public void ajoutJournal(Journal a) {
        String Url = "http://localhost/PIDEV/web/app_dev.php/journale/createJournal";
        MultipartRequest req = new MultipartRequest();
        try {
            req.setUrl(Url);
            req.setPost(true);
            req.addArgument("nbchasse", String.valueOf(a.getNbChasse()));
            req.addArgument("image", String.valueOf(a.getImage()));
            req.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(a.getDate()));
            req.addArgument("animal", String.valueOf(a.getAnimal().getId()));
            req.addArgument("lieu", String.valueOf(a.getLieu().getId()));
            req.addArgument("evenement", String.valueOf(a.getEvenement().getId()));
            req.addArgument("user", String.valueOf(a.getUser_id().getId()));
            req.addArgument("description", String.valueOf(a.getDescription()));
            req.addArgument("idchasseur", String.valueOf(a.getId_chasseur()));

            req.addResponseListener((response) -> {
                byte[] data = (byte[]) response.getMetaData();
                String s = new String(data);
                Dialog.show("Succés", "Journal Ajouté", "ok", null);

                ListJournauxForm aa = new ListJournauxForm();
                aa.getF().show();
            });

            NetworkManager.getInstance().addToQueueAndWait(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Animal> parseAnimal(String jsonText) {
        try {
            tasksA = new ArrayList<>();

            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                Animal a = new Animal();
                float id = Float.parseFloat(obj.get("id").toString());
                a.setId((int) id);
                a.setNom_animal(obj.get("nom").toString());
                tasksA.add(a);
            }

        } catch (IOException ex) {

        }
        return tasksA;
    }

    public ArrayList<Animal> getAllTasksAnimal() {
        String url = "http://localhost/PIDEV/web/app_dev.php/journale/readAnimal";
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(url);
        con.setPost(false);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                tasksA = parseAnimal(new String(con.getResponseData()));
                con.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return tasksA;
    }

    public void modifierJournal(Journal a, int id) {
        String Url = "http://localhost/PIDEV/web/app_dev.php/journale/MAJJoural/" + id;
        MultipartRequest req = new MultipartRequest();
        try {
            req.setUrl(Url);
            req.setPost(false);
            req.addArgument("id", String.valueOf(a.getId()));
            req.addArgument("nbchasse", String.valueOf(a.getNbChasse()));
            req.addArgument("image", String.valueOf(a.getImage()));
            req.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(a.getDate()));
            req.addArgument("animal", String.valueOf(a.getAnimal().getId()));
            req.addArgument("lieu", String.valueOf(a.getLieu().getId()));
            req.addArgument("evenement", String.valueOf(a.getEvenement().getId()));
            req.addArgument("user", String.valueOf(a.getUser_id().getId()));
            req.addArgument("description", String.valueOf(a.getDescription()));
            req.addArgument("idchasseur", String.valueOf(a.getId_chasseur()));
            
            req.addResponseListener((response) -> {
                byte[] data = (byte[]) response.getMetaData();
                String s = new String(data);
                Dialog.show("Succés", "Journal Modifié", "ok", null);

                ListJournauxForm aa = new ListJournauxForm();
                aa.getF().show();
            });

            NetworkManager.getInstance().addToQueueAndWait(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int calculJournal() {
        ServiceJournal eventService = new ServiceJournal();
        ArrayList<Journal> listEvent = eventService.getAllTasks(3);
        Iterator<Journal> it = listEvent.iterator();

        int nb = 0;
        int s = 0;

        for (Journal ee : listEvent) {

            s += ee.getNbChasse();
            nb++;

            System.out.println(s);
        }

        return s;
    }

    ArrayList<Bareme> tasksB = new ArrayList<>();

    public ArrayList<Bareme> BaremePars(String jsonText) {
        try {

            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                Bareme a = new Bareme();
                float id = Float.parseFloat(obj.get("id").toString());
                float min = Float.parseFloat(obj.get("min").toString());
                float max = Float.parseFloat(obj.get("max").toString());
                float note = Float.parseFloat(obj.get("note").toString());
                a.setId((int) id);
                a.setMin((int) min);
                a.setMax((int) max);
                a.setNote((int) note);
                tasksB.add(a);
            }

        } catch (IOException ex) {

        }
        return tasksB;
    }

    public ArrayList<Bareme> getAllBareme() {
        String url = "http://localhost/PIDEV/web/app_dev.php/journale/readBareme";
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(url);
        con.setPost(false);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                tasksB = BaremePars(new String(con.getResponseData()));
                con.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return tasksB;
    }

    public int calculScore() {
        ServiceJournal eventService = new ServiceJournal();
        ArrayList<Journal> listEvent = eventService.getAllTasks(3);
        ArrayList<Bareme> listBareme = eventService.getAllBareme();
        Iterator<Journal> it = listEvent.iterator();
        int s = 0;
        int nb = nbcompetition();

        for (Bareme ee : listBareme) {

            if (calculJournal() > ee.getMin() && (calculJournal() < ee.getMax())) {
                s = ee.getNote();

            }
        }
        if (nb >= 1 && nb <= 3) {
            s = s + 3;
            System.out.println("score" + s);
        } else if (nb >= 4) {
            s = s + 5;
            System.out.println(s);
        } else {
            s = s + 0;
        }

        return s;

    }

    public int nbcompetition() {
        ServiceJournal eventService = new ServiceJournal();
        ArrayList<Journal> listEvent = eventService.getAllTasks(3);
        ArrayList<Bareme> listBareme = eventService.getAllBareme();
        Iterator<Journal> it = listEvent.iterator();
        int s = 0;
        int nb = 0;
        while (it.hasNext()) {
            Journal ens = it.next();
            if ("competition".equals(ens.getEvenement().getType_events().getType())) {
                nb++;
                System.out.println("nb:" + nb);

            }
        }
        return nb;
    }

    public void sendMail() {

        Message m = new Message("<html><body>Check out <a href=\"https://www.codenameone.com/\">Codename One</a></body></html>");
        m.setMimeType(Message.MIME_HTML);

// notice that we provide a plain text alternative as well in the send method
        boolean success = m.sendMessageViaCloudSync("Codename One", "br.rassil@gmail.com", "Name Of User", "Message Subject",
                "Check out Codename One at https://www.codenameone.com/");
        System.out.println("success: " + success);
    }
}
