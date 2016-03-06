package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public class MagnetMove extends MagnetAction {

    private MagnetGroup magnetGroupNew;
    private MagnetGroup magnetGroupOld;
    private Magnet magnet;
    private int rowIndex;
    private int colIndex;
    private int rowIndexOld;
    private int colIndexOld;

    public MagnetMove(ModelMediator mediator, Magnet magnet, PointF pointF) {
        setMediator(mediator);
        this.magnetGroupNew = new MagnetGroup(mediator, pointF);
        this.magnetGroupOld = magnet.getMagnetGroup();
        this.magnet = magnet;
        rowIndex = -1;
        colIndex = -1;
    }

    public MagnetMove(ModelMediator mediator, Magnet magnet, MagnetGroup magnetGroupNew,
                      final int rowIndex, final int colIndex) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a MagnetMove action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroupNew = magnetGroupNew;
        this.magnetGroupOld = magnet.getMagnetGroup();
        this.magnet = magnet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    @Override
    void execute() {
        // TODO: LSM: Write to file

        // Remove from previous MagnetGroup
        removeMagnetFromGroup(magnet, magnetGroupOld);

        // Check if old group is now empty. If so, remove it
        if (magnetGroupOld.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroupOld);

        // Check if the magnetgroup was created for this action
        if (magnetGroupNew.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroupNew);

        insertMagnetIntoGroup(magnet, magnetGroupNew, rowIndex, colIndex);

        notifyMagnetMoved(magnet, magnetGroupOld, magnetGroupNew);
    }

    @Override
    void undo() {
        // TODO: LSM: Remove file

        // Remove magnet from group
        removeMagnetFromGroup(magnet, magnetGroupNew);

        // Check if the magnetgroup was created for this action
        if (magnetGroupNew.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroupNew);

        // Check if the old group was removed due to emptiness
        if (magnetGroupOld.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroupOld);

        insertMagnetIntoGroup(magnet, magnetGroupOld, rowIndexOld, colIndexOld);

        notifyMagnetMoved(magnet, magnetGroupNew, magnetGroupOld);
    }
}
