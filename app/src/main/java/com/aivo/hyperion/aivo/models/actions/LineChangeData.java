package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.ModelListener;

public class LineChangeData extends Action {

    private Line line;
    private int typeNew;
    private int typeOld;
    private int colorNew;
    private int colorOld;
    private int thicknessNew;
    private int thicknessOld;

    public LineChangeData(Line line, final int typeNew, final int colorNew, final int thicknessNew) {
        this.line = line;

        this.typeNew = typeNew;
        this.colorNew = colorNew;
        this.thicknessNew = thicknessNew;

        this.typeOld = line.getType();
        this.colorOld = line.getColor();
        this.thicknessOld = line.getThickness();
    }

    @Override
    void execute() {
        line.setData(typeNew, colorNew, thicknessNew);
        for (ModelListener listener : mediator.getListeners())
            listener.onLineChange(line);
    }

    @Override
    void undo() {
        line.setData(typeOld, colorOld, thicknessOld);
            for (ModelListener listener : mediator.getListeners())
                listener.onLineChange(line);
    }
}
