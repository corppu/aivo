package com.aivo.hyperion.aivo.models;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.actions.Action;
import com.aivo.hyperion.aivo.models.actions.MagnetChangeData;
import com.aivo.hyperion.aivo.models.actions.MagnetDelete;
import com.aivo.hyperion.aivo.models.actions.MagnetMove;

public class Magnet extends Note {

    // Local properties
    private MagnetGroup magnetGroup;
    public MagnetGroup getMagnetGroup() { return magnetGroup; }
    
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("Magnet created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    public Magnet(ModelMediator mediator_, MagnetGroup magnetGroup) {
        setMediator(mediator_);
        this.magnetGroup = magnetGroup;
    }

    public void actionChangeData(String newTitle) {
        Action action = new MagnetChangeData(this, newTitle, content, color);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeData(String newTitle, String newContent) {
        Action action = new MagnetChangeData(this, newTitle, newContent, color);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionChangeColor(final int newColor) {
        Action action = new MagnetChangeData(this, title, content, newColor);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(MagnetGroup newMagnetGroup, final int rowIndex, final int colIndex) {
        Action action = new MagnetMove(mediator, this, magnetGroup, rowIndex, colIndex);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionMoveTo(PointF newPoint) {
        Action action = new MagnetMove(mediator, this, newPoint);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }

    public void actionDelete() {
        Action action = new MagnetDelete(mediator, this);
        mediator.getMindmap().getActionHandler().executeAction(action);
    }
}
