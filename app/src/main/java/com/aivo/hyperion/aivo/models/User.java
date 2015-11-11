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
        pojo = mediator.getLSM().loadUser(userId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getUserId(); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions
    protected void savePojo() throws IOException {
        mediator.getLSM().saveUser(pojo);
    }
    protected int getAddNextFreeMindmapId() {
        int nextId;
        if (pojo.getDeletedMindmapIds().size() > 0) {
            nextId = pojo.getDeletedMindmapIds().remove(0);
        } else {
            nextId = pojo.getMindmapIdCounter();
            pojo.setMindmapIdCounter(nextId + 1);
        }
        pojo.getMindmapIds().add(nextId);
        return nextId;
    }
    protected int getAddNextFreeNoteId() {
        int nextId;
        if (pojo.getDeletedNoteIds().size() > 0) {
            nextId = pojo.getDeletedNoteIds().remove(0);
        } else {
            nextId = pojo.getNoteIdCounter();
            pojo.setNoteIdCounter(nextId + 1);
        }
        pojo.getNoteIds().add(nextId);
        return nextId;
    }
    protected void deleteMindmapId(final int mindmapId) {
        if (!pojo.getMindmapIds().contains(mindmapId))
            throw new InternalError("Tried to delete a unlisted Mindmap from a User!"); // TODO: Move to mediator
        pojo.getMindmapIds().remove(new Integer(mindmapId));
        pojo.getDeletedMindmapIds().add(mindmapId);
    }
    protected void deleteNoteId(final int noteId) {
        if (!pojo.getNoteIds().contains(noteId))
            throw new InternalError("Tried to delete a unlisted Note from a User!"); // TODO: Move to mediator
        pojo.getNoteIds().remove(new Integer(noteId));
        pojo.getDeletedNoteIds().add(noteId);
    }
}
