package com.aivo.hyperion.aivo.models.actions;

import android.support.annotation.Nullable;

import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by corpp on 14.12.2015.
 */
public class CreateMagnet extends Action {

    private int x;
    private int y;
    private String title;

    public CreateMagnet(ModelMediator mediator, final int x, final int y, @Nullable String title) {
        setMediator(mediator);
        this.x = x;
        this.y = y;
        this.title = title;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
