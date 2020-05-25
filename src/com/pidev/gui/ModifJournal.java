/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.gui;

import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.pidev.entities.Animal;
import com.pidev.entities.Evenement;
import com.pidev.entities.Journal;
import com.pidev.entities.Lieu;
import com.pidev.services.ServiceEVLieu;
import com.pidev.services.ServiceJournal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ghada
 */
public class ModifJournal extends Form {

    Form f;
    TextField tchasse;
    TextArea tdescription;
    TextField tlieu;
    TextField trating;
    private Resources theme;
    Button btnMod, btnAnnuler;
    TextField description;
    Form form;
    Animal ida;
    Lieu idl;
    Evenement idv;
    private static String i;

    public ModifJournal(Journal ta) {
        f = new Form("Modification");
        Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container c2 = new Container(new BoxLayout(BoxLayout.X_AXIS));
        Label animal = new Label("Animal");
        Label lieu = new Label("Lieu");
        Label nb = new Label("Nombre chasse");
        Label d = new Label("Date");
        Label des = new Label("Description");
        Label rating = new Label("Rating");
        tchasse = new TextField(String.valueOf(ta.getNbChasse()));
        tdescription = new TextArea(ta.getDescription());
        trating = new TextField(String.valueOf(ta.getId_chasseur()));
        TextField path = new TextField(ta.getImage());
        btnMod = new Button("Modifier");
        btnAnnuler = new Button("Annuler");
        Date dt = new Date();
        Picker p = new Picker();
        p.setDate(ta.getDate());
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
        ida = ta.getAnimal();
        cmp.addActionListener((evt) -> {
            ida = listAnimal.get(cmp.getSelectedIndex());

        });
        idl = ta.getLieu();
        cmpl.addActionListener((evt) -> {
            idl = listLieu.get(cmpl.getSelectedIndex());

        });

        btnMod.addActionListener((e) -> {
            if ((dt.getTime() - p.getDate().getTime()) < 0) {
                if (Dialog.show("Error", "Date  invalide", "ok", null)) {
                    form.showBack();
                }
            } else {
                if ((tchasse.getText().length() == 0) || (Float.parseFloat(tchasse.getText()) == 0)) {
                    if (Dialog.show("Error", "Nombre de chasse invalide", "ok", null)) {
                        form.show();
                    }
                } else {
                    if ((Float.parseFloat(tchasse.getText()) > 20)) {
                        if (Dialog.show("Error", "Max Nombre de chasse est 20", "ok", null)) {
                            form.show();
                        }
                    } else {
                        if ((Float.parseFloat(trating.getText()) > 6)) {
                            if (Dialog.show("Error", "Rating de 0 à 6 ", "ok", null)) {
                                form.show();
                            }
                        } else {
                            ta.setAnimal(ida);
                            ta.setLieu(idl);
                            ta.setDate(p.getDate());
                            ta.setNbChasse(Integer.parseInt(tchasse.getText()));
                            ta.setImage(path.getText());
                            ta.setDescription(tdescription.getText());
                            ta.setId_chasseur(Integer.parseInt(trating.getText()));

                            ser.modifierJournal(ta, ta.getId());

                        }
                    }
                }
            }
        });

        btnAnnuler.addActionListener((e) -> {
            ListJournauxForm a = new ListJournauxForm();
            a.getF().show();
        });

        f.getToolbar().addCommandToLeftBar("back", null, (ev) -> {
            ListJournauxForm h;
            h = new ListJournauxForm();
            h.getF().show();

        });
        c1.add(lieu);
        c1.add(cmpl);
        c1.add(animal);
        c1.add(cmp);
        c1.add(d);
        c1.add(p);
        c1.add(nb);
        c1.add(tchasse);
        c1.add(des);
        c1.add(tdescription);
        c1.add(rating);
        c1.add(trating);
        c2.add(btnMod);
        c2.add(btnAnnuler);
        c1.add(c2);
        f.add(c1);
        f.show();
    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

}
