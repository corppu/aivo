package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

public class Magnet {

    // Local properties
    private String title;
    private String content;
    private int color;
    private MagnetGroup magnetGroup;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Magnet(ModelMediator mediator_) {
        setMediator(mediator_);
    }

    // Debug (or not) functions

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setColor(int color) {
        this.color = color;
    }

    // End of debug functions

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public int getColor() {
        return color;
    }
    public MagnetGroup getMagnetGroup() {
        return magnetGroup;
    }
    public boolean hasImage() {
        return false; // TODO
    }
    public boolean hasVideo() {
        return false; // TODO
    }

    public void changeTitle(String newTitle) {

    }

    public void changeContent(String newContent) {

    }

    public void changeColor(final int newColor) {

    }

    public void moveToMagnetGroup(MagnetGroup newMagnetGroup, final int rowIndex, final int colIndex) {

    }

    public void moveToPoint(PointF newPoint) {

    }
}
