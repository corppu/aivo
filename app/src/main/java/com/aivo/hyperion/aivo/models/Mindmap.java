package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    /** Create a new Mindmap. Gets required information from the Mediator.
     *
     * @param mediator_  ModelMediator reference. Required!
     */
    protected Mindmap(ModelMediator mediator_) {
        setMediator(mediator_);
        pojo = new MindmapPojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.getUser().getId());
        pojo.setMindmapId(mediator.getUser().getAddNextFreeMindmapId());
    }

    /** Create a Mindmap from a existing file.
     *
     * @param mediator_     ModelMediator reference. Required!
     * @param mindmapId     Mindmap identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    protected Mindmap(ModelMediator mediator_, final int mindmapId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.getLSM().loadMindmap(mediator.getUser().getId(), mindmapId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getMindmapId(); }
    public ArrayList<Integer> getMagnetIds() { return new ArrayList<Integer>(pojo.getMagnetIds()); }


    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.getLSM().saveMindmap(pojo);
    }
    protected void saveMagnets() throws IOException {
        for (Magnet magnet : mediator.getMagnets())
            magnet.savePojo();
    }
    protected int getAddNextFreeMagnetId() {
        int nextId;
        if (pojo.getRemovedMagnetIds().size() > 0) {
            nextId = pojo.getRemovedMagnetIds().remove(0).intValue();
        } else {
            nextId = pojo.getMagnetIdCounter();
            pojo.setMagnetIdCounter(nextId + 1);
        }
        pojo.getMagnetIds().add(nextId);
        return nextId;
    }
    protected void openMagnets() throws IOException {
        for (int magnetId : pojo.getMagnetIds()) {
            mediator.getMagnets().add(new Magnet(mediator, magnetId));
        }
    }
}