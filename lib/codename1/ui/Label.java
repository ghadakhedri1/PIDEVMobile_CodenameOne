/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores
 * CA 94065 USA or visit www.oracle.com if you need additional information or
 * have any questions.
 */
package com.codename1.ui;

import com.codename1.cloud.BindTarget;
import com.codename1.io.Log;
import com.codename1.ui.TextSelection.Char;
import com.codename1.ui.TextSelection.Span;
import com.codename1.ui.TextSelection.Spans;
import com.codename1.ui.TextSelection.TextSelectionSupport;
import com.codename1.ui.geom.*;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.LookAndFeel;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.EventDispatcher;

/**
 * <p>Allows displaying a single line of text and icon (both optional) with different alignment options. This class
 * is a base class for several components allowing them to declare alignment/icon
 * appearance universally.</p>
 * <p>
 * Label text can be positioned in one of 4 locations as such:
 * </p>
 * <script src="https://gist.github.com/codenameone/3bfd03a497bc09700128.js"></script>
 * <img src="https://www.codenameone.com/img/developer-guide/components-label-text-position.png" alt="Label text positioning" />
 * 
 * 
 * @author Chen Fishbein
 */
public class Label extends Component {
    /**
     * Fallback to the old default look and feel renderer for cases where compatibility is essential
     */
    private boolean legacyRenderer;
    private String text = "";
    
    private TextSelectionSupport textSelectionSupport;
    private boolean textSelectionEnabled;
    
    
    private Image icon;
    private Image maskedIcon;
    
    private int valign = BOTTOM;

    private int textPosition = RIGHT;
    
    private static int defaultGap = 2;
    
    private int gap = defaultGap;
    
    private int shiftText = 0;
    
    private boolean tickerRunning = false;
    private static boolean defaultTickerEnabled = true;
    private boolean tickerEnabled = defaultTickerEnabled;
    
    private long tickerStartTime;
    
    private long tickerDelay;
    
    private boolean rightToLeft;
    
    private boolean endsWith3Points = true;

    private Object mask;
    
    private String maskName;
    private EventDispatcher textBindListeners = null;
    private boolean shouldLocalize = true;
    private boolean showEvenIfBlank = false;
    private int shiftMillimeters = 1;
    private int stringWidthUnselected = -1;
    
    private boolean autoSizeMode;
    private float maxAutoSize = -1;
    private Font originalFont;
    private int widthAtLastCheck = -1;
    private char materialIcon;
    private float materialIconSize = -1;
    
    /** 
     * Constructs a new label with the specified string of text, left justified.
     * 
     * @param text the string that the label presents.
     */
    public Label(String text) {
        noBind = true;
        setUIID("Label");
        this.text = text;
        localize();
        setFocusable(false);
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }

    /** 
     * Constructs a new label with the specified string of text and uiid
     * 
     * @param text the string that the label presents.
     * @param uiid the uiid for the label
     */
    public Label(String text, String uiid) {
        noBind = true;
        this.text = text;
        localize();
        setFocusable(false);

        setUIID(uiid);
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }

    // workaround for potential infinite recursion situation https://github.com/codenameone/CodenameOne/commit/54a4092003b0ee5631c05250824a6466b84e757f#commitcomment-24244448
    private boolean autoSizeLaidOutLock;
    
    /**
     * {@inheritDoc}
     * This is overriden for auto size mode
     */
    @Override
    protected void laidOut() {
        if(autoSizeLaidOutLock) {
            return;
        }
        autoSizeLaidOutLock = true;
        super.laidOut();
        if(autoSizeMode) {
            initAutoResize();
        }
        autoSizeLaidOutLock = false;
    }
    
    
    
    /**
     * Construct an empty label
     */
    public Label() {
        this("");
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }

    /** 
     * Constructs a new label with the specified icon
     * 
     * @param icon the image that the label presents.
     */
    public Label(Image icon) {
        this("");
        this.icon = icon;
        if(icon != null && icon.requiresDrawImage()) {
            legacyRenderer = true;
        }
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }


