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

    // List of magnets in the magnet group (Never null, use clear!)
    private ArrayList<Magnet> magnets;

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
        magnets = new ArrayList<>();

        // Set identifiers and update other models
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.mindmap.getId());
        pojo.setMagnetGroupId(mediator.user.getNextObjectId());

        // Set other pojo data
        pojo.setX(x); pojo.setY(y);
    }

    protected MagnetGroup(ModelMediator mediator_, final int magnetGroupId) throws IOException {
        setMediator(mediator_);
        //pojo = mediator.lsm.loadLine(mediator.user.getId(), mediator.mindmap.getId(), magnetGroupId);

        for (int magnetId : pojo.getMagnetIds()) {
            magnets.add(new Magnet(mediator, magnetId));
        }
    }

    //----------------------------------------------------------------------------------------------
    // Public interface

    public int getId() { return pojo.getMagnetGroupId(); }

    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        for (Line line : mediator.mindmap.lines)
            if (pojo.getLineIds().contains(line.getId()))
                lines.add(line);
        return lines;
    }

    public ArrayList<Magnet> getMagnets() {
        return magnets;
    }

    public int getX() { return pojo.getX(); }
    public int getY() { return pojo.getY(); }
    public String getTitle() { return pojo.getTitle(); }


    public void moveGroup(final int newX, final int newY) {
        pojo.setX(newX);
        pojo.setY(newY);
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        for (Magnet magnet : magnets)
            magnet.savePojo();
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
        magnets.add(magnet);
    }

    protected void removeMagnet(Magnet magnet) {
        pojo.getMagnetIds().remove(new Integer(magnet.getId()));
        magnets.remove(magnet);

        // If we remove the last magnet, remove the group as well
        if (magnets.isEmpty()) {
            for (Line line : getLines())
                line.deleteLine();

            mediator.mindmap.magnetGroups.remove(this);
        }
    }

}
