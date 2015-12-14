package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MagnetPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setMagnetId(mediator.mindmap.getAddNextFreeMagnetId());

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
        pojo = mediator.lsm.loadMagnet(mediator.user.getId(),
                                            mediator.mindmap.getId(), magnetId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getMagnetId(); }
    public ArrayList<Integer> getLineIds() { return new ArrayList<Integer>(pojo.getLineIds()); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveMagnet(pojo);
    }
    protected boolean isConnectedTo(Magnet magnet) {
        if (magnet == this)
            throw new InternalError("Tried to check if a Magnet is connected to itself!!!");
        for (Integer lineId : magnet.getLineIds()) {
            if (pojo.getLineIds().contains(lineId))
                return true;
        }
        return false;
    }
    /** Should only be called from Mindmaps addLine() */
    protected void addLine(Line line) {
        pojo.getLineIds().add(line.getId());
    }
}
