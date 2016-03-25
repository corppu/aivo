package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;
import android.util.Log;

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
import java.util.List;

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
    protected Line(ModelMediator mediator_, JSONObject json, Mindmap owningMindmap) {
        setMediator(mediator_);
        this.points = new ArrayList<>();
        try {
            this.id = json.getInt("id");
            this.type = json.getInt("type");
            this.thickness = json.getInt("thickness");

            JSONArray jsonPoints = json.getJSONArray("points");
            for (int i = 0; i < jsonPoints.length(); i+=2) {
                points.add(new PointF((float) jsonPoints.getDouble(i), (float) jsonPoints.getDouble(i+1)));
            }

            final int groupId1 = json.getInt("magnetGroupId1");
            final int groupId2 = json.getInt("magnetGroupId2");
            for (MagnetGroup magnetGroup : owningMindmap.getMagnetGroups()) {
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

    protected JSONObject getJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("type", type);
            object.put("thickness", thickness);

            JSONArray jsonPoints = new JSONArray();
            for (PointF point : points) {
                jsonPoints.put((double)point.x);
                jsonPoints.put((double)point.y);
            }
            object.put("points", jsonPoints);

            object.put("magnetGroupId1", magnetGroup1.getId());
            object.put("magnetGroupId2", magnetGroup2.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // DO NOT USE! Only for LineChangeData action!
    public void setData(final int newType, final int newThickness) {
        type = newType;
        thickness = newThickness;
    }

    // DO NOT USE! Only for MagnetMove action!
    public void setGroups(final MagnetGroup magnetGroup1, final MagnetGroup magnetGroup2) {
        this.magnetGroup1 = magnetGroup1;
        this.magnetGroup2 = magnetGroup2;
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
