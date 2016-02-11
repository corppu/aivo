package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;
import android.util.Log;

import com.aivo.hyperion.aivo.models.pojos.LinePojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Line {

    // Local properties
    private MagnetGroup magnetGroup1;
    private MagnetGroup magnetGroup2;
    private ArrayList<PointF> points;
    private int type;
    private int thickness;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Line created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Line(ModelMediator mediator_, MagnetGroup magnetGroup1, MagnetGroup magnetGroup2) {
        setMediator(mediator_);
        this.magnetGroup1 = magnetGroup1;
        this.magnetGroup2 = magnetGroup2;
    }

    // Debug (or not) functions

    public void setMagnetGroup1(MagnetGroup magnetGroup1) {
        this.magnetGroup1 = magnetGroup1;
    }
    public void setMagnetGroup2(MagnetGroup magnetGroup2) {
        this.magnetGroup2 = magnetGroup2;
    }
    public void setPoints(ArrayList<PointF> points) {
        this.points = points;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    // End of debug functions

    public MagnetGroup getMagnetGroup1() {
        return magnetGroup1;
    }
    public MagnetGroup getMagnetGroup2() {
        return magnetGroup2;
    }
    public ArrayList<PointF> getPoints() {
        return points;
    }
    public int getType() {
        return type;
    }
    public int getThickness() {
        return thickness;
    }

    public void changeType(final int newType) {

    }

    public void changeThickness(final int newThickness) {

    }

    public void addPoint(PointF newPoint, final int targetIndex) {

    }

    public void removePoint(PointF point) {

    }
}
