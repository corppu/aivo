package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;

/**
 * Created by MicroLoota on 6.3.2016.
 */
public class MagnetGroupChangeData extends Action {
    MagnetGroup magnetGroup;
    String titleNew;
    String titleOld;

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
