package com.aivo.hyperion.aivo.models.actions;

import android.view.View;

import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;

/**
 * Created by MicroLoota on 14.12.2015.
 */
public class DeleteUser implements IAction {

    private User mUser;
    private final View mView;

    public DeleteUser(User magnet, final View view) {
        mUser = magnet;
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
