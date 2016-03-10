package com.aivo.hyperion.aivo.models;

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

    public User(ModelMediator mediator_) {
        setMediator(mediator_);
        setTheme(0);
    }

}
