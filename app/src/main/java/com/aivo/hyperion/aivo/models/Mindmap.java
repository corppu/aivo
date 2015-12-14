package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // List of magnets in the mindmap (Never null, use clear!)
    private ArrayList<Magnet> magnets;

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
    public ArrayList<Line> getLines() {
        return new ArrayList<Line>(lines);
    }

    public void addMagnet(final Magnet previousMagnet, final int x, final int y) { // TODO:  root/parent etc
        // Create and add the new magnet to the mindmap
        Magnet magnet = new Magnet(mediator, x, y);
        magnets.add(magnet);

    }

    public void removeMagnet(Magnet magnet) {
        if (!magnets.contains(magnet))
            throw new InternalError("Tried to remove a unlisted Magnet!!!");

        // Remove connections

    }

    public void selectMagnet(Magnet magnet) {
        if (!magnets.contains(magnet))
            throw new InternalError("Tried to select a unlisted Magnet!!!");

    }

    public void addLine(Magnet magnet1, Magnet magnet2) {
        if (magnet1.isConnectedTo(magnet2))
            throw new InternalError("Tried to connect Magnets that were already connected!");

        Line line = new Line(mediator, magnet1, magnet2);
        lines.add(line);
    }
    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveMindmap(pojo);
        for (Magnet magnet : magnets)
            magnet.savePojo();
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