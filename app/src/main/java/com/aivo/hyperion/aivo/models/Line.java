package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.LineChangeData;
import com.aivo.hyperion.aivo.models.actions.LineDelete;

import java.util.ArrayList;

public class Line {

    // Local properties
    private MagnetGroup magnetGroup1;
    private MagnetGroup magnetGroup2;
    private ArrayList<PointF> points;
    private int type;
    private int color;
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


    public MagnetGroup getMagnetGroup1() { return magnetGroup1; }
    public MagnetGroup getMagnetGroup2() { return magnetGroup2; }
    public ArrayList<PointF> getPoints() { return points; }
    public int getType() { return type; }
    public int getColor() { return color; }
    public int getThickness() { return thickness; }

    // DO NOT USE! Only for LineChangeData action!
    public void setData(final int newType, final int newColor, final int newThickness) {
        type = newType;
        color = newColor;
        thickness = newThickness;
    }

    public void actionChangeType(final int newType) {
        Action action = new LineChangeData(this, newType, color, thickness);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeColor(final int newColor) {
        Action action = new LineChangeData(this, type, newColor, thickness);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeThickness(final int newThickness) {
        Action action = new LineChangeData(this, type, color, newThickness);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionAddPoint(PointF newPoint, final int targetIndex) {

    }

    public void actionRemovePoint(PointF point) {

    }

    public void actionDelete() {
        Action action = new LineDelete(mediator, this);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }
}
