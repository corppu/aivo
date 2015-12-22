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
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.themeId = -1;

        this.title = "New Theme";
        this.colorBackground = Color.WHITE;
        this.colorLine = Color.BLACK;
        this.colorMenu = Color.rgb(255,250,205); // lemonchiffon
        this.colorMenuText = Color.BLACK;
    }

    public int getColorLine() {
        return colorLine;
    }

    public void setColorLine(int colorLine) {
        this.colorLine = colorLine;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public int getColorMenu() {
        return colorMenu;
    }

    public void setColorMenu(int colorMenu) {
        this.colorMenu = colorMenu;
    }

    public int getColorMenuText() {
        return colorMenuText;
    }

    public void setColorMenuText(int colorMenuText) {
        this.colorMenuText = colorMenuText;
    }
}
