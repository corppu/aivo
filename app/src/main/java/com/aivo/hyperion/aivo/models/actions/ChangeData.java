package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * Created by MicroLoota on 11.2.2016.
 */
public class ChangeData extends Action {
    private Magnet magnet;
    private MagnetGroup magnetGroup;
    private Mindmap mindmap;

    private String titleNew;
    private String titleOld;
    private String contentNew;
    private String contentOld;

    public ChangeData(Magnet magnet, final String newTitle) {
        setMediator(mediator);
        this.magnet = magnet;
        this.magnetGroup = null;
        this.mindmap = null;

        this.titleOld = magnet.getTitle();
        this.titleNew = newTitle;
        this.contentOld = magnet.getContent();
        this.contentNew = this.contentOld;
    }

    public ChangeData(Magnet magnet, final String newTitle, final String newContent) {
        setMediator(mediator);
        this.magnet = magnet;
        this.magnetGroup = null;
        this.mindmap = null;

        this.titleOld = magnet.getTitle();
        this.titleNew = newTitle;
        this.contentOld = magnet.getContent();
        this.contentNew = newContent;
    }

    public ChangeData(MagnetGroup magnetGroup, final String newTitle) {
        setMediator(mediator);
        this.magnet = null;
        this.magnetGroup = magnetGroup;
        this.mindmap = null;

        this.titleOld = magnetGroup.getTitle();
        this.titleNew = newTitle;
    }

    public ChangeData(Mindmap mindmap, final String newTitle) {
        setMediator(mediator);
        this.magnet = null;
        this.magnetGroup = null;
        this.mindmap = mindmap;

        this.titleOld = mindmap.getTitle();
        this.titleNew = newTitle;
    }

    @Override
    void execute() {
        if (magnet != null)  {
            magnet.setData(titleNew, contentNew);
        } else if (magnetGroup != null) {
            magnetGroup.setData(titleNew);
        } else if (mindmap != null) {
            mindmap.setData(titleNew);
        }
    }

    @Override
    void undo() {
        if (magnet != null)  {
            magnet.setData(titleOld, contentOld);
        } else if (magnetGroup != null) {
            magnetGroup.setData(titleOld);
        } else if (mindmap != null) {
            mindmap.setData(titleOld);
        }
    }
}
