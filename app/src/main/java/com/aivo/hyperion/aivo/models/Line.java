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

        if (magnet1.isConnectedTo(magnet2))
            throw new InternalError("Tried to create a Line between Magnets that already have a Line between them!");

        pojo.setUserId(mediator.getUser().getId());
        pojo.setMindmapId(mediator.getMindmap().getId());
        pojo.setLineId(mediator.getMindmap().getAddNextFreeLineId());
        pojo.setMagnet1(magnet1.getId());
        pojo.setMagnet2(magnet2.getId());
    }

    protected Line(ModelMediator mediator_, final int lineId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.getLSM().loadLine(mediator.getUser().getId(), mediator.getMindmap().getId(), lineId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getLineId(); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.getLSM().saveLine(pojo);
    }
}
