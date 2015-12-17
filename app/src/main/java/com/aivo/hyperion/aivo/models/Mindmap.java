package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // List of magnets in the mindmap (Never null, use clear!)
    private ArrayList<Magnet> magnets;

    // List of magnet groups in the mindmap (Never null, use clear!)
    private ArrayList<MagnetGroup> magnetGroups;

    // List of lines in the current mindmap (Never null, use clear!)
    private ArrayList<Line> lines;

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
        magnets = new ArrayList<Magnet>();
        magnetGroups = new ArrayList<MagnetGroup>();
        lines = new ArrayList<Line>();

        // Set identifiers and update other models
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.user.getAddNextFreeMindmapId());
    }

    /** Create a Mindmap from a existing file.
     *
     * @param mediator_     ModelMediator reference. Required!
     * @param mindmapId     Mindmap identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    protected Mindmap(ModelMediator mediator_, final int mindmapId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.lsm.loadMindmap(mediator.user.getId(), mindmapId);

        for (int magnetId : pojo.getMagnetIds()) {
            magnets.add(new Magnet(mediator, magnetId));
        }
        for (int magnetGroupId : pojo.getMagnetGroupIds()) {
            magnetGroups.add(new MagnetGroup(mediator, magnetGroupId));
        }
        for (int lineId : pojo.getLineIds()) {
            lines.add(new Line(mediator, lineId));
        }
    }

    //----------------------------------------------------------------------------------------------
    // Public interface

    public int getId() { return pojo.getMindmapId(); }
    public ArrayList<Magnet> getMagnets() {
        return new ArrayList<Magnet>(magnets);
    }
    public ArrayList<MagnetGroup> getMagnetGroups() {
        return new ArrayList<MagnetGroup>(magnetGroups);
    }
    public ArrayList<Line> getLines() {
        return new ArrayList<Line>(lines);
    }

    public void createMagnetToGroup(MagnetGroup parentGroup) {
        Magnet magnet = new Magnet(mediator, parentGroup);
        magnets.add(magnet);
    }

    public void createMagnetToNewGroup(final int x_, final int y_) {
        MagnetGroup magnetGroup = new MagnetGroup(mediator, x_, y_);
        Magnet magnet = new Magnet(mediator, magnetGroup);
        magnetGroup.addMagnet(magnet);

        magnetGroups.add(magnetGroup);
        magnets.add(magnet);
    }

    public void removeMagnet(Magnet magnet) {
        if (!magnets.contains(magnet))
            throw new InternalError("Tried to remove a unlisted Magnet!!!");

        MagnetGroup magnetGroup = magnet.getMagnetGroup();

        // Remove the magnet from its group
        magnetGroup.removeMagnet(magnet);

        // If the MagnetGroup is now empty, we must remove it
        if (magnetGroup.getMagnets().isEmpty()) {

            // Remove connections first
            for (Line line : magnetGroup.getLines())
                removeLine(line);

            magnetGroups.remove(magnetGroup);
        }

        magnets.remove(magnet);
    }

    public void addLine(MagnetGroup group1, MagnetGroup group2) {
        if (group1.isConnectedTo(group2))
            throw new InternalError("Tried to connect MagnetGroups that were already connected!");

        Line line = new Line(mediator, group1, group2);
        lines.add(line);
    }

    public void removeLine(Line line) {
        if (!lines.contains(line))
            throw new InternalError("Tried to remove a unlisted Line!!!");

        line.getMagnetGroup1().removeLine(line);
        line.getMagnetGroup2().removeLine(line);

        lines.remove(line);
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveMindmap(pojo);
        for (Magnet magnet : magnets)
            magnet.savePojo();
        for (MagnetGroup magnetGroup : magnetGroups)
            magnetGroup.savePojo();
        for (Line line : lines)
            line.savePojo();
    }
    protected int getAddNextFreeMagnetId() {
        int nextId;
        if (pojo.getRemovedMagnetIds().size() > 0) {
            nextId = pojo.getRemovedMagnetIds().remove(0);
        } else {
            nextId = pojo.getMagnetIdCounter();
            pojo.setMagnetIdCounter(nextId + 1);
        }
        pojo.getMagnetIds().add(nextId);
        return nextId;
    }
    protected int getAddNextFreeMagnetGroupId() {
        int nextId;
        if (pojo.getRemovedMagnetGroupIds().size() > 0) {
            nextId = pojo.getRemovedMagnetGroupIds().remove(0);
        } else {
            nextId = pojo.getMagnetGroupIdCounter();
            pojo.setMagnetGroupIdCounter(nextId + 1);
        }
        pojo.getMagnetGroupIds().add(nextId);
        return nextId;
    }
    protected int getAddNextFreeLineId() {
        int nextId;
        if (pojo.getRemovedLineIds().size() > 0) {
            nextId = pojo.getRemovedLineIds().remove(0);
        } else {
            nextId = pojo.getLineIdCounter();
            pojo.setLineIdCounter(nextId + 1);
        }
        pojo.getLineIds().add(nextId);
        return nextId;
    }
}