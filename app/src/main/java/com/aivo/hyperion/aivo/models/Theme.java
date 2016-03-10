package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.ThemePojo;

import java.io.IOException;
import java.util.ArrayList;

public class Theme {

    static final private int themeAmount = 3;
    private int themeId;

    static final private String[] title =               {"Default", "Dark", "Beige"};
    static final private int[] colorBackground =        {0,         0,      0};
    static final private int[] colorLine =              {0,         0,      0};

    static final private int[] colorMenuDefault =       {0,         0,      0};
    static final private int[] colorMenuText =          {0,         0,      0};
    static final private int[] colorMenuIcon =          {0,         0,      0};

    static final private int[] colorMagnetGroup =       {0,         0,      0};
    static final private int[] colorMagnetGroupTitle =  {0,         0,      0};

    static final private int[] colorMagnetBackground =  {0,         0,      0};

    public Theme(final int themeId) {
        if (themeId >= themeAmount || themeId < 0)
            throw new InternalError("Theme created with an invalid theme id!");

        this.themeId = themeId;
    }

    public String getTitle() { return title[themeId]; }
    public int getColorBackground() { return colorBackground[themeId]; }
    public int getColorLine() { return colorLine[themeId]; }
    public int getColorMenuDefault() { return colorMenuDefault[themeId]; }
    public int getColorMenuText() { return colorMenuText[themeId]; }
    public int getColorMenuIcon() { return colorMenuIcon[themeId]; }
    public int getColorMagnetGroup() { return colorMagnetGroup[themeId]; }
    public int getColorMagnetGroupTitle() { return colorMagnetGroupTitle[themeId]; }
    public int getColorMagnetBackground() { return colorMagnetBackground[themeId]; }
}
