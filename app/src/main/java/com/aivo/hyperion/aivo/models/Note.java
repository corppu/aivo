package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.NotePojo;

import java.io.IOException;

public class Note {
    // The local pojo
    private NotePojo pojo;

    // The model mediator reference
    private ModelMediator mediator;
    private void setMediator(ModelMediator modelMediator_) {
        if (modelMediator_ == null)
            throw new InternalError("User created without a valid ModelMediator reference!");
        mediator = modelMediator_;
    }

    /** Create a new Note. Gets required information from the mediator.
     *
     * @param mediator_  LocalStorageModule reference. Required!
     */
    public Note(ModelMediator mediator_) {
        setMediator(mediator_);
        pojo = new NotePojo();

        // Set identifiers and update other models
        pojo.setUserId(mediator.getUser().getId());
        pojo.setMindmapId(mediator.getUser().getAddNextFreeNoteId());
    }

    /** Create a Note from a existing file.
     *
     * @param mediator_     LocalStorageModule reference. Required!
     * @param noteId        Note identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    public Note(ModelMediator mediator_, final int noteId) throws IOException {
        setMediator(mediator_);
        pojo = mediator.getLSM().loadNote(mediator.getUser().getId(), noteId);
    }

    //----------------------------------------------------------------------------------------------
    // Public interface
    public int getId() { return pojo.getNoteId(); }

    //----------------------------------------------------------------------------------------------
    // Protected model functions

    // TODO: Jos noten aukaisee ja irrottaa magneetista, täytyy päivittää kyseinen magneetti...
}
