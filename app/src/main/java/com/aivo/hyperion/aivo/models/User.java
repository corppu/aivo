package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.UserPojo;

import java.io.IOException;

public class User {
    // The local pojo
    private UserPojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    /** Create a new User. Gets required information from the mediator.
     *
     * @param mediator_  ModelMediator reference. Required!
     */
    protected User(ModelMediator mediator_) {
        setMediator(mediator_);
        pojo = new UserPojo();
        pojo.setUserId(0); // TODO: Figure out next available user id
    }

    /** Create a User from a existing file.
     *
     * @param mediator_     ModelMediator reference. Required!
     * @param userId        User identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    protected User(ModelMediator mediator_, final int userId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.lsm.loadUser(userId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getUserId(); }

    public Theme getDefaultTheme() {
        if (pojo.getDefaultThemeId() < 0) {
            return new Theme();
        } else {
            return new Theme();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.lsm.saveUser(pojo);
    }
    protected int getNextObjectId() {
        int nextId = pojo.getObjectIdCounter();
        pojo.setObjectIdCounter(nextId + 1);
        return nextId;
    }
    protected void addMindmapId(final int mindmapId) {
        pojo.getMindmapIds().add(mindmapId);
    }
    protected void deleteMindmapId(final int mindmapId) {
        if (!pojo.getMindmapIds().contains(mindmapId))
            throw new InternalError("Tried to delete a unlisted Mindmap from a User!"); // TODO: Move to mediator
        pojo.getMindmapIds().remove(new Integer(mindmapId));
    }
    protected void addThemeId(final int themeId) {
        pojo.getThemeIds().add(themeId);
    }
    protected void deleteThemeId(final int themeId) {
        if (!pojo.getThemeIds().contains(themeId))
            throw new InternalError("Tried to delete a unlisted Mindmap from a User!"); // TODO: Move to mediator
        pojo.getThemeIds().remove(new Integer(themeId));
    }
}
