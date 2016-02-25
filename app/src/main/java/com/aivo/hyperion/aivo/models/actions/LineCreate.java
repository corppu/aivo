package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 8.2.2016.
 */
public class LineCreate extends Action {

    private Line line;

    public LineCreate(ModelMediator mediator, MagnetGroup magnetGroup1, MagnetGroup magnetGroup2) {
        if (magnetGroup1 == magnetGroup2)
            throw new InternalError("Tried to LineCreate from a group to the same group!");
        else {
            for (Line line : magnetGroup1.getLines())
                if (line.getMagnetGroup1() == magnetGroup2 || line.getMagnetGroup2() == magnetGroup2)
                    throw new InternalError("Tried to LineCreate between already connected groups!");
        }

        setMediator(mediator);
        this.line = new Line(mediator, magnetGroup1, magnetGroup2);
    }

    @Override
    public void execute() {
        // TODO: LSM: Write to file
        mediator.getMindmap().getLines().add(line);
        line.getMagnetGroup1().getLines().add(line);
        line.getMagnetGroup2().getLines().add(line);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineCreate(line);
            listener.onMagnetGroupChange(line.getMagnetGroup1());
            listener.onMagnetGroupChange(line.getMagnetGroup2());
        }
    }

    @Override
    public void undo() {
        // TODO: LSM: Remove file
        mediator.getMindmap().getLines().remove(line);
        line.getMagnetGroup1().getLines().remove(line);
        line.getMagnetGroup2().getLines().remove(line);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineDelete(line);
            listener.onMagnetGroupChange(line.getMagnetGroup1());
            listener.onMagnetGroupChange(line.getMagnetGroup2());
        }
    }
}
