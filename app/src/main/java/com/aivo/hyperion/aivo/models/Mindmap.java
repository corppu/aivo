package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.ActionHandler;
import com.aivo.hyperion.aivo.models.actions.LineCreate;
import com.aivo.hyperion.aivo.models.actions.MagnetCreate;
import com.aivo.hyperion.aivo.models.actions.MagnetCreateChild;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mindmap {

    // The ActioHandler
    protected ActionHandler actionHandler;

    // Is this dirty?
    private boolean isDirty;

    // Local properties
    private String title;
    
    // List of magnet groups in the mindmap (Never null, use clear!)
    private List<MagnetGroup> magnetGroups;

    // List of lines in the mindmap (Never null, use clear!)
    private List<Line> lines;

    // Global id counter for this mindmap, always use "idCounter++"
    private int idCounter;
    protected int getNextId() { return idCounter++; }

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Mindmap created without a valid ModelMediator reference!");
        this.mediator = modelMediator_;
    }

    protected Mindmap(ModelMediator mediator_, String title) {
        setMediator(mediator_);
        this.actionHandler = new ActionHandler();
        this.magnetGroups = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.title = title;
        this.isDirty = true;
    }

    protected Mindmap(ModelMediator mediator_, JSONObject json) {
        setMediator(mediator_);
        this.actionHandler = new ActionHandler();
        this.magnetGroups = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.isDirty = false;

        try {
            this.title = json.getString("title");
            this.idCounter = json.getInt("idCounter");

            JSONArray jsonMagnetGroups = json.getJSONArray("magnetGroups");
            MagnetGroup magnetGroup;
            for (int j = 0; j < jsonMagnetGroups.length(); ++j) {
                magnetGroup = new MagnetGroup(mediator, jsonMagnetGroups.getJSONObject(j));
                magnetGroups.add(magnetGroup);
            }

            JSONArray jsonLines = json.getJSONArray("lines");
            Line line;
            for (int j = 0; j < jsonLines.length(); ++j) {
                line = new Line(mediator, jsonMagnetGroups.getJSONObject(j));
                lines.add(line);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() { return title; }
    public List<MagnetGroup> getMagnetGroups() { return magnetGroups; }
    public List<Line> getLines() { return lines; }
    public ActionHandler getActionHandler() { return actionHandler; }

    /** Change mindmap title through an action.
     *
     * @param newTitle      New title
     */
    public void changeTitle(String newTitle) {
        title = newTitle;
        for (ModelListener listener : mediator.getListeners()) listener.onMindmapTitleChange(this);
    }

    /** Creates a magnet through an action into a existing magnet group.
     *
     * @param magnetGroup   Group to create magnet into.
     * @param rowIndex      Group row to create magnet into. If greater than count, creates new row.
     * @param colIndex      Group row position to create magnet into. Shifts elements right.
     *                      If greater than element count in row, magnet is created in the end.
     */
    public void actionCreateMagnet(MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        Action action = new MagnetCreate(mediator, magnetGroup, rowIndex, colIndex);
        getActionHandler().executeAction(action);
    }
    public void actionCreateMagnet(MagnetGroup magnetGroup, final int rowIndex, final int colIndex, Note noteReference) {
        Action action = new MagnetCreate(mediator, magnetGroup, rowIndex, colIndex, noteReference);
        getActionHandler().executeAction(action);
    }

    /** Creates a magnet through an action into a new magnet group.
     *
     * @param pointF        Where a new MagnetGroup is created, to contain the new Magnet.
     */
    public void actionCreateMagnet(PointF pointF) {
        Action action = new MagnetCreate(mediator, pointF);
        getActionHandler().executeAction(action);
    }
    public void actionCreateMagnet(PointF pointF, Note noteReference) {
        Action action = new MagnetCreate(mediator, pointF, noteReference);
        getActionHandler().executeAction(action);
    }

    /** Creates a magnet through an action into a new magnet group,
     *  that will be connected by a line to a parent magnet group.
     *
     * @param parentMagnetGroup Group to connect the new group to
     * @param pointF            Where a new MagnetGroup is created, to contain the new Magnet.
     */
    public void actionCreateMagnetChild(MagnetGroup parentMagnetGroup, PointF pointF) {
        Action action = new MagnetCreateChild(mediator, parentMagnetGroup, pointF);
        getActionHandler().executeAction(action);
    }
    public void actionCreateMagnetChild(MagnetGroup parentMagnetGroup, PointF pointF, Note noteReference) {
        Action action = new MagnetCreateChild(mediator, parentMagnetGroup, pointF, noteReference);
        getActionHandler().executeAction(action);
    }

    /** Creates a new line through an action.
     *
     * @param magnetGroup1  Connecting magnet group.
     * @param magnetGroup2  Connecting magnet group.
     */
    public void actionCreateLine(MagnetGroup magnetGroup1, MagnetGroup magnetGroup2) {
        Action action = new LineCreate(mediator, magnetGroup1, magnetGroup2);
        getActionHandler().executeAction(action);
    }

    /** Removes this Mindmap. IRREVERSIBLE!
     *
     */
    public void delete() {
        mediator.closeMindmap();
    }
}
