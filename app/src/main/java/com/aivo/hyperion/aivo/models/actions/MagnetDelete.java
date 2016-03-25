package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class MagnetDelete extends MagnetAction {

    private Magnet magnet;
    private MagnetGroup magnetGroup;
    private int rowIndex;
    private int colIndex;
    private boolean removedRow;

    public MagnetDelete(ModelMediator mediator, Magnet magnet) {
        setMediator(mediator);
        this.magnet = magnet;
        this.magnetGroup = magnet.getMagnetGroup();
        final int[] rowcol = getMagnetRowCol(magnet);
        this.rowIndex = rowcol[0];
        this.colIndex = rowcol[1];
        this.removedRow = magnetGroup.getIsMagnetAloneOnRow(magnet); // Row is removed, if it becomes empty
    }

    @Override
    void execute() {

        removeMagnetFromGroup(magnet, magnetGroup);
        notifyMagnetDeletedFromGroup(magnet, magnetGroup);
    }

    @Override
    void undo() {

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex, removedRow);
        notifyMagnetCreatedIntoGroup(magnet, magnetGroup);
    }
}
