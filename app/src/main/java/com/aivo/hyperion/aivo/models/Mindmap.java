package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // List of magnet groups in the mindmap (Never null, use clear!)
    protected ArrayList<MagnetGroup> magnetGroups;

    // List of lines in the mindmap (Never null, use clear!)
    protected ArrayList<Line> lines;

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
        magnetGroups = new ArrayList<>();
        lines = new ArrayList<>();

        // Set identifiers
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.user.getNextObjectId());

        // Update user
        mediator.user.addMindmapId(pojo.getMindmapId());
        mediator.user.setRecent(pojo.getMindmapId());
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

        for (int magnetGroupId : pojo.getMagnetGroupIds()) {
            magnetGroups.add(new MagnetGroup(mediator, magnetGroupId));
        }

        // Update user
        mediator.user.setRecent(pojo.getMindmapId());
    }

    //----------------------------------------------------------------------------------------------
    // Public interface

    public int getId() { return pojo.getMindmapId(); }

    public ArrayList<MagnetGroup> getMagnetGroups() {
        return new ArrayList<>(magnetGroups);
    }

    public ArrayList<Line> getLines() {
        return new ArrayList<>(lines);
    }

    public void createMagnet(MagnetGroup parentGroup) { // TODO: Position within groups magnets?
        Magnet magnet = new Magnet(mediator, parentGroup);
        parentGroup.addMagnet(magnet);
    }

    public void createMagnet(final int x, final int y) {
        MagnetGroup magnetGroup = new MagnetGroup(mediator, x, y);
        magnetGroups.add(magnetGroup);
        createMagnet(magnetGroup);
    }

    public void createLine(MagnetGroup group1, MagnetGroup group2) {
        if (group1.isConnectedTo(group2))
            throw new InternalError("Tried to connect MagnetGroups that were already connected!");

        Line line = new Line(mediator, group1, group2);
        lines.add(line);
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveMindmap(pojo);
        for (MagnetGroup magnetGroup : magnetGroups)
            magnetGroup.savePojo();
        for (Line line : lines)
            line.savePojo();
    }
}
