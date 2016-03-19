package com.aivo.hyperion.aivo.models;

public class Theme {

    private int themeId;
    static final private int themeAmount = 3;

    static final private String[] title =                   {"Default", "Dark", "Beige"};
    static final private int[] colorBackground =            {0xFFFFFF, 0, 0};
    static final private int[] colorLine =                  {0xA5A9AC, 0, 0};
    static final private int[] colorMagnet =                {0xF7F3F3, 0, 0};
    static final private int[] colorMagnetGroup =           {0x7EDFFC, 0, 0};

    static final private int[] colorMenu =                  {0xF7F3F3, 0, 0};
    static final private int[] colorMenuItem =              {0x24C3FB, 0, 0};
    static final private int[] colorMenuText =              {0xA5A9AC, 0, 0};
    static final private int[] colorMenuSelection =         {0x2C94F4, 0, 0};

    static final private int[] colorMainMenu =              {0xBBE1F9, 0, 0};
    static final private int[] colorMainMenuItem =          {0x24C3FB, 0, 0};
    static final private int[] colorMainMenuText =          {0xF7F7F7, 0, 0};
    static final private int[] colorMainMenuSelection =     {0x2C94F4, 0, 0};

    static final private int[] colorContextMenu =           {0x24C3FB, 0, 0};
    static final private int[] colorContextMenuItem =       {0xF7F7F7, 0, 0};
    static final private int[] colorContextMenuSelection =  {0x2C94F4, 0, 0};

    public Theme(final int themeId) {
        if (themeId >= themeAmount || themeId < 0)
            throw new InternalError("Theme created with an invalid theme id!");

        this.themeId = themeId;
    }

    public int getThemeId() { return themeId; }
    public String getTitle() { return title[themeId]; }

    public int getColorBackground() { return colorBackground[themeId]; }
    public int getColorLine() { return colorLine[themeId]; }
    public int getColorMagnet() { return colorMagnet[themeId]; }
    public int getColorMagnetGroup() { return colorMagnetGroup[themeId]; }

    public int getColorMenu() { return colorMenu[themeId]; }
    public int getColorMenuItem() { return colorMenuItem[themeId]; }
    public int getColorMenuText() { return colorMenuText[themeId]; }
    public int getColorMenuSelection() { return colorMenuSelection[themeId]; }

    public int getColorMainMenu() { return colorMainMenu[themeId]; }
    public int getColorMainMenuItem() { return colorMainMenuItem[themeId]; }
    public int getColorMainMenuText() { return colorMainMenuText[themeId]; }
    public int getColorMainMenuSelection() { return colorMainMenuSelection[themeId]; }

    public int getColorContextMenu() { return colorContextMenu[themeId]; }
    public int getColorContextMenuIcon() { return colorContextMenuItem[themeId]; }
    public int getColorContextMenuSelection() { return colorContextMenuSelection[themeId]; }
}
