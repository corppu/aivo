package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MagnetPojo;

import java.io.IOException;

public class Magnet {
    // The local pojo
    private MagnetPojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    /** Create a new Magnet. Gets required information from the mediator.
     *
     * @param mediator_  LocalStorageModule reference. Required!
     */
    protected Magnet(ModelMediator mediator_, final int x, final int y) {
        setMediator(mediator_);
        pojo = new MagnetPojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.getUser().getId());
        pojo.setMindmapId(mediator.getMindmap().getId());
        pojo.setMagnetId(mediator.getMindmap().getAddNextFreeMagnetId());

        // Set other pojo data
        pojo.setX(x); pojo.setY(y);
    }

    /** Create a Magnet from a existing file.
     *
     * @param mediator_     LocalStorageModule reference. Required!
     * @param magnetId      Magnet identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    protected Magnet(ModelMediator mediator_, final int magnetId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.getLSM().loadMagnet(mediator.getUser().getId(),
                                            mediator.getMindmap().getId(), magnetId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getMagnetId(); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.getLSM().saveMagnet(pojo);
    }
}
