package com.aivo.hyperion.aivo.models;

import android.support.annotation.Nullable;

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
     * @param mediator_     LocalStorageModule reference. Required!
     * @param magnetGroup   MagnetGroup to create this magnet into. Required!
     */
    protected Magnet(ModelMediator mediator_, MagnetGroup magnetGroup) {
        setMediator(mediator_);
        pojo = new MagnetPojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setMagnetId(mediator.user.getNextObjectId());

        pojo.setMagnetGroupId(magnetGroup.getId());
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
    public MagnetGroup getMagnetGroup() {
        for (MagnetGroup magnetGroup : mediator.mindmap.getMagnetGroups())
            if (magnetGroup.getId() == pojo.getMagnetGroupId())
                return magnetGroup;
        throw new InternalError("Could not find Magnets MagnetGroup from Mindmap!");
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveMagnet(pojo);
    }
    protected void changeGroupTo(@Nullable MagnetGroup magnetGroup) {
        getMagnetGroup().removeMagnet(this);
        if ( !(magnetGroup == null) ) {
            pojo.setMagnetGroupId(magnetGroup.getId());
            magnetGroup.addMagnet(this);
        }
    }
}
