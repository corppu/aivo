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

    protected Line(ModelMediator mediator_, Magnet magnet1, Magnet magnet2) {
        setMediator(mediator_);
        pojo = new LinePojo();

        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setLineId(mediator.mindmap.getAddNextFreeLineId());
        pojo.setMagnet1(magnet1.getId());
        pojo.setMagnet2(magnet2.getId());

        magnet1.addLine(this);
        magnet2.addLine(this);
    }

    protected Line(ModelMediator mediator_, final int lineId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.lsm.loadLine(mediator.user.getId(), mediator.mindmap.getId(), lineId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getLineId(); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveLine(pojo);
    }
}
