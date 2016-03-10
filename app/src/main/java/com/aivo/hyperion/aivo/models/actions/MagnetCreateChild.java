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

    public MagnetCreateChild(ModelMediator mediator, MagnetGroup parentMagnetGroup, PointF pointF) {
        setMediator(mediator);
        this.parentMagnetGroup = parentMagnetGroup;
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup);
        this.line = new Line(mediator, this.parentMagnetGroup, this.magnetGroup);
    }

    public MagnetCreateChild(ModelMediator mediator, MagnetGroup parentMagnetGroup, PointF pointF, Note noteReference) {
        setMediator(mediator);
        this.parentMagnetGroup = parentMagnetGroup;
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, noteReference);
        this.line = new Line(mediator, this.parentMagnetGroup, this.magnetGroup);
    }

    @Override
    void execute() {
        mediator.getMindmap().getMagnetGroups().add(magnetGroup);
        mediator.getMindmap().getLines().add(line);
        line.getMagnetGroup1().getLines().add(line);
        line.getMagnetGroup2().getLines().add(line);
        insertMagnetIntoGroup(magnet, magnetGroup, 0, 0);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetCreate(magnet);
            listener.onLineCreate(line);
            listener.onMagnetGroupCreate(magnetGroup);
            listener.onMagnetGroupChange(parentMagnetGroup);
        }
    }

    @Override
    void undo() {
        mediator.getMindmap().getLines().remove(line);
        line.getMagnetGroup1().getLines().remove(line);
        line.getMagnetGroup2().getLines().remove(line);
        removeMagnetFromGroup(magnet, magnetGroup);
        mediator.getMindmap().getMagnetGroups().remove(magnetGroup);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineDelete(line);
            listener.onMagnetDelete(magnet);
            listener.onMagnetGroupChange(parentMagnetGroup);
            listener.onMagnetGroupDelete(magnetGroup);
        }
    }
}
