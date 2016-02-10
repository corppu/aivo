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
public class CreateMagnet extends Action {

    private MagnetGroup magnetGroup;
    private Magnet magnet;
    private int rowIndex;
    private int colIndex;

    public CreateMagnet(ModelMediator mediator, PointF pointF) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup);
        this.rowIndex = -1;
        this.colIndex = -1;
    }

    public CreateMagnet(ModelMediator mediator, MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a CreateMagnet action with negative row/col indexes!");

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

        insertMagnetIntoGroup();
    }

    @Override
    public void undo() {
        // TODO: LSM: Remove file

        // Remove magnet from group
        for (List< Magnet > magnetRow : magnetGroup.getMagnets())
            if (magnetRow.remove(magnet))
                break;

        // Check if the magnetgroup was created for this action
        if (rowIndex < 0)
            mediator.getMindmap().getMagnetGroups().remove(magnetGroup);
    }

    private void insertMagnetIntoGroup() {
        List< List< Magnet > > magnets = magnetGroup.getMagnets();
        List< Magnet > magnetRow;

        // If the group is new, just add on the first row
        if (rowIndex < 0) {
            magnetRow = new ArrayList<>();
            magnetRow.add(magnet);
            magnets.add(magnetRow);

        } else {

            // Find the magnet row to add the magnet to
            if (rowIndex >= magnets.size()) {
                magnetRow = new ArrayList<>();
                magnets.add(magnetRow);
            } else
                magnetRow = magnets.get(rowIndex);

            // Add the magnet in the correct position
            if (colIndex >= magnetRow.size())
                magnetRow.add(magnet);
            else
                magnetRow.add(colIndex, magnet);
        }
    }
}
