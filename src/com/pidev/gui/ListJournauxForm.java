/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.gui;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.ImageViewer;
import com.pidev.entities.Journal;
import com.pidev.services.ServiceJournal;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.Slider;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.FlowLayout;
import java.util.Random;

/**
 *
 * @author ghada
 */
public class ListJournauxForm {

    private Resources theme;
    Button btnFB;
    Form f;
    Button modif, supp;
    Label desc;
    int moy = 0;

    public ListJournauxForm() {
        theme = UIManager.initFirstTheme("/theme_1");
        f = new Form("Liste Journaux", BoxLayout.y());

        ArrayList<Journal> journaux = new ArrayList<>();
        ServiceJournal ce = new ServiceJournal();
        journaux.addAll(ServiceJournal.getInstance().getAllTasks(3));
        Style s = UIManager.getInstance().getComponentStyle("Title");
        TextField searchField = new TextField("", "Search journal");
        searchField.getHintLabel().setUIID("Title");
        searchField.setUIID("Title");
        searchField.getAllStyles().setAlignment(Component.LEFT);
        f.getToolbar().setTitleComponent(searchField);
        FontImage searchIcon = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, s);
        searchField.addDataChangeListener((i1, i2) -> {
            String t = searchField.getText();
            if (t.length() < 1) {
                f.removeAll();
                for (Journal journal : journaux) {
                    this.addItem(journal);
                }
                for (Component cmp : f.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }

            } else {
                f.removeAll();
                for (Journal journal : journaux) {
                    if ((journal.getDescription().toLowerCase().contains(t.toLowerCase())) || (journal.getAnimal().getNom_animal().toLowerCase().contains(t.toLowerCase()))
                            || (journal.getLieu().getNom_lieu().toLowerCase().contains(t.toLowerCase())) || (journal.getEvenement().getType_events().getType().toLowerCase().contains(t.toLowerCase()))) {
                        this.addItem(journal);
                    }
                }
            }
            f.getContentPane().animateLayout(250);
        });
        f.getToolbar().addCommandToRightBar("", searchIcon, (e) -> {
            searchField.startEditingAsync();
        });

