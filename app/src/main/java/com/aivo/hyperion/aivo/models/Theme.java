package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.ThemePojo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MicroLoota on 22.12.2015.
 */
public class Theme {
    // The local pojo
    private ThemePojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    /** Create a new Theme. Gets required information from the Mediator.
     *
     * @param mediator_  ModelMediator reference. Required!
     */
    protected Theme(ModelMediator mediator_) {
        setMediator(mediator_);
        pojo = new ThemePojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.user.getId());
        pojo.setThemeId(mediator.user.getNextObjectId());
        mediator.user.addThemeId(pojo.getThemeId());
    }

    /** Create a Theme from a existing file.
     *
     * @param mediator_  ModelMediator reference. Required!
     * @param themeId    Theme identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    protected Theme(ModelMediator mediator_, final int themeId) throws IOException {
        setMediator(mediator_);
        //pojo = mediator.lsm.loadTheme(mediator.user.getId(), themeId);
    }

    // For default theme
    protected Theme() {}

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getThemeId(); }

}