    /** 
     * Constructs a new label with the specified icon and UIID
     * 
     * @param icon the image that the label presents.
     * @param uiid the uiid for the label
     */
    public Label(Image icon, String uiid) {
        this("", uiid);
        this.icon = icon;
        if(icon != null && icon.requiresDrawImage()) {
            legacyRenderer = true;
        }
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }
    
    /** 
     * Constructs a new label with the specified icon text and UIID
     * 
     * @param text the text of the label
     * @param icon the image that the label presents.
     * @param uiid the uiid for the label
     */
    public Label(String text, Image icon, String uiid) {
        this(text, uiid);
        this.icon = icon;
        if(icon != null && icon.requiresDrawImage()) {
            legacyRenderer = true;
        }
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }
    
    /** 
     * Constructs a new label with the specified icon and text
     * 
     * @param text the text of the label
     * @param icon the image that the label presents.
     */
    public Label(String text, Image icon) {
        this(text);
        this.icon = icon;
        if(icon != null && icon.requiresDrawImage()) {
            legacyRenderer = true;
        }
        endsWith3Points = UIManager.getInstance().getLookAndFeel().isDefaultEndsWith3Points();
    }
    
    /**
     * This method is shorthand for {@link com.codename1.ui.FontImage#setMaterialIcon(com.codename1.ui.Label, char)}
     * @param c one of the constants from {@link com.codename1.ui.FontImage}
     */
    public void setMaterialIcon(char c) {
        FontImage.setMaterialIcon(this, c);
        materialIconSize = -1;
        materialIcon = c;
    }
    
    /**
     * This method is shorthand for {@link com.codename1.ui.FontImage#setMaterialIcon(com.codename1.ui.Label, char, float)}
     * @param c one of the constants from {@link com.codename1.ui.FontImage}
     * @param size the size of the icon in millimeters
     */
    public void setMaterialIcon(char c, float size) {
        FontImage.setMaterialIcon(this, c, size);
        materialIconSize = size;
        materialIcon = c;
    }
    
    /**
     * Returns the material icon assigned to this component or 0 if not applicable
     * @return the material icon
     */
    public char getMaterialIcon() {
        return materialIcon;
    }
    
