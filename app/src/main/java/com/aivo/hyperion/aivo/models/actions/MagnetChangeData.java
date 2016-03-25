package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;

public class MagnetChangeData extends Action {

    private Magnet magnet;
    private String titleNew;
    private String titleOld;
    private String contentNew;
    private String contentOld;
    private int colorNew;
    private int colorOld;
    private boolean favouriteNew;
    private boolean favouriteOld;

    public MagnetChangeData(ModelMediator mediator, Magnet magnet, String titleNew, String contentNew,
                            final int colorNew, final boolean favouriteNew) {
        setMediator(mediator);

        this.magnet = magnet;

        this.titleNew = titleNew;
        this.contentNew = contentNew;
        this.colorNew = colorNew;
        this.favouriteNew = favouriteNew;

        this.titleOld = magnet.getTitle();
        this.contentOld = magnet.getContent();
        this.colorOld = magnet.getColor();
        this.favouriteOld = magnet.getIsFavourite();
    }

    @Override
    void execute() {
        magnet.setData(titleNew, contentNew, colorNew);
        magnet.setFavourite(favouriteNew);
        for (ModelListener listener : mediator.getListeners()) { listener.onMagnetChange(magnet); }
    }

    @Override
    void undo() {
        magnet.setData(titleOld, contentOld, colorOld);
        magnet.setFavourite(favouriteOld);
        for (ModelListener listener : mediator.getListeners()) { listener.onMagnetChange(magnet); }
    }
}
