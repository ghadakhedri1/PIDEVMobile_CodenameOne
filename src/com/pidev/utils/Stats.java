/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pidev.utils;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.io.JSONParser;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.pidev.entities.Journal;
import com.pidev.gui.ListJournauxForm;
import com.pidev.services.ServiceJournal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ghada
 */
public class Stats {
   
      List<Journal> Produits = new ArrayList<>();
    
   /**
     * Creates a renderer for the specified colors.
     */
    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Builds a category series using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add("Produit " + ++k, value);
        }

        return series;
    
    }
    
    

    public Form createPieChartForm() {

        // Generate the values
   
        double[] values = new double[]{14, 3, 4, 1, 12};

        // Set up the renderer
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
       
        renderer.setChartTitleTextSize(20);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
        r.setGradientEnabled(true);
        r.setGradientStart(0, ColorUtil.BLUE);
        r.setGradientStop(0, ColorUtil.GREEN);
        r.setHighlighted(true);


        // Create the chart ... pass the values and renderer to the chart object.
        

                JSONParser json = new JSONParser();

 
          
        PieChart chart = new PieChart(buildCategoryDataset("quantite produits", values), renderer);

        // Wrap the chart in a Component so we can add it to a form
        ChartComponent c = new ChartComponent(chart);

        // Create a form and show it.
        Form f = new Form("Statistique");
        
        f.setLayout(new BorderLayout());

        f.addComponent(BorderLayout.CENTER, c); 
    /* f.getToolbar().addMaterialCommandToSideMenu("retour", FontImage.MATERIAL_REPORT, e -> new AffichageProduit().getAll()  ); */
    
    Style s =UIManager.getInstance().getComponentStyle("Title");
   
Button back =new Button();
    back.getAllStyles().setAlignment(Component.RIGHT);
    FontImage backIcon= FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK,s);
    
    f.getToolbar().addCommandToLeftBar("",backIcon,(e)->{new ListJournauxForm().getF().show();});

        return f;
    
}
}
