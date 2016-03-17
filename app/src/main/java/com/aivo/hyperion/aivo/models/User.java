package com.aivo.hyperion.aivo.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    // Current theme
    private Theme theme;
    public Theme getTheme() { return theme; }
    public void setTheme(int themeId) { this.theme = new Theme(themeId); }

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    protected User(ModelMediator mediator_) {
        setMediator(mediator_);
        setTheme(0);
    }

    protected User(ModelMediator mediator_, JSONObject json) {
        setMediator(mediator_);
        try {
            setTheme(json.getInt("themeId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
