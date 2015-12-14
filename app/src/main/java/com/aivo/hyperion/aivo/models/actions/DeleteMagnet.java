package com.aivo.hyperion.aivo.models.actions;

import android.view.View;

import com.aivo.hyperion.aivo.models.pojos.MagnetPojo;

/**
 * Created by corpp on 14.12.2015.
 */
public class DeleteMagnet implements IAction {

    private MagnetPojo mMagnet;
    private final View mView;

    public DeleteMagnet(MagnetPojo magnet, final View view) {
        mMagnet = magnet;
        mView = view;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }

}
