/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.gui;

import com.codename1.capture.Capture;
import com.codename1.io.ConnectionRequest;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.pidev.entities.Animal;
import com.pidev.entities.Journal;
import com.pidev.services.ServiceJournal;
import java.util.Date;
import java.util.List;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextArea;
import com.pidev.entities.Evenement;
import com.pidev.entities.Lieu;
import com.pidev.entities.User;
import com.pidev.services.ServiceEVLieu;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;
import rest.file.uploader.tn.FileUploader;
/**
 *
 * @author ghada
 */
public class AjouterJournal extends Form {

   
    private Resources theme;
    Form form;
    Container f;
    TextField tdescription;
    TextField tlieu;
    Button btnajout, btnaff, btnedit, imgBtn;
    TextField path;
    Animal ida;
    Lieu idl;
    Evenement idv;
    private FileUploader file;
    String fileNameInServer;
    private String imgPath;
  

    //Date tdate;

    public AjouterJournal() {
        theme = UIManager.initFirstTheme("/theme_1");
        form = new Form("Ajouter Journal",BoxLayout.y());
        f = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container c1 = new Container(new BoxLayout(BoxLayout.X_AXIS));
        Validator v = new Validator();
        Date dt = new Date();
        Picker p = new Picker();
        p.setDate(dt);
        tdescription = new TextField("", "Description", 500, TextArea.ANY);
        tdescription.setHint("Description");
        TextField nbchasse = new TextField();
        v.addConstraint(nbchasse, new RegexConstraint("[0-9]*", "les chiffres sont acceptés!"));
        Label animal = new Label("Animal");
        Label lieu = new Label("Lieu");
        Label event = new Label("Evenement");
        nbchasse.setHint("Nombre Chasse");
        Label nb = new Label("Nombre chasse");
        Label d = new Label("Date");
        Label des = new Label("Description");   
        Label imag = new Label("Image");   
         Label rate=new Label("Rating :");
       TextField sl = new TextField();
        sl.setEditable(true);
        ComboBox cmp = new ComboBox<Animal>();
        ComboBox cmpl = new ComboBox<Lieu>();
        ComboBox cmpe = new ComboBox<Evenement>();
        ServiceJournal ser = new ServiceJournal();
        ServiceEVLieu se = new ServiceEVLieu();
        List<Animal> listAnimal = ser.getAllTasksAnimal();
        for (Animal l : listAnimal) {
            cmp.addItem(l.getNom_animal());
        }
        List<Lieu> listLieu = se.getAllTasksLieu();

        for (Lieu l : listLieu) {
            cmpl.addItem(l.getNom_lieu());
        }
        List<Evenement> listev = se.getAllEvenement();
        for (Evenement l : listev) {
            cmpe.addItem(l.getType_events().getType());
        }
        cmp.addActionListener((evt) -> {

            ida = listAnimal.get(cmp.getSelectedIndex());
            //System.out.println(Ccateg.getSelectedItem().toString());
        });
        cmpl.addActionListener((evt) -> {

            idl = listLieu.get(cmpl.getSelectedIndex());
            //  System.out.println(Ccateg.getSelectedItem().toString());
        });
        cmpe.addActionListener((evt) -> {

            idv = listev.get(cmpe.getSelectedIndex());
            //  System.out.println(Ccateg.getSelectedItem().toString());
        });
        btnajout = new Button("ajouter");
        btnajout.getUnselectedStyle().setFgColor(5542241);
        btnaff = new Button("Affichage");
        btnedit = new Button("Modifier");
        Button upload = new Button("Upload");
        upload.setMaterialIcon(FontImage.MATERIAL_CLOUD_UPLOAD);
       upload.addPointerReleasedListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent evt) {
               
                try {
                    imgPath=Capture.capturePhoto();
                    System.out.println(imgPath);
                    String link = imgPath.toString();
                    int pod =link.indexOf("/",2);
                    String news =link.substring(pod+2,link.length());
                    System.out.println(""+news);
                    FileUploader fu = new FileUploader("http://localhost/PIDEV/web");
                    fileNameInServer=fu.upload(news);
                    path.setText(fileNameInServer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                
            }
        });

       
    
       
        f.add(lieu);
        f.add(cmpl);
        f.add(animal);
        f.add(cmp);
        f.add(event);
        f.add(cmpe);
        f.add(d);
        f.add(p);
        f.add(nb);
        f.add(nbchasse);
        f.add(des);
        f.add(tdescription);
        f.add(imag);
       // f.add(path);
        f.add(upload);
        f.add(rate);
        f.add(sl);
        
        c1.add(btnajout);
        c1.add(btnaff);
        
        f.add(c1);
        form.add(f);
        //form.show();
        btnajout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((dt.getTime() - p.getDate().getTime()) < 0) {
                    if (Dialog.show("Error", "Date  invalide", "ok", null)) {
                        form.showBack();
                    }
                } else {
                    if ((nbchasse.getText().length() == 0) || (Float.parseFloat(nbchasse.getText()) == 0)) {
                        if (Dialog.show("Error", "Nombre de chasse invalide", "ok", null)) {
                            form.show();
                        }
                    }
                    else {
                    if ((Float.parseFloat(nbchasse.getText()) > 20) ) {
                        if (Dialog.show("Error", "Max Nombre de chasse est 20", "ok", null)) {
                            form.show();
                        }
                    }
                    else {
                    if (ida==null) {
                        if (Dialog.show("Error", "Choisir un animal SVP ", "ok", null)) {
                            form.show();
                        }
                    }
                    else {
                    if (idl==null) {
                        if (Dialog.show("Error", "Choisir un Lieu SVP ", "ok", null)) {
                            form.show();
                        }
                    }
                    else {
                    if (idv==null) {
                        if (Dialog.show("Error", "Choisir Type d'evenement  SVP ", "ok", null)) {
                            form.show();
                        }
                    }
                    else {
                    if ((Float.parseFloat(sl.getText()) >6) ) {
                        if (Dialog.show("Error", "Rating de 0 à 6 ", "ok", null)) {
                            form.show();
                        }
                    }
                    else {

                        try {
                            User u = new User(3);

                            Journal t = new Journal(u, idv, Integer.parseInt(nbchasse.getText()), ida, idl, p.getDate(),fileNameInServer,tdescription.getText(),Integer.parseInt(sl.getText()));
                           

                            ser.ajoutJournal(t);
             
                           //sendMail("ghada.khedri1@esprit.tn");
                            System.out.println(t);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }}}}}}
        });

        btnaff.addActionListener((e) -> {
            ListJournauxForm a = new ListJournauxForm();
            a.getF().show();
        });

        //currentEvent = new Evenement(idEvent ,nom, description);
        btnedit.addActionListener((e) -> {

        });
        form.show();
    }
    
    public void sendMail(String Email) {
        ConnectionRequest req = new ConnectionRequest();
        req.setUrl("http://localhost/journal/sendmail.php?email=" + Email);

        req.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {

                byte[] data = (byte[]) evt.getMetaData();
                String s = new String(data);
                System.err.println("Mail Sent");
            }
        });

        NetworkManager.getInstance().addToQueue(req);
    }

    public Form getF() {
        return form;
    }

    public void setF(Form form) {
        this.form = form;
    }

  

}
