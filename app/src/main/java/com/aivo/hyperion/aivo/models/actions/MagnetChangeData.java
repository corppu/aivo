package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.ModelListener;

public class MagnetChangeData extends Action {

    private Magnet magnet;
    private String titleNew;
    private String titleOld;
    private String contentNew;
    private String contentOld;
    private int colorNew;
    private int colorOld;

    public MagnetChangeData(Magnet magnet, String titleNew, String contentNew, int colorNew) {
        this.magnet = magnet;

        this.titleNew = titleNew;
        this.contentNew = contentNew;
        this.colorNew = colorNew;

        this.titleOld = magnet.getTitle();
        this.contentOld = magnet.getContent();
        this.colorOld = magnet.getColor();
    }

    @Override
    void execute() {
        magnet.setData(titleNew, contentNew, colorNew);
        for (ModelListener listener : mediator.getListeners()) { listener.onMagnetChange(magnet); }
    }

    @Override
    void undo() {
        magnet.setData(titleOld, contentOld, colorOld);
        for (ModelListener listener : mediator.getListeners()) { listener.onMagnetChange(magnet); }
    }
}
