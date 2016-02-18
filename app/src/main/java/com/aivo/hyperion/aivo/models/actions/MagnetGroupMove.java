package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.MagnetGroup;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public class MagnetGroupMove extends Action {

    private MagnetGroup magnetGroup;
    private PointF pointOld;
    private PointF pointNew;

    public MagnetGroupMove(MagnetGroup magnetGroup, PointF newPoint) {
        this.magnetGroup = magnetGroup;
        this.pointOld = new PointF(magnetGroup.getPoint().x, magnetGroup.getPoint().y);
        this.pointNew = new PointF(newPoint.x, newPoint.y);
    }

    @Override
    void execute() {
        magnetGroup.getPoint().set(pointNew);
    }

    @Override
    void undo() {
        magnetGroup.getPoint().set(pointOld);
    }
}
