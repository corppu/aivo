package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public class MagnetMove extends MagnetAction {

    private MagnetGroup magnetGroup;
    private Magnet magnet;
    private int rowIndex;
    private int colIndex;
    private MagnetGroup magnetGroupOld;
    private int rowIndexOld;
    private int colIndexOld;

    public MagnetMove(ModelMediator mediator, Magnet magnet, PointF pointF) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = magnet;
        rowIndex = -1;
        colIndex = -1;
        this.magnetGroupOld = magnet.getMagnetGroup();
    }

    public MagnetMove(ModelMediator mediator, Magnet magnet, MagnetGroup magnetGroup,
                      final int rowIndex, final int colIndex) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a MagnetMove action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = magnet;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.magnetGroupOld = magnet.getMagnetGroup();
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
        if (magnetGroup.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroup);

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex);

        notifyMagnetMoved(magnet, magnetGroupOld, magnetGroup);
    }

    @Override
    void undo() {
        // TODO: LSM: Remove file

        // Remove magnet from group
        removeMagnetFromGroup(magnet, magnetGroup);

        // Check if the magnetgroup was created for this action
        if (magnetGroup.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroup);

        // Check if the old group was removed due to emptiness
        if (magnetGroupOld.getMagnets().size() == 0)
            mediator.getMindmap().getMagnetGroups().add(magnetGroupOld);

        insertMagnetIntoGroup(magnet, magnetGroupOld, rowIndexOld, colIndexOld);

        notifyMagnetMoved(magnet, magnetGroup, magnetGroupOld);
    }
}
