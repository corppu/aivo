package com.aivo.hyperion.aivo.models.actions;

import android.view.View;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by corpp on 14.12.2015.
 */
public class DeleteMagnet implements IAction {

    private Magnet mMagnet;
    private final View mView;

    public DeleteMagnet(Magnet magnet, final View view) {
        mMagnet = magnet;
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
