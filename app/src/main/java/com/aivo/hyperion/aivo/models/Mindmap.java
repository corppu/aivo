package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mindmap {

    // Local properties
    private String title;
    
    // List of magnet groups in the mindmap (Never null, use clear!)
    protected List<MagnetGroup> magnetGroups;

    // List of lines in the mindmap (Never null, use clear!)
    protected List<Line> lines;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Mindmap(ModelMediator mediator_) {
        setMediator(mediator_);
        magnetGroups = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public List<MagnetGroup> getMagnetGroups() {
        return magnetGroups;
    }

    public List<Line> getLines() {
        return lines;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMagnetGroups(List<MagnetGroup> magnetGroups) {
        this.magnetGroups = magnetGroups;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
