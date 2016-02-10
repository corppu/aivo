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
    protected List< List<Magnet> > magnets;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("MagnetGroup created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public MagnetGroup(ModelMediator mediator_) {
        setMediator(mediator_);
        magnets = new ArrayList<>();
    }

    // Debug (or not) functions

    public void setMagnets(List< List<Magnet> > magnets) {
        this.magnets = magnets;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPoint(PointF point) {
        this.point = point;
    }

    // End of debug functions

    public PointF getPoint() {
        return point;
    }
    public String getTitle() {
        return title;
    }
    public List< List<Magnet> > getMagnets() {
        return magnets;
    }

    public void changeTitle(String newTitle) {

    }

    public void moveToPoint(PointF newPoint) {

    }

    public void moveToMagnetGroup(MagnetGroup magnetGroupToMergeInto, final boolean keepLines) {

    }
}
