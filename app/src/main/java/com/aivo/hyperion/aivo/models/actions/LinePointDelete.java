package com.aivo.hyperion.aivo.models.actions;

import android.graphics.PointF;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class LinePointDelete extends Action {

    private Line line;
    private PointF point;
    private int pointIndex;

    public LinePointDelete(ModelMediator mediator, Line line, PointF point) {

        setMediator(mediator);
        this.line = line;
        this.point = point;
        this.pointIndex = line.getPoints().indexOf(point);

        if (this.pointIndex < 0)
            throw new InternalError("Tried to remove a point from a line, that was not in the line!");
    }

    @Override
    void execute() {
        line.getPoints().remove(point);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }

    @Override
    void undo() {
        line.getPoints().add(pointIndex, point);

        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }
}
