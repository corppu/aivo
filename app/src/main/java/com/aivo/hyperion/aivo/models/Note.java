package com.aivo.hyperion.aivo.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Note {

    // These have to be protected, so Magnet can access them
    protected String title;
    protected String content;
    protected int color;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getColor() { return color; }

    // The model mediator reference
    protected ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Note created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    protected Note(){} // default constructor for Magnet
    protected Note(ModelMediator mediator_) {
        setMediator(mediator_);
    }
    protected Note(ModelMediator mediator_, Magnet magnetReference) {
        setMediator(mediator_);
        this.title = magnetReference.getTitle();
        this.content = magnetReference.getContent();
        this.color = magnetReference.getColor();
    }
    protected Note(ModelMediator mediator_, JSONObject json) {
        try {
            this.title = json.getString("title");
            this.content = json.getString("content");
            this.color = json.getInt("color");

        } catch (JSONException e) {
            for (ModelListener listener : mediator.getListeners()) listener.onException(e);
        }
    }
    protected JSONObject getJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("title", title);
            object.put("content", content);
            object.put("color", color);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    /** For Notes: use this.
     *  For Magnets: DO NOT USE. Only to be used through actions.
     */
    public void setData(String newTitle, String newContent, final int newColor) {
        title = newTitle;
        content = newContent;
        color = newColor;

        for (ModelListener listener : mediator.getListeners()) listener.onNoteChange(this);
    }

    public boolean hasImage() {
        return false; // TODO
    }
    public boolean hasVideo() {
        return false; // TODO
    }

}
