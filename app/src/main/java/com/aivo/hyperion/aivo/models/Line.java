package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.LinePointAdd;
import com.aivo.hyperion.aivo.models.actions.LineChangeData;
import com.aivo.hyperion.aivo.models.actions.LineDelete;
import com.aivo.hyperion.aivo.models.actions.LinePointDelete;
import com.aivo.hyperion.aivo.models.actions.LinePointMove;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Line {

    // Local properties
    private int id;
    private MagnetGroup magnetGroup1;
    private MagnetGroup magnetGroup2;
    private ArrayList<PointF> points;
    private int type;
    private int thickness;

    public MagnetGroup getMagnetGroup1() { return magnetGroup1; }
    public MagnetGroup getMagnetGroup2() { return magnetGroup2; }
    public ArrayList<PointF> getPoints() { return points; }
    public int getType() { return type; }
    public int getThickness() { return thickness; }
    public int getId() { return id; }

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
        this.id = mediator.getMindmap().getNextId();
        this.points = new ArrayList<>();
    }

    /** Used to construct a line from a json object.
     *  Should only be called from mindmap, when creating the mindmap from json!
     */
    protected Line(ModelMediator mediator_, JSONObject json) {
        setMediator(mediator_);
        this.points = new ArrayList<>();

        try {
            this.id = json.getInt("id");
            this.type = json.getInt("type");
            this.thickness = json.getInt("thickness");

            JSONArray jsonPoints = json.getJSONArray("points");
            for (int i = 0; i < jsonPoints.length(); ++i) {
                points.add((PointF) jsonPoints.get(i));
            }

            final int groupId1 = json.getInt("magnetGroup1");
            final int groupId2 = json.getInt("magnetGroup2");

            for (MagnetGroup magnetGroup : mediator.getMindmap().getMagnetGroups()) {
                if (magnetGroup.getId() == groupId1) {
                    magnetGroup.addLine(this);
                    magnetGroup1 = magnetGroup;
                }
                if (magnetGroup.getId() == groupId2) {
                    magnetGroup.addLine(this);
                    magnetGroup2 = magnetGroup;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public void actionMovePoint(PointF pointToMove, PointF newPoint) {
        Action action = new LinePointMove(mediator, this, pointToMove, newPoint);
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
