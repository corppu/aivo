package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.ChangeData;
import com.aivo.hyperion.aivo.models.actions.LineCreate;
import com.aivo.hyperion.aivo.models.actions.MagnetCreate;
import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mindmap {

    // Is this dirty?
    private boolean isDirty;

    // Local properties
    private String title;
    
    // List of magnet groups in the mindmap (Never null, use clear!)
    private List<MagnetGroup> magnetGroups;

    // List of lines in the mindmap (Never null, use clear!)
    private List<Line> lines;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        this.mediator = modelMediator_;
    }

    public Mindmap(ModelMediator mediator_, String title) {
        setMediator(mediator_);
        this.magnetGroups = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.title = title;
        this.isDirty = true;
    }

    // DO NOT USE! Only for ChangeData action!
    public void setData(String newTitle) { title = newTitle; }
    public String getTitle() { return title; }
    public List<MagnetGroup> getMagnetGroups() { return magnetGroups; }
    public List<Line> getLines() { return lines; }

    /** Change mindmap title through an action.
     *
     * @param newTitle
     */
    public void actionChangeData(String newTitle) {
        Action action = new ChangeData(this, newTitle);
        mediator.actionHandler.executeAction(action);
        mediator.notifyMindmapChanged();
    }

    /** Creates a magnet through an action.
     *
     * @param magnetGroup   Group to create magnet into.
     * @param rowIndex      Group row to create magnet into. If greater than count, creates new row.
     * @param colIndex      Group row position to create magnet into. Shifts elements right.
     *                      If greater than element count in row, magnet is created in the end.
     */
    public void actionCreateMagnet(MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        Action action = new MagnetCreate(mediator, magnetGroup, rowIndex, colIndex);
        mediator.actionHandler.executeAction(action);
        mediator.notifyMindmapChanged();
    }

    /** Creates a magnet through an action.
     *
     * @param pointF        Where a new MagnetGroup is created, to contain the new Magnet.
     */
    public void actionCreateMagnet(PointF pointF) {
        Action action = new MagnetCreate(mediator, pointF);
        mediator.actionHandler.executeAction(action);
        mediator.notifyMindmapChanged();
    }

    /** Creates a new line through an action.
     *
     * @param magnetGroup1  Connecting magnet group.
     * @param magnetGroup2  Connecting magnet group.
     */
    public void actionCreateLine(MagnetGroup magnetGroup1, MagnetGroup magnetGroup2) {
        Action action = new LineCreate(mediator, magnetGroup1, magnetGroup2);
        mediator.actionHandler.executeAction(action);
        mediator.notifyMindmapChanged();
    }

    /** Removes this Mindmap. IRREVERSIBLE!
     *
     */
    public void delete() {

    }
}
