package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class LineChangeData extends Action {

    private Line line;
    private int typeNew;
    private int typeOld;
    private int thicknessNew;
    private int thicknessOld;

    public LineChangeData(ModelMediator mediator, Line line, final int typeNew, final int thicknessNew) {
        setMediator(mediator);

        this.line = line;

        this.typeNew = typeNew;
        this.thicknessNew = thicknessNew;

        this.typeOld = line.getType();
        this.thicknessOld = line.getThickness();
    }

    @Override
    void execute() {
        line.setData(typeNew, thicknessNew);
        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }

    @Override
    void undo() {
        line.setData(typeOld, thicknessOld);
            for (ModelListener listener : mediator.getListeners())
                listener.onLineChange(line);
    }
}
