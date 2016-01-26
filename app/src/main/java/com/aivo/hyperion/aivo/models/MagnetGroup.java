package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.pojos.MagnetGroupPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MagnetGroup {

    // Local properties
    private String title;
    private PointF point;

    // List of magnets in the magnet group (Never null, use clear!)
    protected ArrayList<Magnet> magnets;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("MagnetGroup created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public MagnetGroup() {

    }

    public ArrayList<Magnet> getMagnets() {
        return new ArrayList<Magnet>(magnets);
    }

    public void setMagnets(ArrayList<Magnet> magnets) {
        this.magnets = magnets;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }


}
