package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class LinePointMove extends Action {

    private Line line;
    private PointF pointOld;
    private PointF pointNew;
    private int pointIndex;


    public LinePointMove(ModelMediator mediator, Line line, PointF pointToMove, PointF newPoint) {
        setMediator(mediator);
        this.line = line;
        this.pointOld = pointToMove;
        this.pointNew = newPoint;
        this.pointIndex = line.getPoints().indexOf(pointOld);

        if (this.pointIndex < 0)
            throw new InternalError("Tried to move a point in a line, that was not in the line!");
    }

    @Override
    void execute() {
        line.getPoints().set(pointIndex, pointNew);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }

    @Override
    void undo() {
        line.getPoints().set(pointIndex, pointOld);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }
}
