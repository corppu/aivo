package com.aivo.hyperion.aivo.models.actions;

import android.support.annotation.Nullable;

import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 14.12.2015.
 */
public class CreateMindmap extends Action {
    private String title;

    public CreateMindmap(ModelMediator mediator, @Nullable String title) {
        setMediator(mediator);
        this.title = title;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
