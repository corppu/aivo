package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 25.2.2016.
 */
public class MagnetDelete extends MagnetAction {

    private Magnet magnet;
    private MagnetGroup magnetGroup;
    private int rowIndex;
    private int colIndex;

    public MagnetDelete(ModelMediator mediator, Magnet magnet) {
        setMediator(mediator);
        this.magnet = magnet;
        this.magnetGroup = magnet.getMagnetGroup();
        this.rowIndex = 0; //TODO: fix
        this.colIndex = 0;
    }

    @Override
    void execute() {
        removeMagnetFromGroup(magnet, magnetGroup);

        // check if group is now empty
        if (magnetGroup.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroup);

        notifyMagnetDeletedFromGroup(magnet, magnetGroup);
    }

    @Override
    void undo() {
        // check if group was removed
        if (magnetGroup.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroup);

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex);

        notifyMagnetCreatedIntoGroup(magnet, magnetGroup);
    }
}
