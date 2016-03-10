package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.LinePointAdd;
import com.aivo.hyperion.aivo.models.actions.LineChangeData;
import com.aivo.hyperion.aivo.models.actions.LineDelete;
import com.aivo.hyperion.aivo.models.actions.LinePointDelete;

import java.util.ArrayList;

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


    public MagnetGroup getMagnetGroup1() { return magnetGroup1; }
    public MagnetGroup getMagnetGroup2() { return magnetGroup2; }
    public ArrayList<PointF> getPoints() { return points; }
    public int getType() { return type; }
    public int getThickness() { return thickness; }

    // DO NOT USE! Only for LineChangeData action!
    public void setData(final int newType, final int newThickness) {
        type = newType;
        thickness = newThickness;
    }

    public void actionChangeType(final int newType) {
        Action action = new LineChangeData(mediator, this, newType, thickness);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeThickness(final int newThickness) {
        Action action = new LineChangeData(mediator, this, type, newThickness);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionAddPoint(PointF newPoint, final int targetIndex) {
        Action action = new LinePointAdd(mediator, this, newPoint, targetIndex);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionRemovePoint(PointF point) {
        Action action = new LinePointDelete(mediator, this, point);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionDelete() {
        Action action = new LineDelete(mediator, this);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }
}
