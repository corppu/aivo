package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;
import android.view.View;

import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.pojos.MagnetPojo;

/**
 * Created by corpp on 14.12.2015.
 */
public class MoveMagnet implements IAction {

    private final PointF mStartPointF;
    private final PointF mEndPointF;
    private MagnetPojo mMagnet;
    private final View mView;
    private final ModelMediator mMediator;

    public MoveMagnet(MagnetPojo magnet, final PointF end, final View view, final ModelMediator mediator) {
        mStartPointF = new PointF(magnet.getX(), magnet.getY());
        mEndPointF = end;
        mMagnet = magnet;
        mView = view;
        mMediator = mediator;
    }

    @Override
    public void execute() {
        mMagnet.setX((int)mEndPointF.x);
        mMagnet.setY((int)mEndPointF.y);
    }

    @Override
    public void undo() {
        mMagnet.setX((int)mStartPointF.x);
        mMagnet.setY((int)mStartPointF.y);
    }
}
