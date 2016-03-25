package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.Note;

public class MagnetCreate extends MagnetAction {

    private MagnetGroup magnetGroup;
    private Magnet magnet;
    private int rowIndex;
    private int colIndex;
    private boolean createNewRow;

    public MagnetCreate(ModelMediator mediator, PointF pointF,
                        String title, String content, final int color) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, title, content, color);
        this.rowIndex = 0;
        this.colIndex = 0;
        this.createNewRow = true;
    }

    public MagnetCreate(ModelMediator mediator, PointF pointF, Note noteReference) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, noteReference);
        this.rowIndex = 0;
        this.colIndex = 0;
        this.createNewRow = true;
    }

    public MagnetCreate(ModelMediator mediator, MagnetGroup magnetGroup, final int rowIndex, final int colIndex,
                        final boolean createNewRow, String title, String content, final int color) {
        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = new Magnet(mediator, magnetGroup, title, content, color);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.createNewRow = createNewRow;
    }

    public MagnetCreate(ModelMediator mediator, MagnetGroup magnetGroup, final int rowIndex, final int colIndex,
                        final boolean createNewRow,  Note noteReference) {
        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = new Magnet(mediator, magnetGroup, noteReference);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.createNewRow = createNewRow;
    }

    @Override
    public void execute() {

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex, createNewRow);
        notifyMagnetCreatedIntoGroup(magnet, magnetGroup);
    }

    @Override
    public void undo() {

        removeMagnetFromGroup(magnet, magnetGroup);
        notifyMagnetDeletedFromGroup(magnet, magnetGroup);
    }
}
