package com.aivo.hyperion.aivo.models.actions;

import android.util.Log;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelListener;

import java.util.ArrayList;
import java.util.List;

public abstract class MagnetAction extends Action {


    protected void insertMagnetIntoGroup(Magnet magnet, MagnetGroup magnetGroup,
                                         final int rowIndex, final int colIndex, final boolean createNewRow) {
        final List<List< Magnet >> magnetRows = magnetGroup.getMagnets();
        List< Magnet > magnetRow;

        // Find the magnet row to add the magnet to
        if (rowIndex == magnetRows.size() || createNewRow) {
            magnetRow = new ArrayList<>();
            magnetRows.add(rowIndex, magnetRow);
        } else
            magnetRow = magnetRows.get(rowIndex);

        // Add the magnet in the correct position
        if (colIndex >= magnetRow.size())
            magnetRow.add(magnet);
        else
            magnetRow.add(colIndex, magnet);

        // Check if the magnetGroup was "resurrected", and if so re-add it and its lines to model
        if (magnetGroup.getMagnetCount() == 1) {
            mediator.getMindmap().getMagnetGroups().add(magnetGroup);
            for (Line line : magnetGroup.getLines()) {
                mediator.getMindmap().getLines().add(line);
                if (line.getMagnetGroup1() != magnetGroup)
                    line.getMagnetGroup1().getLines().add(line);
                if (line.getMagnetGroup2() != magnetGroup)
                    line.getMagnetGroup2().getLines().add(line);
            }
        }

        // Add the group reference to the magnet
        magnet.setMagnetGroup(magnetGroup);
    }

    protected void removeMagnetFromGroup(Magnet magnet, MagnetGroup magnetGroup) {
        boolean found = false;
        for (List< Magnet > magnetRow : magnetGroup.getMagnets())
            if (magnetRow.remove(magnet)) {
                found = true;
                if (magnetRow.isEmpty()) magnetGroup.getMagnets().remove(magnetRow);
                break;
            }

        if (!found)
            throw new InternalError("Tried to remove magnet from a group it wasn't inside in!");

        // Remove group and associated lines, if group becomes empty
        if (magnetGroup.getMagnetCount() == 0) {
            mediator.getMindmap().getMagnetGroups().remove(magnetGroup);
            for (Line line : magnetGroup.getLines()) {
                mediator.getMindmap().getLines().remove(line);
                if (line.getMagnetGroup1() != magnetGroup)
                    line.getMagnetGroup1().getLines().remove(line);
                if (line.getMagnetGroup2() != magnetGroup)
                    line.getMagnetGroup2().getLines().remove(line);
            }
        }

        // Remove the group reference from the magnet
        magnet.setMagnetGroup(null);
    }

    protected void moveMagnetGroupLines(MagnetGroup magnetGroupOld, MagnetGroup magnetGroupNew, List<Line> lines) {
        for (Line line : lines) {
            if (line.getMagnetGroup1() == magnetGroupOld)
                line.setGroups(magnetGroupNew, line.getMagnetGroup2());
            else if (line.getMagnetGroup2() == magnetGroupOld)
                line.setGroups(line.getMagnetGroup1(), magnetGroupNew);
            else throw new InternalError("Tried to move lines from a group they were not connected to!");
            
            magnetGroupOld.getLines().remove(line);
            magnetGroupNew.getLines().add(line);
        }
    }

    protected void notifyMagnetCreatedIntoGroup(Magnet magnet, MagnetGroup magnetGroup) {
        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetCreate(magnet);
            // Handle "resurrection" of a group, if necessary
            if (magnetGroup.getMagnetCount() == 1) {
                listener.onMagnetGroupCreate(magnetGroup);

                for (Line line : magnetGroup.getLines())
                    listener.onLineCreate(line);
            }
            else
                listener.onMagnetGroupChange(magnetGroup);
        }
    }

    protected void notifyMagnetDeletedFromGroup(Magnet magnet, MagnetGroup magnetGroup) {
        for (ModelListener listener : mediator.getListeners()) {
            listener.onMagnetDelete(magnet);
            // handle removal of a group, if necessary
            if (magnetGroup.getMagnetCount() == 0) {
                for (Line line : magnetGroup.getLines())
                    listener.onLineDelete(line);
                listener.onMagnetGroupDelete(magnetGroup);
            }
            else
                listener.onMagnetGroupChange(magnetGroup);
        }
    }

    protected void notifyMagnetMoved(Magnet magnet, MagnetGroup magnetGroupOld, MagnetGroup magnetGroupNew) {
        for (ModelListener listener : mediator.getListeners()) {

            // Handle movement within a group
            if (magnetGroupOld == magnetGroupNew) {
                listener.onMagnetGroupChange(magnetGroupNew);
                continue;
            }

            // Handle "resurrection" of a group, if necessary
            if (magnetGroupNew.getMagnetCount() == 1) {
                listener.onMagnetGroupCreate(magnetGroupNew);
                for (Line line : magnetGroupNew.getLines())
                    listener.onLineCreate(line);
            }
            else
                listener.onMagnetGroupChange(magnetGroupNew);

            listener.onMagnetChange(magnet);

            // Handle removal of a group, if necessary
            if (magnetGroupOld.getMagnetCount() == 0) {
                for (Line line : magnetGroupOld.getLines())
                    listener.onLineDelete(line);
                listener.onMagnetGroupDelete(magnetGroupOld);
            }
            else
                listener.onMagnetGroupChange(magnetGroupOld);
        }
    }

    protected int[] getMagnetRowCol(Magnet target) {
        int rowIndex = -1;
        int colIndex;

        for (List< Magnet > magnetRow : target.getMagnetGroup().getMagnets()) {
            ++rowIndex;
            colIndex = -1;
            for (Magnet magnet : magnetRow) {
                ++colIndex;
                if (magnet == target)
                    return new int[] {rowIndex, colIndex};
            }
        }
        throw new InternalError("Tried to find row/col of a magnet, that had no group or was not in its group!");
    }

    protected boolean getIsMagnetAloneOnRow(Magnet magnet) {
        for (List<Magnet> magnetRow : magnet.getMagnetGroup().getMagnets())
            if (magnetRow.contains(magnet)) {
                return magnetRow.size() == 1;
            }
        throw new InternalError("Tried to check if a magnet was alone on its row, but the magnet had no group or was not in its group!");
    }
}
