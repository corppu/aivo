package com.aivo.hyperion.aivo.models.pojos;

import android.graphics.Color;

/**
 * Created by MicroLoota on 22.12.2015.
 */
public class ThemePojo {

    private String createdAt;
    private String updateAt;
    private int userId;
    private int themeId;

    private String title;
    private int colorBackground;
    private int colorLine;
    private int colorMenu;
    private int colorMenuText;

    public ThemePojo() {
        this.colorBackground = Color.WHITE;
        this.colorLine = Color.BLACK;
        this.colorMenu = Color.YELLOW;
        this.colorMenuText = Color.BLACK;
    }
}
