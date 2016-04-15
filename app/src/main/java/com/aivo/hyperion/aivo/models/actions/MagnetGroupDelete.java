package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class MagnetGroupDelete extends Action {

    private MagnetGroup magnetGroup;

    public MagnetGroupDelete(ModelMediator mediator, MagnetGroup magnetGroup) {
        setMediator(mediator);
        this.magnetGroup = magnetGroup;
    }

    @Override
    void execute() {

        mediator.getMindmap().getMagnetGroups().remove(magnetGroup);
        for (Line line : magnetGroup.getLines()) {
            mediator.getMindmap().getLines().remove(line);
            if (line.getMagnetGroup1() != magnetGroup)
                line.getMagnetGroup1().getLines().remove(line);
            if (line.getMagnetGroup2() != magnetGroup)
                line.getMagnetGroup2().getLines().remove(line);
        }

        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetGroupDelete(magnetGroup);
        }
    }

    @Override
    void undo() {

        mediator.getMindmap().getMagnetGroups().add(magnetGroup);
        for (Line line : magnetGroup.getLines()) {
            mediator.getMindmap().getLines().add(line);
            if (line.getMagnetGroup1() != magnetGroup)
                line.getMagnetGroup1().getLines().add(line);
            if (line.getMagnetGroup2() != magnetGroup)
                line.getMagnetGroup2().getLines().add(line);
        }

        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetGroupDelete(magnetGroup);
        }
    }
}
