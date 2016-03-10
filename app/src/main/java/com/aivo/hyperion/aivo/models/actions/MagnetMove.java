package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;

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
        else if (magnetGroupNew == magnet.getMagnetGroup())
            throw new InternalError("Tried to move a Magnet to its own MagnetGroup!");

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
        // Remove from previous MagnetGroup, add to new
        removeMagnetFromGroup(magnet, magnetGroupOld);
        insertMagnetIntoGroup(magnet, magnetGroupNew, rowIndexNew, colIndexNew);
        notifyMagnetMoved(magnet, magnetGroupOld, magnetGroupNew);
    }

    @Override
    void undo() {
        // Remove magnet from new group, add to old
        removeMagnetFromGroup(magnet, magnetGroupNew);
        insertMagnetIntoGroup(magnet, magnetGroupOld, rowIndexOld, colIndexOld);
        notifyMagnetMoved(magnet, magnetGroupNew, magnetGroupOld);
    }
}
