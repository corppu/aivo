package com.aivo.hyperion.aivo.models.actions;

import android.view.View;

import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 14.12.2015.
 */
public class DeleteMindmap implements IAction {

    private Mindmap mMindmap;
    private final View mView;

    public DeleteMindmap(Mindmap mindmap, final View view) {
        mMindmap = mindmap;
        mView = view;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }


    @Override
    public void setMediator(ModelMediator mediator) {

    }

    @Override
    public void setView(View view) {

    }
}
