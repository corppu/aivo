package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.UserPojo;

import java.io.IOException;
import java.util.List;

public class User {

    // Current theme
    protected Theme theme;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public User() {

    }

}
