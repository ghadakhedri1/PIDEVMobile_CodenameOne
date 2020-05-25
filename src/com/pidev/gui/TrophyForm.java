/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.gui;

import com.codename1.components.ShareButton;
import com.codename1.components.SpanLabel;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.pidev.entities.Journal;
import com.pidev.services.ServiceJournal;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ghada
 */
public class TrophyForm {

    private Form form;
    private Resources theme;
    Label user, email, level, nbco;
    Form f;
    SpanLabel lb;

    public TrophyForm() {
        theme = UIManager.initFirstTheme("/theme_1");
        f = new Form("Score",BoxLayout.y());
        Container c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container c1 = new Container(new FlowLayout(Component.CENTER));
        ServiceJournal ser = new ServiceJournal();
        int b = ser.calculJournal();
        int k = ser.calculScore();
        Label nbchasse = new Label("Total Chasse : " + b);
        nbchasse.getStyle().setFgColor(0xb88273);
        Label score = new Label("score : " + k);
        score.getStyle().setFgColor(0xb88273);
        
        if (k > 0 && k <= 5) {
            level = new Label("Level :  Initiation ");
        } else if (k > 5 && k <= 15) {
            level = new Label("Level :  Débutant");
        } else if (k > 15 && k <= 25) {
            level = new Label("Level :  Intérmediaire");
        } else {
            level = new Label("Level :  Avancé");
        }
level.getStyle().setFgColor(0xb88273);
        Button details = new Button("Details");
        Button stat = new Button("Statistique");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                Dialog.show("Details", "Votre score est calculé selon le nombre total de chasse et nombre de competition ,vous avez participé à:" + "  " + ser.nbcompetition() + " " + "compétition(s)", "ok", null);
            }
        });
        stat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
statistique h;
            h = new statistique();
            h.getF().show();
                
            }
        });

        ArrayList<Journal> lis = ser.getAllTasks(3);
        Image img = theme.getImage("trophee-brillant.gif").scaled(800, 800);
        for (Journal li : lis) {
            user = new Label("UserName : " + li.getUser_id().getUsername());
            user.getStyle().setFgColor(0xb88273);
            email = new Label("Email : " + li.getUser_id().getEmail());
            email.getStyle().setFgColor(0xb88273);

        }
        ShareButton sb=new ShareButton();
        sb.setText("Partager Score sur Facebook ");
        sb.setTextToShare("Bonjour, je t’invite à\n" +
"télécharger et à installer l’application Hunters Club . C’est un truc de ouf voici mon score: "+score);
        c1.add(img);
        c.add(c1);
        c.add(FlowLayout.encloseCenter(user));
        c.add(FlowLayout.encloseCenter(email));
        c.add(FlowLayout.encloseCenter(score));
        c.add(FlowLayout.encloseCenter(level));
        c.add(FlowLayout.encloseCenter(nbchasse));    
        c.add(details);
        c.add(sb);
         c.add(stat);
        f.add(c);
        Media video = null;
        try {
            video = MediaManager.createMedia("file:///C:/songs/sand.mp3", true);
            
            video.play();

        } catch (IOException ex) {

        }
        f.getToolbar().addCommandToLeftBar("back", null, (ev) -> {
            ListJournauxForm h;
            h = new ListJournauxForm();
            h.getF().show();

        });
         

    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

}