        ServiceJournal rs = new ServiceJournal();
        if (!rs.getAllTasks(3).isEmpty()) {
            ServiceJournal SP = new ServiceJournal();
            ArrayList<Journal> lis = SP.getAllTasks(3);
            for (Journal li : lis) {
                Container cc = new Container(BoxLayout.x());
                Container c = new Container(BoxLayout.y());
                Label nbchasse = new Label("Nombre Chasse : " + li.getNbChasse());
                Label animal = new Label("Animal : " + li.getAnimal().getNom_animal());
                Label lieu = new Label("Lieu : " + li.getLieu().getNom_lieu());
                Label desc = new Label("Description : " + li.getDescription());
                Label d = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(li.getDate()));
                Slider sl = new Slider();
                sl = createStarRankSlider();
                moy = li.getId_chasseur();
                System.out.println("note:" + li.getId_chasseur());
                sl.setProgress((int) moy);
                c.add(animal);
                c.add(lieu);
                c.add(nbchasse);
                c.add(d);
                c.add(desc);
                Image placeholder1 = Image.createImage(800, 800);
                EncodedImage en = EncodedImage.createFromImage(placeholder1, false);
                URLImage urli = URLImage.createToStorage(en, li.getImage(), "http://localhost/PIDEV/web/uploads/" + li.getImage());
                ImageViewer img = new ImageViewer();
                img.setImage(urli);

                c.add(img);

                Button service1 = new Button("score");
                service1.getAllStyles().setFgColor(ColorUtil.rgb(24, 242, 0));
                service1.setIcon(FontImage.createMaterial(FontImage.MATERIAL_REPLY, service1.getUnselectedStyle()));
                service1.addActionListener((ActionListener) (ActionEvent evt) -> {
                    TrophyForm t = new TrophyForm();
                    t.getF().show();
                });
                c.add(service1);
                c.add(FlowLayout.encloseCenter(sl));
                supp = new Button("Supprimer");
                supp.addActionListener((evt) -> {
                    ServiceJournal service = new ServiceJournal();
                    service.supprimerJournal(li);
                    ListJournauxForm l = new ListJournauxForm();
                    l.getF().show();
                });
                modif = new Button("Modifier");
                modif.addActionListener((evt) -> {
                    ModifJournal m = new ModifJournal(li);
                    m.getF().show();
                });
                c.add(modif);
                c.add(supp);

                f.add(c);

                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

            }
        } else {
            f.add(new Label("Aucun Journal a afficher"));

        }
        f.getToolbar().addCommandToLeftBar("back", null, (ev) -> {
            AjouterJournal h;
            h = new AjouterJournal();
            h.getF().show();

        });

    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

    private void initStarRankStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    public Slider createStarRankSlider() {
        Slider starRank = new Slider();
        starRank.setEditable(true);
        starRank.setMinValue(0);
        starRank.setMaxValue(6);
        starRank.setProgress(2);
        Font fnt;
        fnt = Font.createTrueTypeFont("native:MainLight", "native:MainLight").derive(Display.getInstance().convertToPixels(5, true), Font.STYLE_PLAIN);
        Style s = new Style(0xffff33, 0, fnt, (byte) 0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        s.setOpacity(5);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        initStarRankStyle(starRank.getSliderEmptySelectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderEmptyUnselectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderFullSelectedStyle(), fullStar);
        initStarRankStyle(starRank.getSliderFullUnselectedStyle(), fullStar);
        starRank.setPreferredSize(new Dimension(fullStar.getWidth() * 5, fullStar.getHeight()));
        return starRank;
    }

    public void addItem(Journal a) {
        Container cc = new Container(BoxLayout.x());
        Container c = new Container(BoxLayout.y());
        Label nbchasse = new Label("Nombre Chasse : " + a.getNbChasse());
        Label animal = new Label("Animal : " + a.getAnimal().getNom_animal());
        Label lieu = new Label("Lieu : " + a.getLieu().getNom_lieu());
        Label desc = new Label("Description : " + a.getDescription());
        Label d = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(a.getDate()));
        Slider sl = new Slider();
        sl = createStarRankSlider();
        moy = a.getId_chasseur();
        sl.setProgress((int) moy);

        c.add(animal);
        c.add(lieu);
        c.add(nbchasse);
        c.add(d);
        c.add(desc);

        Image placeholder1 = Image.createImage(800, 800);
        EncodedImage en = EncodedImage.createFromImage(placeholder1, false);
        URLImage urli = URLImage.createToStorage(en, a.getImage(), "http://localhost/PIDEV/web/uploads/" + a.getImage());
        ImageViewer img = new ImageViewer();
        img.setImage(urli);
        c.add(img);
        Button service1 = new Button("score");
        service1.getAllStyles().setFgColor(ColorUtil.rgb(24, 242, 0));
        service1.setIcon(FontImage.createMaterial(FontImage.MATERIAL_REPLY, service1.getUnselectedStyle()));
        service1.addActionListener((ActionListener) (ActionEvent evt) -> {
            TrophyForm t = new TrophyForm();
            t.getF().show();
        });
        c.add(service1);
        c.add(FlowLayout.encloseCenter(sl));
        supp = new Button("Supprimer");
                supp.addActionListener((evt) -> {
                    ServiceJournal service = new ServiceJournal();
                    service.supprimerJournal(a);
                    ListJournauxForm l = new ListJournauxForm();
                    l.getF().show();
                });
                modif = new Button("Modifier");
                modif.addActionListener((evt) -> {
                    ModifJournal m = new ModifJournal(a);
                    m.getF().show();
                });
                c.add(modif);
                c.add(supp);

        f.add(c);

        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        f.refreshTheme();
    }

}
