package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MagnetGroupPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroLoota on 17.12.2015.
 */
public class MagnetGroup {
    // The local pojo
    private MagnetGroupPojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("MagnetGroup created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }


    protected MagnetGroup(ModelMediator mediator_, final int x, final int y) {
        setMediator(mediator_);
        pojo = new MagnetGroupPojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setMagnetGroupId(mediator.mindmap.getAddNextFreeMagnetGroupId());

        // Set other pojo data
        pojo.setX(x); pojo.setY(y);
    }

    protected MagnetGroup(ModelMediator mediator_, final int magnetGroupId) throws IOException {
        setMediator(mediator_);
        //pojo = mediator.lsm.loadLine(mediator.user.getId(), mediator.mindmap.getId(), magnetGroupId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface

    public int getId() { return pojo.getMagnetGroupId(); }
    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<Line>();
        for (Line line : mediator.mindmap.getLines())
            if (pojo.getLineIds().contains(line.getId()))
                lines.add(line);
        return lines;
    }

    public ArrayList<Magnet> getMagnets() {
        ArrayList<Magnet> magnets = new ArrayList<Magnet>();
        for (Magnet magnet : mediator.mindmap.getMagnets())
            if (pojo.getMagnetIds().contains(magnet.getId()))
                magnets.add(magnet);
        return magnets;
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
       // mediator.lsm.saveMagnetGroup(pojo);
    }

    protected boolean isConnectedTo(MagnetGroup group) {
        if (group == this)
            throw new InternalError("Tried to check if a MagnetGroup is connected to itself!!!");

        for (Line line : group.getLines()) {
            if (pojo.getLineIds().contains(line.getId()))
                return true;
        }

        return false;
    }

    protected void addLine(Line line) {
        pojo.getLineIds().add(line.getId());
    }

    protected void removeLine(Line line) {
        pojo.getLineIds().remove(new Integer(line.getId()));
    }

    protected void addMagnet(Magnet magnet) {
        pojo.getMagnetIds().add(magnet.getId());
    }

    protected void removeMagnet(Magnet magnet) {
        pojo.getMagnetIds().remove(new Integer(magnet.getId()));
    }
}
