package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;
import com.aivo.hyperion.aivo.models.pojos.NotePojo;

import java.io.IOException;

/**
 * Created by corpp on 20.10.2015.
 */
public class Note {
    // The local pojo
    private NotePojo pojo;

    // The given local storage module
    private LocalStorageModule lsm;
    private void setLSM(LocalStorageModule lsm_) {
        if (lsm == null)
            throw new InternalError("Note created without a valid LocalStorageModule reference!");
        lsm = lsm_;
    }

    /** Create a new Note with no references.
     *
     * @param lsm_  LocalStorageModule reference. Required!
     */
    public Note(LocalStorageModule lsm_) {
        setLSM(lsm_);
        pojo = new NotePojo();
    }

    /** Create a Note from a existing file.
     *
     * @param lsm_          LocalStorageModule reference. Required!
     * @param userId        User identifier.
     * @param noteId        Note identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    public Note(LocalStorageModule lsm_, final int userId, final int noteId) throws IOException {
        setLSM(lsm_);
        pojo = lsm.loadNote(userId, noteId);
    }
}
