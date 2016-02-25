package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.ChangeData;
import com.aivo.hyperion.aivo.models.actions.MagnetGroupMove;

import java.util.ArrayList;
import java.util.List;

public class MagnetGroup {

    // Local properties
    private String title;
    private PointF point;

    // List of magnets in the magnet group (Never null, use clear!)
    protected List< List<Magnet> > magnets;

    // List of lines that are connected to this group
    protected List< Line > lines;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("MagnetGroup created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public MagnetGroup(ModelMediator mediator_, PointF point) {
        setMediator(mediator_);
        this.magnets = new ArrayList<>();
        this.point = point;
        this.lines = new ArrayList<>();
    }

    // DO NOT USE! Only for ChangeData action!
    public void setData(String newTitle) { title = newTitle; }
    public PointF getPoint() { return point; }
    public String getTitle() { return title;  }
    public List< List<Magnet> > getMagnets() { return magnets; }
    public List< Line > getLines() { return lines; }

    public void actionCreateLine(MagnetGroup magnetGroup) {
        mediator.getMindmap().actionCreateLine(this, magnetGroup);
    }

    public void actionCreateMagnet(final int rowIndex, final int colIndex) {
        mediator.getMindmap().actionCreateMagnet(this, rowIndex, colIndex);
    }

    public void actionChangeData(String newTitle) {
        Action action = new ChangeData(this, newTitle);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(PointF newPoint) {
        Action action = new MagnetGroupMove(this, newPoint);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(MagnetGroup magnetGroupToMergeInto, final boolean keepLines) {

    }
}
