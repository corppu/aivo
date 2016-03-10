package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 6.3.2016.
 */
public class LineDelete extends Action {

    private Line line;

    public LineDelete(ModelMediator mediator, Line line) {
        setMediator(mediator);
        this.line = line;
    }

    @Override
    void execute() {
        mediator.getMindmap().getLines().remove(line);
        line.getMagnetGroup1().getLines().remove(line);
        line.getMagnetGroup2().getLines().remove(line);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineDelete(line);
            listener.onMagnetGroupChange(line.getMagnetGroup1());
            listener.onMagnetGroupChange(line.getMagnetGroup2());
        }
    }

    @Override
    void undo() {
        mediator.getMindmap().getLines().add(line);
        line.getMagnetGroup1().getLines().add(line);
        line.getMagnetGroup2().getLines().add(line);

        for (ModelListener listener : mediator.getListeners()) {
            listener.onLineCreate(line);
            listener.onMagnetGroupChange(line.getMagnetGroup1());
            listener.onMagnetGroupChange(line.getMagnetGroup2());
        }
    }
}