    /**
     * Returns the material icon size assigned to this component or 0/-1 if 
     * not applicable
     * @return the material icon size
     */
    public float getMaterialIconSize() {
        return materialIconSize;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getBaselineResizeBehavior() {
        switch(valign) {
            case TOP:
            return BRB_CONSTANT_ASCENT;
        case BOTTOM:
            return BRB_CONSTANT_DESCENT;
        case CENTER:
            return BRB_CENTER_OFFSET;
        }
        return BRB_OTHER;
    }

    @Override
    public int getBaseline(int width, int height) {
        Style s = getStyle();
        Font f = s.getFont();
        
        int innerHeight = height-s.getVerticalPadding();
        return s.getPaddingTop()+(innerHeight-f.getHeight())/2+f.getAscent();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUIID(String id) {
        super.setUIID(id);
        if(materialIcon != 0) {
            FontImage.setMaterialIcon(this, materialIcon, materialIconSize);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshTheme(boolean merge) {
        super.refreshTheme(merge);
        if(materialIcon != 0) {
            FontImage.setMaterialIcon(this, materialIcon, materialIconSize);
        }
    }
    
    /**
     * Sets the Label text
     * 
     * @param text the string that the label presents.
     */
    public void setText(String text){
        widthAtLastCheck = -1;
        this.text = text;
        localize();
        stringWidthUnselected = -1;
        setShouldCalcPreferredSize(true);
        repaint();
    }
    

    private void localize() {
        if(shouldLocalize) {
            this.text =  getUIManager().localize(text, text);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    void initComponentImpl() {
        super.initComponentImpl();
        UIManager manager = getUIManager();
        LookAndFeel lf = manager.getLookAndFeel();
        if(hasFocus()) {
            if(lf instanceof DefaultLookAndFeel) {
                ((DefaultLookAndFeel)lf).focusGained(this);
            }
        }
        // solves the case of a user starting a ticker before adding the component
        // into the container
        if(isTickerEnabled() && isTickerRunning() && !isCellRenderer()) {
            getComponentForm().registerAnimatedInternal(this);
        }
        checkAnimation();
        if(maskName != null && mask == null) {
            setMask(UIManager.getInstance().getThemeMaskConstant(maskName));
        }
        if(getIcon() != null) {
            getIcon().lock();
        }
    }

    /**
     * {@inheritDoc}
     */
    void deinitializeImpl() {
        super.deinitializeImpl(); 
        Form f = getComponentForm();
        if(f != null) {
            f.deregisterAnimated(this);
        }
        if(getIcon() != null) {
            getIcon().unlock();
        }
    }
    
    
    /**
     * Returns the label text
     * 
     * @return the label text
     */
    public String getText(){
        return text;
    }
    
    /**
     * Sets the Label icon, if the icon is unmodified a repaint would not be triggered
     * 
     * @param icon the image that the label presents.
     */
    public void setIcon(Image icon){
        if(this.icon == icon) {
            return;
        }
        widthAtLastCheck = -1;
        if(icon != null) {
            if(icon.requiresDrawImage()) {
                legacyRenderer = true;
            }

            if(mask != null) {
                maskedIcon = icon.applyMaskAutoScale(mask);
            }
        }
        this.icon = icon;
        setShouldCalcPreferredSize(true);
        checkAnimation();
        repaint();
    }
    
    void checkAnimation() {
        super.checkAnimation();
        if(icon != null && icon.isAnimation()) {
            Form parent = getComponentForm();
            if(parent != null) {
                // animations are always running so the internal animation isn't
                // good enough. We never want to stop this sort of animation
                parent.registerAnimated(this);
            }
        }
    }
    
    /**
     * Returns the labels icon
     * 
     * @return the labels icon
     */
    public Image getIcon(){
        return icon;
    }
    
    /**
     * Sets the Alignment of the Label to one of: CENTER, LEFT, RIGHT
     * 
     * @param align alignment value
     * @see #CENTER
     * @see #LEFT
     * @see #RIGHT
     * @deprecated use Style.setAlignment instead
     */
    public void setAlignment(int align){
        getSelectedStyle().setAlignment(align);
        getUnselectedStyle().setAlignment(align);
    }
    
    /**
     * Sets the vertical alignment of the Label to one of: CENTER, TOP, BOTTOM
     * <strong>The valign property is only relevant relatively to the icon and not the entire label, this will
     * only work when there is an icon</strong>
     * 
     * @param valign alignment value
     * @see #CENTER
     * @see #TOP
     * @see #BOTTOM
     */
    public void setVerticalAlignment(int valign) {
        if(valign != CENTER && valign != TOP && valign != BOTTOM){
            throw new IllegalArgumentException("Alignment can't be set to " + valign);
        }
        this.valign = valign;
    }

    /**
     * Returns the vertical alignment of the Label, this will only work when the icon
     * is in the side of the text and not above or below it.
     * <strong>The valign property is only relevant relatively to the icon and not the entire label, this will
     * only work when there is an icon</strong>
     * 
     * @return the vertical alignment of the Label one of: CENTER, TOP, BOTTOM
     * @see #CENTER
     * @see #TOP
     * @see #BOTTOM
     */
    public int getVerticalAlignment(){
        return valign;
    }
    
    /**
     * Returns the alignment of the Label
     * 
     * @return the alignment of the Label one of: CENTER, LEFT, RIGHT
     * @see #CENTER
     * @see #LEFT
     * @see #RIGHT
     * @deprecated use Style.getAlignment instead
     */
    public int getAlignment(){
        return getStyle().getAlignment();
    }

    /**
     * Sets the position of the text relative to the icon if exists
     *
     * @param textPosition alignment value (LEFT, RIGHT, BOTTOM or TOP)
     * @see #LEFT
     * @see #RIGHT
     * @see #BOTTOM
     * @see #TOP
     */
    public void setTextPosition(int textPosition) {
        if (textPosition != LEFT && textPosition != RIGHT && textPosition != BOTTOM && textPosition != TOP) {
            throw new IllegalArgumentException("Text position can't be set to " + textPosition);
        }
        this.textPosition = textPosition;
    }

    
    /**
     * Returns The position of the text relative to the icon
     * 
     * @return The position of the text relative to the icon, one of: LEFT, RIGHT, BOTTOM, TOP
     * @see #LEFT
     * @see #RIGHT
     * @see #BOTTOM
     * @see #TOP
     */
    public int getTextPosition(){
        return textPosition;
    }
    
    /**
     * Set the gap in pixels between the icon/text to the Label boundaries
     * 
     * @param gap the gap in pixels
     */
    public void setGap(int gap) {
        this.gap = gap;
    }
    
    /**
     * Returns the gap in pixels between the icon/text to the Label boundaries
     * 
     * @return the gap in pixels between the icon/text to the Label boundaries
     */
    public int getGap() {
        return gap;
    }
    
    /**
     * Set the default gap in pixels between the icon/text to the Label boundaries
     * 
     * @param gap the gap in pixels
     */
    public static void setDefaultGap(int gap) {
        defaultGap = gap;
    }
    
    /**
     * Returns the default gap in pixels between the icon/text to the Label boundaries
     * 
     * @return the gap in pixels between the icon/text to the Label boundaries
     */
    public static int getDefaultGap() {
        return defaultGap;
    }
    
    /**
     * {@inheritDoc}
     */
    protected String paramString() {
        return super.paramString() + ", text = " +getText() + ", gap = " + gap;
    }
    
    /**
     * {@inheritDoc}
     */
    public void paint(Graphics g) {
        if(legacyRenderer) {
            initAutoResize();
            getUIManager().getLookAndFeel().drawLabel(g, this);
            return;
        }
        paintImpl(g);
    }

    void paintImpl(Graphics g) {
        initAutoResize();
        Object icn = null;
        Image i = getIconFromState();
        if(i != null) {
            icn = i.getImage();
        } else {
            // optimize away a common usage pattern for drawing the background only
            if(text == null || text.equals("") || text.equals(" ")) {
                return;
            }
        }
        //getUIManager().getLookAndFeel().drawLabel(g, this);
        int cmpX = getX() + g.getTranslateX();
        int cmpY = getY() + g.getTranslateY();
        int cmpHeight = getHeight();
        int cmpWidth = getWidth();
        Style s = getStyle();
        Font f = s.getFont();
        String t = text;
        if(text == null) { 
            t = "";
        }
        Display.impl.drawLabelComponent(g.getGraphics(), cmpX, cmpY, cmpHeight, cmpWidth, s, t, 
                icn, null, 0, gap, isRTL(), false, textPosition, getStringWidth(f), tickerRunning, shiftText, 
                endsWith3Points, valign);
    }
    
    /**
     * Allows us to limit the maximum size for the autosize mode
     * @param maxSize the maximum font size in millimeters
     */
    public void setMaxAutoSize(float maxSize) {
        maxAutoSize = maxSize;
    }

    /**
     * Allows us to limit the maximum size for the autosize mode
     * 
     * @return the maximum font size in millimeters
     */
    public float getMaxAutoSize() {
        return maxAutoSize;
    }
    
    void initAutoResize() {
        if(autoSizeMode) {
            Style s = getUnselectedStyle();
            int p = s.getHorizontalPadding();
            int w = getWidth();
            if(w > p + 10) {
                if(originalFont == null) {
                    originalFont = s.getFont();
                } else {
                    if(w == widthAtLastCheck) {
                        return;
                    }
                }
                
                Font currentFont = getUnselectedStyle().getFont();
                float fontSize = currentFont.getPixelSize();
                if(fontSize < 1) {
                    Log.p("Autosize disabled probably because component wasn't using native fonts for UIID: " + getUIID());
                    autoSizeMode = false;
                    return;
                }
                widthAtLastCheck = w;
                autoSizeMode = false;
                int currentWidth = calcPreferredSize().getWidth();
                int maxSizePixel = Display.getInstance().convertToPixels(maxAutoSize);
                while(currentWidth < w) {
                    fontSize++;
                    if(fontSize >= maxSizePixel) {
                        fontSize = maxSizePixel;
                        currentFont = currentFont.derive(maxSizePixel, currentFont.getStyle());
                        getAllStyles().setFont(currentFont);
                        currentWidth = calcPreferredSize().getWidth();
                        break;
                    }
                    currentFont = currentFont.derive(fontSize, currentFont.getStyle());
                    getAllStyles().setFont(currentFont);
                    currentWidth = calcPreferredSize().getWidth();
                }
                while(currentWidth > w) {
                    fontSize--;
                    currentFont = currentFont.derive(fontSize, currentFont.getStyle());
                    getAllStyles().setFont(currentFont);
                    currentWidth = calcPreferredSize().getWidth();
                }
                autoSizeMode = true;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected Dimension calcPreferredSize(){
        return getUIManager().getLookAndFeel().getLabelPreferredSize(this);
    }
    
    /**
     * Simple getter to return how many pixels to shift the text inside the Label

     * @return number of pixels to shift
     */
    public int getShiftText() {
        return shiftText;
    }

    /**
     * This method shifts the text from it's position in pixels.
     * The value can be positive/negative to move the text to the right/left
     * 
     * @param shiftText The number of pixels to move the text
     */
    public void setShiftText(int shiftText) {
        this.shiftText = shiftText;
    }
    
    /**
     * Returns true if a ticker should be started since there is no room to show
     * the text in the label.
     * 
     * @return true if a ticker should start running
     */
    public boolean shouldTickerStart() {
        if(!tickerEnabled){
            return false;
        }
        Style style = getStyle();
        int txtW = style.getFont().stringWidth(getText());
        int textSpaceW = getAvaliableSpaceForText();
        return txtW > textSpaceW && textSpaceW > 0;
    }

    Image getIconFromState() {
        return getMaskedIcon();
    }

    int getAvaliableSpaceForText() {
        Style style = getStyle();
        int textSpaceW = getWidth() - style.getHorizontalPadding();
        Image icon = getIconFromState();

        if (icon != null && (getTextPosition() == Label.RIGHT || getTextPosition() == Label.LEFT)) {
            textSpaceW = textSpaceW - icon.getWidth();
        }
        return textSpaceW;
    }

    /**
     * This method will start the text ticker
     */
    public void startTicker() {
        startTicker(getUIManager().getLookAndFeel().getTickerSpeed(), true);
    }

    /**
     * This method will start the text ticker
     * 
     * @param delay the delay in millisecods between animation intervals
     * @param rightToLeft if true move the text to the left
     */
    public void startTicker(long delay, boolean rightToLeft){
        //return if ticker is not enabled
        if(!tickerEnabled){
            return;
        }
        if(!isCellRenderer()){
            Form parent = getComponentForm();
            if(parent != null) {
                parent.registerAnimatedInternal(this);
            }
        }
        tickerStartTime = System.currentTimeMillis();
        tickerDelay = delay;
        tickerRunning = true;
        this.rightToLeft = rightToLeft;
        if (isRTL()) {
        	this.rightToLeft = !this.rightToLeft;
        }
    }
    
    /**
     * Stops the text ticker
     */
    public void stopTicker(){
        tickerRunning = false;
        setShiftText(0);
        deregisterAnimatedInternal();
    }

    /**
     * {@inheritDoc}
     */
    void tryDeregisterAnimated() {
        if(tickerEnabled || tickerRunning) {
            return;
        }
        super.tryDeregisterAnimated();
    }

    /**
     * Returns true if the ticker is running
     * 
     * @return true if the ticker is running
     */
    public boolean isTickerRunning() {
        return tickerRunning;
    }

    /**
     * Sets the Label to allow ticking of the text.
     * By default is true
     * 
     * @param tickerEnabled
     */
    public void setTickerEnabled(boolean tickerEnabled) {
        this.tickerEnabled = tickerEnabled;
    }

    /**
     * This method return true if the ticker is enabled on this Label
     * 
     * @return tickerEnabled
     */
    public boolean isTickerEnabled() {
        return tickerEnabled;
    }
   
    /**
     * If the Label text is too long fit the text to the widget and adds "{@code ...}"
     * points at the end. By default this is set to {@code false} for faster performance.
     * 
     * @param endsWith3Points true if text should add "..." at the end
     */
    public void setEndsWith3Points(boolean endsWith3Points){
        this.endsWith3Points = endsWith3Points;
    }

    /**
     * If the Label text is too long fit the text to the widget and adds "{@code ...}"
     * points at the end. By default this is set to {@code false} for faster performance.
     * 
     * @return true if this Label adds "..." when the text is too long
     */
    public boolean isEndsWith3Points() {
        return endsWith3Points;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public boolean animate() {
        boolean animateTicker = false;
        if(tickerRunning && tickerStartTime + tickerDelay < System.currentTimeMillis()){
            tickerStartTime = System.currentTimeMillis();
            if(rightToLeft){
                shiftText -= Display.getInstance().convertToPixels(shiftMillimeters, true);
                if(shiftText + getStringWidth(getStyle().getFont()) < 0) {
                    shiftText = getStringWidth(getStyle().getFont()); 
                }
            }else{
                shiftText += Display.getInstance().convertToPixels(shiftMillimeters, true);
                if(getStringWidth(getStyle().getFont()) - shiftText < 0) {
                    shiftText = -getStringWidth(getStyle().getFont()); 
                }
            }     
            animateTicker = true;
        }                
        // if we have an animated icon then just let it do its thing...
        boolean val = icon != null && icon.isAnimation() && icon.animate();
        boolean parent = super.animate();
        return  val || parent || animateTicker;
    }

    /**
     * Allows disabling/enabling tickers globally
     *
     * @return the defaultTickerEnabled
     */
    public static boolean isDefaultTickerEnabled() {
        return defaultTickerEnabled;
    }

    /**
     * Allows disabling/enabling tickers globally
     * @param aDefaultTickerEnabled the defaultTickerEnabled to set
     */
    public static void setDefaultTickerEnabled(boolean aDefaultTickerEnabled) {
        defaultTickerEnabled = aDefaultTickerEnabled;
    }

    
    /**
     * A mask image can be applied to the label (see the image mask method for details)
     * which allows for things like rounded image appearance etc.
     * 
     * @param mask the mask returned from the image object
     */
    public void setMask(Object mask) {
        this.mask = mask;
    }
    
    /**
     * Returns the mask matching the given image
     * 
     * @return the mask for the given label
     */
    public Object getMask() {
        return mask;
    }

    /**
     * Determines the name of the mask from the image constants thus allowing the mask to be applied from the theme
     * @return the maskName
     */
    public String getMaskName() {
        return maskName;
    }

    /**
     * Determines the name of the mask from the image constants thus allowing the mask to be applied from the theme
     * @param maskName the maskName to set
     */
    public void setMaskName(String maskName) {
        this.maskName = maskName;
        setMask(UIManager.getInstance().getThemeMaskConstant(maskName));
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames() {
        return new String[] {"maskName"};
    }

    /**
     * {@inheritDoc}
     */
    public Class[] getPropertyTypes() {
       return new Class[] { String.class };
    }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertyTypeNames() {
        return new String[] {"String"};
    }

    /**
     * {@inheritDoc}
     */
    public Object getPropertyValue(String name) {
        if(name.equals("maskName")) {
            return getMaskName();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String setPropertyValue(String name, Object value) {
        if(name.equals("maskName")) {
            setMaskName((String)value);
            return null;
        }
        return super.setPropertyValue(name, value);
    }
    
    /**
     * If a mask is applied returns the icon with a mask, otherwise returns the icon
     * @return the icon masked or otherwise
     */
    public Image getMaskedIcon() {
        if(maskedIcon != null) {
            return maskedIcon;
        }
        if(mask != null) {
            if(icon != null) {
                maskedIcon = icon.applyMaskAutoScale(mask);
                return maskedIcon;
            }
        }
        return icon;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getBindablePropertyNames() {
        return new String[] {"text"};
    }
    
    /**
     * {@inheritDoc}
     */
    public Class[] getBindablePropertyTypes() {
        return new Class[] {String.class};
    }
    
    /**
     * {@inheritDoc}
     */
    public void bindProperty(String prop, BindTarget target) {
        if(prop.equals("text")) {
            if(textBindListeners == null) {
                textBindListeners = new EventDispatcher();
            }
            textBindListeners.addListener(target);
            return;
        }
        super.bindProperty(prop, target);
    }
    
    /**
     * {@inheritDoc}
     */
    public void unbindProperty(String prop, BindTarget target) {
        if(prop.equals("text")) {
            if(textBindListeners == null) {
                return;
            }
            textBindListeners.removeListener(target);
            if(!textBindListeners.hasListeners()) {
                textBindListeners = null;
            }
            return;
        }
        super.unbindProperty(prop, target);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getBoundPropertyValue(String prop) {
        if(prop.equals("text")) {
            return getText();
        }
        return super.getBoundPropertyValue(prop);
    }

    /**
     * {@inheritDoc}
     */
    public void setBoundPropertyValue(String prop, Object value) {
        if(prop.equals("text")) {
            setText((String)value);
            return;
        }
        super.setBoundPropertyValue(prop, value);
    }

    /**
     * Indicates if text should be localized when set to the label, by default
     * all text is localized so this allows disabling automatic localization for 
     * a specific label.
     * @return the shouldLocalize value
     */
    public boolean isShouldLocalize() {
        return shouldLocalize;
    }

    /**
     * Indicates if text should be localized when set to the label, by default
     * all text is localized so this allows disabling automatic localization for 
     * a specific label.
     * @param shouldLocalize the shouldLocalize to set
     */
    public void setShouldLocalize(boolean shouldLocalize) {
        this.shouldLocalize = shouldLocalize;
    }

    /**
     * Returns the number of millimeters that should be shifted in tickering
     * 
     * @return the shiftMillimeters
     */
    public int getShiftMillimeters() {
        return shiftMillimeters;
    }

    /**
     * Sets the millimeters that should be shifted in tickering
     * 
     * @param shiftMillimeters the shiftMillimeters to set
     */
    public void setShiftMillimeters(int shiftMillimeters) {
        this.shiftMillimeters = shiftMillimeters;
    }

    /**
     * By default labels and subclasses become 0 sized when they are blank even ignoring their padding
     * setting this to true makes the padding take effect even in a blank field.
     * @return the showEvenIfBlank
     */
    public boolean isShowEvenIfBlank() {
        return showEvenIfBlank;
    }

    /**
     * By default labels and subclasses become 0 sized when they are blank even ignoring their padding
     * setting this to true makes the padding take effect even in a blank field.
     * @param showEvenIfBlank the showEvenIfBlank to set
     */
    public void setShowEvenIfBlank(boolean showEvenIfBlank) {
        this.showEvenIfBlank = showEvenIfBlank;
    }    
    
    /**
     * This method is equivalent to label.getStyle().getFont().stringWidth(label.getText()) but its faster
     * @param fnt the font is passed as an optimization to save a call to getStyle
     * @return the string width
     */
    public int getStringWidth(Font fnt) {
        if(isUnselectedStyle) {
            // very optimized way to get the string width of a label for the common unselected case in larger lists
            if(stringWidthUnselected < 0) {
                stringWidthUnselected = fnt.stringWidth(text);
            }
            return stringWidthUnselected;
        }
        return fnt.stringWidth(text);
    }

    /**
     * Fallback to the old default look and feel renderer for cases where compatibility is essential
     * @return the legacyRenderer
     */
    public boolean isLegacyRenderer() {
        return legacyRenderer;
    }

    /**
     * Fallback to the old default look and feel renderer for cases where compatibility is essential
     * @param legacyRenderer the legacyRenderer to set
     */
    public void setLegacyRenderer(boolean legacyRenderer) {
        this.legacyRenderer = legacyRenderer;
    }

    @Override
    public void styleChanged(String propertyName, Style source) {
        super.styleChanged(propertyName, source);
        // If we're using a custom font, we need to use the legacy renderer.
        if (Style.FONT.equals(propertyName) && source.getFont() instanceof CustomFont) {
            setLegacyRenderer(true);
        }
    }

    /**
     * Autosize mode automatically shrinks/grows the font of the label to fit in the available width, it carries
     * a noticeable performance penalty and we recommend you avoid using it unless absolutely necessary
     * @return the autoSizeMode
     */
    public boolean isAutoSizeMode() {
        return autoSizeMode;
    }

    /**
     * Autosize mode automatically shrinks/grows the font of the label to fit in the available width, it carries
     * a noticeable performance penalty and we recommend you avoid using it unless absolutely necessary
     * @param autoSizeMode the autoSizeMode to set
     */
    public void setAutoSizeMode(boolean autoSizeMode) {
        this.autoSizeMode = autoSizeMode;
    }
    
    /**
     * Enables text selection on this label.  Text selection must also be enabled on the Form in order to
     * text selection to be activated.
     * @param enabled 
     * @see #setTextSelectionEnabled(boolean) 
     * @see Form#getTextSelection() 
     * @see TextSelection#setEnabled(boolean) 
     * @since 7.0
     */
    public void setTextSelectionEnabled(boolean enabled) {
        this.textSelectionEnabled = enabled;
        if (enabled) {
            setCursor(Component.TEXT_CURSOR);
        }
    }
    
    /**
     * Returns true if text selection is enabled on this label.  Default is {@literal false}.  To enable text selection,
     * you must enable text selection on the Form with {@link Form#getTextSelection() } and {@link TextSelection#setEnabled(boolean) },
     * and also ensure that the label's text selection is enabled via {@link #setTextSelectionEnabled(boolean) }.
     * @return 
     * @see #setTextSelectionEnabled(boolean) 
     * @since 7.0
     */
    public boolean isTextSelectionEnabled() {
        return textSelectionEnabled;
    }
    
    private Span span;
    public TextSelectionSupport getTextSelectionSupport() {
        if (textSelectionSupport == null) {
            textSelectionSupport = new TextSelectionSupport() {
                @Override
                public TextSelection.Spans getTextSelectionForBounds(TextSelection sel, Rectangle bounds) {
                    span = getUIManager().getLookAndFeel().calculateLabelSpan(sel, Label.this);
                    if (span == null) {
                        return sel.newSpans();
                    }
                    
                    Span result = span.getIntersection(bounds, true);
                    if (bounds.getY() < getY()) {
                        
                    }
                    Spans out = sel.newSpans();
                    out.add(result);
                    return out;
                            
                }

                @Override
                public boolean isTextSelectionEnabled(TextSelection sel) {
                    return textSelectionEnabled;
                }

                @Override
                public boolean isTextSelectionTriggerEnabled(TextSelection sel) {
                    return textSelectionEnabled;
                }

                @Override
                public Span triggerSelectionAt(TextSelection sel, int x, int y) {
                    span = getUIManager().getLookAndFeel().calculateLabelSpan(sel, Label.this);
                    if (span.size() == 0) {
                        return null;
                    }
                    Char charAtPoint = span.charAt(x, y);
                    if (charAtPoint == null) {
                        return null;
                    }
                    
                    int startPos = charAtPoint.getPosition();
                    int endPos = charAtPoint.getPosition()+1;
                    String dividers = " \t\r\n-.;";
                    
                    while (startPos > span.first().getPosition()) {
                        if (dividers.indexOf(Label.this.getText().substring(startPos, startPos+1)) < 0) {
                            startPos--;
                        } else {
                            if (startPos < span.last().getPosition()) {
                                startPos++;
                            }
                            break;
                        }
                    }
                    
                    while (endPos < span.last().getPosition()+1) {
                        if (dividers.indexOf(Label.this.getText().substring(endPos-1, endPos))<0) {
                            endPos++;
                        } else {
                            if (endPos > span.first().getPosition()) {
                                endPos--;
                            }
                            break;
                        }
                    }
                    System.out.println("Text is ["+getText().substring(startPos, endPos)+"]");
                    return span.subspan(startPos, endPos);
                }

                @Override
                public String getTextForSpan(TextSelection sel, Span span) {
                    int offset = span.getStartPos();
                    offset = Math.max(0, offset);
                    offset = Math.min(getText().length()-1, offset);
                    int end = span.getEndPos();
                    end = Math.min(getText().length(), end);
                    return getText().substring(offset, end);
                    
                }
                
            };
        }
        return textSelectionSupport;
    };
            
}
