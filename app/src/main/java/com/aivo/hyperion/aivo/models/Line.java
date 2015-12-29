package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LinePojo;

import java.io.IOException;

/**
 * Created by MicroLoota on 11.11.2015.
 */
public class Line {
    // The local pojo
    private LinePojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Line created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    protected Line(ModelMediator mediator_, MagnetGroup magnetGroup1, MagnetGroup magnetGroup2) {
        setMediator(mediator_);
        pojo = new LinePojo();

        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setLineId(mediator.user.getNextObjectId());
        pojo.setMagnetGroup1(magnetGroup1.getId());
        pojo.setMagnetGroup2(magnetGroup2.getId());

        magnetGroup1.addLine(this);
        magnetGroup2.addLine(this);
    }

    protected Line(ModelMediator mediator_, final int lineId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.lsm.loadLine(mediator.user.getId(), mediator.mindmap.getId(), lineId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getLineId(); }
    public MagnetGroup getMagnetGroup1() {
        for (MagnetGroup magnetGroup : mediator.mindmap.getMagnetGroups())
            if (magnetGroup.getLines().contains(this))
                return magnetGroup;
        throw new InternalError("Could not find a Lines first MagnetGroup from Mindmap!");
    }
    public MagnetGroup getMagnetGroup2() {
        boolean firstFound = false;
        for (MagnetGroup magnetGroup : mediator.mindmap.getMagnetGroups())
            if (magnetGroup.getLines().contains(this))
                if (!firstFound) firstFound = true;
                else return magnetGroup;
        throw new InternalError("Could not find a Lines second MagnetGroup from Mindmap!");
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveLine(pojo);
    }
}
