package com.aivo.hyperion.aivo.models.actions;

import android.support.annotation.Nullable;
import android.view.View;

import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by corpp on 14.12.2015.
 */
public class CreateMagnet implements IAction {

    ModelMediator mediator;
    View view;

    private int x;
    private int y;
    private String title;

    public CreateMagnet(final int x, final int y, @Nullable String title) {
        this.x = x;
        this.y = y;
        this.title = title;
    }

    @Override
    public void setMediator(ModelMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        mediator.getMindmap().createMagnet(x, y);
    }

    @Override
    public void undo() {

    }
}
