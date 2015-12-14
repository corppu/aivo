package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;

import java.io.IOException;

import java.util.ArrayList;

public class ModelMediator {
    // Create one LSM to be used by all model classes in the package
    protected LocalStorageModule lsm;

    // The current User (null if none opened)
    protected User user;

    // The currently open Mindmap (null if none opened)
    protected Mindmap mindmap;

    // The currently open Note (null if none opened)
    protected Note note;

    protected ArrayList<ModelListener> listeners;

    public ModelMediator() {
        lsm = new LocalStorageModule();
        user = null;
        mindmap = null;
        note = null;
        listeners = new ArrayList<ModelListener>();
    }

    public void registerListener(ModelListener listener) {
        if (listeners.contains(listener))
            throw new InternalError("Tried to register a already registered ModelListener!");
        listeners.add(listener);
    }

    public void unregisterListener(ModelListener listener) {
        if (!listeners.contains(listener))
            throw new InternalError("Tried to unregister a unregistered ModelListener!");
        listeners.remove(listener);
    }

    public User getUser() {
        return user;
    }

    public Mindmap getMindmap() {
        return mindmap;
    }

    public Note getNote() {
        return note;
    }

    //----------------------------------------------------------------------------------------------
    // Public open/close functions for User, Note and Magnet
    /** Open a existing User, or create and open a new User.
     *
     * @param userId    User identifier. If negative, new User is created with unique id.
     */
    public void openUser(final int userId) {
        // Close any opened User
        closeUser();

        // Do we want to create a new user?
        if (userId < 0) {
            user = new User(this);

        } else {
            // Try to open a existing User file
            try {
                user = new User(this, userId);
            } catch (IOException e) {
                user = null;
                for (ModelListener listener : listeners) listener.onException(e);
                return;
            }
        }

        for (ModelListener listener : listeners) listener.onUserOpened();
    }

    /** Open a existing Mindmap, or create and open a new Mindmap.
     *
     * @param mindMapId     Mindmap identifier. If negative, new Mindmap is created with unique id.
     */
    public void openMindmap(final int mindMapId) {
        // Can't open a Mindmap without first opening a User
        if (user==null)
            throw new InternalError("Tried to open a Mindmap without first opening a User!");

        // Close any opened Mindmap
        closeMindmap();

        // Do we want to create a new Mindmap?
        if (mindMapId < 0) {
            mindmap = new Mindmap(this);

        } else {
            // Try to open the Mindmap file
            try {
                mindmap = new Mindmap(this, mindMapId);
            } catch (IOException e) {
                mindmap = null;
                for (ModelListener listener : listeners) listener.onException(e);
                return;
            }
        }

        for (ModelListener listener : listeners) listener.onMindmapOpened();
    }

    /** Open a existing Note, or create and open a new Note.
     *
     * @param noteId    Note identifier. If negative, new Note is created with unique id.
     */
    public void openNote(final int noteId) {
        // Can't open a Note without first opening a User
        if (user==null)
            throw new InternalError("Tried to open a Note without opening a User!");

        // Close any opened Note
        closeNote();

        // Try to open the Note file
        try {
            note = new Note(this, noteId);
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
        }

        for (ModelListener listener : listeners) listener.onNoteOpened();
    }

    /** First saves and closes any opened Note, then Mindmap and finally the User.
     *
     * @return  True if no user is open after.
     */
    public boolean closeUser() {
        if (user == null) return true;

        // First try to close any opened Note or Mindmap
        if (!closeNote() || !closeMindmap())
            return false;

        try {
            user.savePojo();
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
            return false;
        }

        // Saved successfully, close user
        user = null;

        for (ModelListener listener : listeners) listener.onUserClosed();
        return true;
    }

    /** Save and close any open Mindmap.
     *
     * @return  True if no Mindmap is open after.
     */
    public boolean closeMindmap() {
        if (mindmap == null) return true;

        try {
            mindmap.savePojo();
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
            return false;
        }

        // Saved successfully, close mindmap
        mindmap = null;

        for (ModelListener listener : listeners) listener.onMindmapClosed();
        return true;
    }

    /** Save and close any open Note.
     *
     * @return  True if no Note is open after.
     */
    public boolean closeNote() {
        if (note == null) return true;

        try {
            note.savePojo();
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
            return false;
        }

        // Saved successfully, close note
        note = null;

        for (ModelListener listener : listeners) listener.onNoteClosed();
        return true;
    }



}
