package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public abstract class MagnetAction extends Action {


    protected void insertMagnetIntoGroup(Magnet magnet, MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        List<List< Magnet >> magnets = magnetGroup.getMagnets();
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

    protected void removeMagnetFromGroup(Magnet magnet, MagnetGroup magnetGroup) {

        for (List< Magnet > magnetRow : magnetGroup.getMagnets())
            if (magnetRow.remove(magnet))
                return;
        throw new InternalError("Tried to remove magnet from a group it wasn't inside!");
    }
}
