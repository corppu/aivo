package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;

public class MagnetGroupChangeData extends Action {

    private MagnetGroup magnetGroup;
    private String titleNew;
    private String titleOld;

    public MagnetGroupChangeData(MagnetGroup magnetGroup, String titleNew) {
        this.magnetGroup = magnetGroup;
        this.titleNew = titleNew;
        this.titleOld = magnetGroup.getTitle();
    }

    @Override
    void execute() {
        magnetGroup.setData(titleNew);
        for (ModelListener listener : mediator.getListeners())
            listener.onMagnetGroupChange(magnetGroup);
    }

    @Override
    void undo() {
            magnetGroup.setData(titleOld);
            for (ModelListener listener : mediator.getListeners())
                listener.onMagnetGroupChange(magnetGroup);
    }
}
