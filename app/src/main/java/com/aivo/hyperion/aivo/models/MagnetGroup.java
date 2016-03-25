package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;
import android.util.Log;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.MagnetGroupChangeData;
import com.aivo.hyperion.aivo.models.actions.MagnetGroupMove;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MagnetGroup {

    // Local properties
    private int id;
    private String title;
    private PointF point;

    // List of magnets in the magnet group (Never null, use clear!)
    protected List< List<Magnet> > magnets;

    // List of lines that are connected to this group
    protected List< Line > lines;

    // DO NOT USE! Only for MagnetGroupChangeData action!
    public void setData(String newTitle) { title = newTitle; }
    public PointF getPoint() { return point; } // TODO: Refactor to getTopLeftPoint
    public String getTitle() { return title;  }
    public List< List<Magnet> > getMagnets() { return magnets; }
    public List< Line > getLines() { return lines; }
    public int getId() { return id; }

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
        this.point = new PointF(point.x, point.y);
        this.lines = new ArrayList<>();
        this.title = new String();
        this.id = mediator.getMindmap().getNextId();
    }

    /** Used to construct a magnetgroup from a json object.
     *  Should only be called from mindmap, when creating the mindmap from json!
     */
    protected MagnetGroup(ModelMediator mediator_, JSONObject json) {
        setMediator(mediator_);
        try {
            this.magnets = new ArrayList<>();
            this.lines = new ArrayList<>();
            this.id = json.getInt("id");
            this.title = json.getString("title");
            this.point = new PointF((float)json.getDouble("point.x"), (float)json.getDouble("point.y"));

            JSONArray jsonMagnets = json.getJSONArray("magnets");
            JSONArray jsonMagnetRow;
            List<Magnet> magnetRow;
            Magnet magnet;

            // Reconstruct magnet lists
            for (int i = 0; i < jsonMagnets.length(); ++i) {
                jsonMagnetRow = jsonMagnets.getJSONArray(i);
                magnetRow = new ArrayList<>();
                magnets.add(magnetRow);
                for (int j = 0; j < jsonMagnetRow.length(); ++j) {
                    magnet = new Magnet(mediator, this, jsonMagnetRow.getJSONObject(j));
                    magnetRow.add(magnet);
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
            object.put("title", title);
            object.put("point.x", (double) point.x);
            object.put("point.y", (double) point.y);

            JSONArray jsonMagnets = new JSONArray();
            for (List<Magnet> magnetRow : magnets) {
                JSONArray jsonMagnetRow = new JSONArray();
                for (Magnet magnet : magnetRow)
                    jsonMagnetRow.put(magnet.getJSON());
                jsonMagnets.put(jsonMagnetRow);
            }
            object.put("magnets", jsonMagnets);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public int getMagnetCount() {
        int count = 0;
        for (List<Magnet> magnetRow : magnets)
            count += magnetRow.size();
        return count;
    }

    // Should only be called from line, when creating a mindmap from json!
    protected void addLine(Line line) {
        lines.add(line);
    }

    public void actionCreateLine(MagnetGroup magnetGroup) {
        mediator.getMindmap().actionCreateLine(this, magnetGroup);
    }

    public void actionCreateMagnet(final int rowIndex, final int colIndex, final boolean createNewRowAlways,
                                   String title, String content, final int color) {
        mediator.getMindmap().actionCreateMagnet(this, rowIndex, colIndex, createNewRowAlways, title, content, color);
    }
    public void actionCreateMagnet(final int rowIndex, final int colIndex,
                                   final boolean createNewRowAlways, Note noteReference) {
        mediator.getMindmap().actionCreateMagnet(this, rowIndex, colIndex, createNewRowAlways, noteReference);
    }

    public void actionCreateMagnetChild(PointF pointF, String title, String content, final int color) {
        mediator.getMindmap().actionCreateMagnetChild(this, pointF, title, content, color);
    }

    public void actionChangeData(String newTitle) {
        Action action = new MagnetGroupChangeData(mediator, this, newTitle);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(PointF newPoint) {
        Action action = new MagnetGroupMove(mediator, this, newPoint);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(MagnetGroup magnetGroupToMergeInto, final boolean keepLines) {
        throw new InternalError("MagnetGroup merging unimplemented for now!");
    }
}
