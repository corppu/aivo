package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class LinePointAdd extends Action {

    private Line line;
    private PointF pointNew;
    private int pointIndex;

    public LinePointAdd(ModelMediator mediator, Line line, PointF pointNew, final int pointIndex) {
        if (pointIndex < 0 || pointIndex >= line.getPoints().size())
            throw new InternalError("LinePointAdd created with an invalid pointIndex!");

        setMediator(mediator);
        this.line = line;
        this.pointNew = pointNew;
        this.pointIndex = pointIndex;
    }

    @Override
    void execute() {
        line.getPoints().add(pointIndex, pointNew);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }

    @Override
    void undo() {
        line.getPoints().remove(pointNew);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }
}
