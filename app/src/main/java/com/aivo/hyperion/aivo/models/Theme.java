package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.ThemePojo;

import java.io.IOException;
import java.util.ArrayList;

public class Theme {

    private String title;
    private int colorBackground;
    private int colorLine;

    private int colorMenuDefault;
    private int colorMenuContext;
    private int colorMenuText;

    private int colorMagnetGroup;
    private int colorMagnetGroupTitle;

    private int colorMagnetBackground;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Theme() {

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

    public int getColorLine() {
        return colorLine;
    }

    public void setColorLine(int colorLine) {
        this.colorLine = colorLine;
    }

    public int getColorMenuDefault() {
        return colorMenuDefault;
    }

    public void setColorMenuDefault(int colorMenuDefault) {
        this.colorMenuDefault = colorMenuDefault;
    }

    public int getColorMenuContext() {
        return colorMenuContext;
    }

    public void setColorMenuContext(int colorMenuContext) {
        this.colorMenuContext = colorMenuContext;
    }

    public int getColorMenuText() {
        return colorMenuText;
    }

    public void setColorMenuText(int colorMenuText) {
        this.colorMenuText = colorMenuText;
    }

    public int getColorMagnetGroup() {
        return colorMagnetGroup;
    }

    public void setColorMagnetGroup(int colorMagnetGroup) {
        this.colorMagnetGroup = colorMagnetGroup;
    }

    public int getColorMagnetGroupTitle() {
        return colorMagnetGroupTitle;
    }

    public void setColorMagnetGroupTitle(int colorMagnetGroupTitle) {
        this.colorMagnetGroupTitle = colorMagnetGroupTitle;
    }

    public int getColorMagnetBackground() {
        return colorMagnetBackground;
    }

    public void setColorMagnetBackground(int colorMagnetBackground) {
        this.colorMagnetBackground = colorMagnetBackground;
    }


}
