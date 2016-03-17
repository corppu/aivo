package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.Note;

public class MagnetCreateChild extends MagnetAction {

    // Could simply contain actions MagnetCreate and LineCreate, but this is faster
    private MagnetGroup parentMagnetGroup;
    private MagnetGroup magnetGroup;
    private Magnet magnet;
    private Line line;

    public MagnetCreateChild(ModelMediator mediator, MagnetGroup parentMagnetGroup, PointF pointF,
                             String title, String content, final int color) {
        setMediator(mediator);
        this.parentMagnetGroup = parentMagnetGroup;
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, title, content, color);
        this.line = new Line(mediator, this.parentMagnetGroup, this.magnetGroup);

        // Only need to add the line to the NEW magnetgroup, insert adds it to the other
        this.magnetGroup.getLines().add(line);
    }

    @Override
    void execute() {
        insertMagnetIntoGroup(magnet, magnetGroup, 0, 0);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetCreate(magnet);
            listener.onMagnetGroupCreate(magnetGroup);
            listener.onLineCreate(line);
            listener.onMagnetGroupChange(parentMagnetGroup);
        }
    }

    @Override
    void undo() {
        removeMagnetFromGroup(magnet, magnetGroup);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineDelete(line);
            listener.onMagnetDelete(magnet);
            listener.onMagnetGroupChange(parentMagnetGroup);
            listener.onMagnetGroupDelete(magnetGroup);
        }
    }
}
