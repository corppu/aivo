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
    private int rowIndexNew;
    private int colIndexNew;
    private int rowIndexOld;
    private int colIndexOld;

    public MagnetMove(ModelMediator mediator, Magnet magnet, PointF pointF) {
        setMediator(mediator);
        this.magnetGroupNew = new MagnetGroup(mediator, pointF);
        this.magnetGroupOld = magnet.getMagnetGroup();
        this.magnet = magnet;
        this.rowIndexNew = 0;
        this.colIndexNew = 0;
        final int[] rowcol = getMagnetRowCol(magnet);
        this.rowIndexOld = rowcol[0];
        this.colIndexOld = rowcol[1];
    }

    public MagnetMove(ModelMediator mediator, Magnet magnet, MagnetGroup magnetGroupNew,
                      final int rowIndexNew, final int colIndexNew) {
        if (rowIndexNew < 0 || colIndexNew < 0)
            throw new InternalError("Tried to create a MagnetMove action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroupNew = magnetGroupNew;
        this.magnetGroupOld = magnet.getMagnetGroup();
        this.magnet = magnet;
        this.rowIndexNew = rowIndexNew;
        this.colIndexNew = colIndexNew;
        final int[] rowcol = getMagnetRowCol(magnet);
        this.rowIndexOld = rowcol[0];
        this.colIndexOld = rowcol[1];
    }

    @Override
    void execute() {
        // Remove from previous MagnetGroup
        removeMagnetFromGroup(magnet, magnetGroupOld);

        // Check if old group is now empty. If so, remove it
        if (magnetGroupOld.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroupOld);

        // Check if the magnetgroup was created for this action
        if (magnetGroupNew.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroupNew);

        insertMagnetIntoGroup(magnet, magnetGroupNew, rowIndexNew, colIndexNew);

        notifyMagnetMoved(magnet, magnetGroupOld, magnetGroupNew);
    }

    @Override
    void undo() {
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
