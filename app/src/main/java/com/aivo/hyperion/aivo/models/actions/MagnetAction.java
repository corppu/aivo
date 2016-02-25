package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public abstract class MagnetAction extends Action {


    protected void insertMagnetIntoGroup(Magnet magnet, MagnetGroup magnetGroup, final int rowIndex, final int colIndex) {
        List<List< Magnet >> magnetRows = magnetGroup.getMagnets();
        List< Magnet > magnetRow;

        // Find the magnet row to add the magnet to
        if (rowIndex >= magnetRows.size()) {
            magnetRow = new ArrayList<>();
            magnetRows.add(magnetRow);
        } else
            magnetRow = magnetRows.get(rowIndex);
        // Add the magnet in the correct position
        if (colIndex >= magnetRow.size())
            magnetRow.add(magnet);
        else
            magnetRow.add(colIndex, magnet);
    }

    protected void removeMagnetFromGroup(Magnet magnet, MagnetGroup magnetGroup) {

        for (List< Magnet > magnetRow : magnetGroup.getMagnets())
            if (magnetRow.remove(magnet))
                return;
        throw new InternalError("Tried to remove magnet from a group it wasn't inside!");
    }

    protected void notifyMagnetInsertedIntoGroup(Magnet magnet, MagnetGroup magnetGroup) {
        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetCreate(magnet);
            if (magnetGroup.getMagnets().size() == 1)
                listener.onMagnetGroupCreate(magnetGroup);
            else
                listener.onMagnetGroupChange(magnetGroup);
        }
    }

    protected void notifyMagnetRemovedFromGroup(Magnet magnet, MagnetGroup magnetGroup) {
        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetDelete(magnet);
            if (magnetGroup.getMagnets().size() == 0)
                listener.onMagnetGroupDelete(magnetGroup);
            else
                listener.onMagnetGroupChange(magnetGroup);
        }
    }

    protected void notifyMagnetMoved(Magnet magnet, MagnetGroup magnetGroupOld, MagnetGroup magnetGroupNew) {
        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetChange(magnet);

            if (magnetGroupOld.getMagnets().size() == 0)
                listener.onMagnetGroupDelete(magnetGroupOld);
            else
                listener.onMagnetGroupChange(magnetGroupOld);

            if (magnetGroupOld.getMagnets().size() == 1)
                listener.onMagnetGroupCreate(magnetGroupNew);
            else
                listener.onMagnetGroupChange(magnetGroupNew);
        }
    }
}
