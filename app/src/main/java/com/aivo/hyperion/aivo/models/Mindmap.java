package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;

public class Mindmap {

    // Local properties
    private String title;
    
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

    public Mindmap() {

    }

    public ArrayList<MagnetGroup> getMagnetGroups() {
        return new ArrayList<MagnetGroup>(magnetGroups);
    }

    public ArrayList<Line> getLines() {
        return new ArrayList<Line>(lines);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMagnetGroups(ArrayList<MagnetGroup> magnetGroups) {
        this.magnetGroups = magnetGroups;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
