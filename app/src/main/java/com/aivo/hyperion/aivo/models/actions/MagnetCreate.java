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

    public MagnetCreate(ModelMediator mediator, PointF pointF,
                        String title, String content, final int color) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, title, content, color);
        this.rowIndex = 0;
        this.colIndex = 0;
    }

    public MagnetCreate(ModelMediator mediator, PointF pointF, Note noteReference) {
        setMediator(mediator);
        this.magnetGroup = new MagnetGroup(mediator, pointF);
        this.magnet = new Magnet(mediator, magnetGroup, noteReference);
        this.rowIndex = 0;
        this.colIndex = 0;
    }

    public MagnetCreate(ModelMediator mediator, MagnetGroup magnetGroup, final int rowIndex, final int colIndex,
                        String title, String content, final int color) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a MagnetCreate action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = new Magnet(mediator, magnetGroup, title, content, color);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public MagnetCreate(ModelMediator mediator, MagnetGroup magnetGroup,
                        final int rowIndex, final int colIndex, Note noteReference) {
        if (rowIndex < 0 || colIndex < 0)
            throw new InternalError("Tried to create a MagnetCreate action with negative row/col indexes!");

        setMediator(mediator);
        this.magnetGroup = magnetGroup;
        this.magnet = new Magnet(mediator, magnetGroup, noteReference);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    @Override
    public void execute() {

        insertMagnetIntoGroup(magnet, magnetGroup, rowIndex, colIndex);
        notifyMagnetCreatedIntoGroup(magnet, magnetGroup);
    }

    @Override
    public void undo() {

        removeMagnetFromGroup(magnet, magnetGroup);
        notifyMagnetDeletedFromGroup(magnet, magnetGroup);
    }
}
