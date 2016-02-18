package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corpp on 14.12.2015.
 */
public class MagnetCreate extends MagnetAction {

    private MagnetGroup magnetGroup;
    private Magnet magnet;
    private int rowIndex;
    private int colIndex;

    public MagnetCreate(ModelMediator mediator, PointF pointF) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup);
        this.rowIndex = -1;
        this.colIndex = -1;
    }

    public MagnetCreate(ModelMediator mediator, MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a MagnetCreate action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = new Magnet(mediator, magnetGroup);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    @Override
    public void execute() {
        // TODO: LSM: Write to file

        // Check if the magnetgroup was created for this action
        if (rowIndex < 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroup);

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex);
    }

    @Override
    public void undo() {
        // TODO: LSM: Remove file

        // Remove magnet from group
        removeMagnetFromGroup(magnet, magnetGroup);

        // Check if the magnetgroup was created for this action
        if (rowIndex < 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroup);
    }
}
