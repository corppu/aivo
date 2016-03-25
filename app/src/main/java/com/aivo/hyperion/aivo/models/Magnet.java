package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.MagnetChangeData;
import com.aivo.hyperion.aivo.models.actions.MagnetDelete;
import com.aivo.hyperion.aivo.models.actions.MagnetGroupMove;
import com.aivo.hyperion.aivo.models.actions.MagnetMove;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Magnet extends Note {

    // Local properties
    private int id;
    private MagnetGroup magnetGroup;

    // DO NOT USE! Only to be used by actions.
    public void setMagnetGroup(MagnetGroup newMagnetGroup) { magnetGroup = newMagnetGroup; }
    public MagnetGroup getMagnetGroup() { return magnetGroup; }
    public int getId() { return id; }
    
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Magnet created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Magnet(ModelMediator mediator_, MagnetGroup magnetGroup,
                  String title, String content, final int color) {
        setMediator(mediator_);
        this.magnetGroup = magnetGroup;
        this.id = mediator.getMindmap().getNextId();
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public Magnet(ModelMediator mediator_, MagnetGroup magnetGroup, Note noteReference) {
        setMediator(mediator_);
        this.magnetGroup = magnetGroup;
        this.title = noteReference.getTitle();
        this.content = noteReference.getContent();
        this.color = noteReference.getColor();
        this.id = mediator.getMindmap().getNextId();
    }

    /** Used to construct a magnet from a json object.
     *  Should only be called from magnetgroup, when creating a mindmap from json!
     */
    protected Magnet(ModelMediator mediator_, MagnetGroup magnetGroup, JSONObject json) {
        setMediator(mediator_);
        this.magnetGroup = magnetGroup;
        try {
            this.id = json.getInt("id");
            this.title = json.getString("title");
            this.content = json.getString("content");
            this.color = json.getInt("color");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected JSONObject getJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("title", title);
            object.put("content", content);
            object.put("color", color);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void actionChangeData(String newTitle) {
        Action action = new MagnetChangeData(mediator, this, newTitle, content, color);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeData(String newTitle, String newContent) {
        Action action = new MagnetChangeData(mediator, this, newTitle, newContent, color);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeColor(final int newColor) {
        Action action = new MagnetChangeData(mediator, this, title, content, newColor);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(MagnetGroup newMagnetGroup, final int rowIndex, final int colIndex, final boolean createNewRowAlways) {
        Action action = new MagnetMove(mediator, this, newMagnetGroup, rowIndex, colIndex, createNewRowAlways);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(PointF newPoint) {
        Action action;
        if (getMagnetGroup().getMagnetCount() == 1)
            action = new MagnetGroupMove(mediator, getMagnetGroup(), newPoint);
        else
            action = new MagnetMove(mediator, this, newPoint);

        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionDelete() {
        Action action = new MagnetDelete(mediator, this);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }
}
