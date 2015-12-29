package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // List of magnet groups in the mindmap (Never null, use clear!)
    private ArrayList<MagnetGroup> magnetGroups;

    // List of lines in the mindmap (Never null, use clear!)
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
        magnetGroups = new ArrayList<>();
        lines = new ArrayList<>();

        // Set identifiers
        pojo.setUserId(mediator.user.getId());
        pojo.setMindmapId(mediator.user.getNextObjectId());
        mediator.user.addMindmapId(pojo.getMindmapId());
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

    public void createMagnet(final int x_, final int y_) {
        MagnetGroup magnetGroup = new MagnetGroup(mediator, x_, y_);
        magnetGroups.add(magnetGroup);
        createMagnet(magnetGroup);
    }

    public void moveMagnet(Magnet magnet, MagnetGroup newMagnetGroup) {  // TODO: Position within new groups magnets?
        if (!magnetGroups.contains(newMagnetGroup))
            throw new InternalError("Tried to move a Magnet into a unlisted MagnetGroup!!!");

        magnet.changeGroupTo(newMagnetGroup);
    }

    public void moveMagnet(Magnet magnet, final int x_, final int y_) {

        MagnetGroup magnetGroup = new MagnetGroup(mediator, x_, y_);
        magnetGroups.add(magnetGroup);

        magnet.changeGroupTo(magnetGroup);
    }

    public void deleteMagnet(Magnet magnet) {

        magnet.changeGroupTo(null);
    }

    public void addLine(MagnetGroup group1, MagnetGroup group2) {
        if (group1.isConnectedTo(group2))
            throw new InternalError("Tried to connect MagnetGroups that were already connected!");

        Line line = new Line(mediator, group1, group2);
        lines.add(line);
    }

    public void deleteLineConnection(Line line) {
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
        for (MagnetGroup magnetGroup : magnetGroups)
            magnetGroup.savePojo();
        for (Line line : lines)
            line.savePojo();
    }
}
