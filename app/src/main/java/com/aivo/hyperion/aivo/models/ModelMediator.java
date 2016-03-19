package com.aivo.hyperion.aivo.models;

import java.util.ArrayList;
import java.util.List;

public class ModelMediator {

    // The current User (null if none opened)
    private User user;

    // The currently open Mindmap (null if none opened)
    private Mindmap mindmap;

    // List of all the notes of the user
    private ArrayList<Note> notes;

    // The registered listeners of this ModelMediator
    private ArrayList<ModelListener> listeners;

    // The StorageModule
    private StorageModule storageModule;

    public ModelMediator() {
        user = null;
        mindmap = null;
        notes = new ArrayList<>();
        listeners = new ArrayList<>();
        storageModule = new StorageModule(this);
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

    public User getUser() { return user; }

    public Mindmap getMindmap() { return mindmap; }

    public List<Note> getNotes() { return new ArrayList<>(notes); }

    public List<ModelListener> getListeners() { return new ArrayList<>(listeners); }

    public StorageModule getStorageModule() { return storageModule; }

    //----------------------------------------------------------------------------------------------
    // Public open/close functions for User, Mindmap and Note

    /** Create a new User and open it.
     *
     */
    public void createUser() {
        // Close any opened User
        if (!closeUser()) return;

        user = new User(this);
        for (ModelListener listener : listeners) listener.onUserOpen(user);
    }

    /** First saves and closes any opened Note, then Mindmap and finally the User.
     *
     * @return  True if no user is open after.
     */
    public boolean closeUser() {
        if (user == null) return true;

        // First try to close any opened Mindmap
        if (!closeMindmap())
            return false;

        /*try {
            user.savePojo();
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
            return false;
        }*/

        // Saved successfully, close user
        user = null;

        for (ModelListener listener : listeners) listener.onUserClosed();
        return true;
    }

    /** Create a new Mindmap and open it.
     *
     * @param title     Unique identifier for the new Mindmap.
     */
    public void createMindmap(String title) {
        if (user==null)
            throw new InternalError("Tried to create a Mindmap without first opening a User!");

        // Check that the title isn't used
        if (isMindmapTitleUsed(title))
            throw new InternalError("Tried to create a Mindmap with a used title!");

        // Close any opened Mindmap
        if (!closeMindmap()) return;

        // Create the mindmap
        mindmap = new Mindmap(this, title);
        for (ModelListener listener : listeners) listener.onMindmapOpen(mindmap);

        storageModule.saveMindmap(mindmap);
    }

    /** Close any open Mindmap and open another. Does nothing if the mindmap is not found.
     *
     * @param title     Title of the Mindmap to be opened.
     * @return          True if successful.
     */
    public boolean openMindmap(String title) {
        Mindmap tempMindmap = storageModule.loadMindmap(title);

        if (tempMindmap != null && closeMindmap()) {
            mindmap = tempMindmap;
            for (ModelListener listener : listeners) listener.onMindmapOpen(mindmap);
            return true;
        }

        return false;
    }

    /** Save and close any open Mindmap.
     *
     * @return  True if no Mindmap is open after.
     */
    public boolean closeMindmap() {
        if (mindmap == null) return true;

        // Save the Mindmap locally
        if (!storageModule.saveMindmap(mindmap))
            return false;

        // Saved successfully, close mindmap
        mindmap = null;

        for (ModelListener listener : listeners) listener.onMindmapClosed();
        return true;
    }

    /** Creates a blank new Note.
     *
     * @return  the Note created.
     */
    public Note createNote() {
        Note note = new Note(this);
        notes.add(note);
        for (ModelListener listener : listeners) listener.onNoteCreate(note);
        return note;
    }

    /** Creates a new Note using a Magnet as a reference.
     *
     * @return  the Note created.
     */
    public Note createNote(Magnet magnetReference) {
        Note note = new Note(this, magnetReference);
        notes.add(note);
        for (ModelListener listener : listeners) listener.onNoteCreate(note);
        return note;
    }

    /** Deletes the given Note from the mediator and google drive.
     *
     * @param note  the Note to be deleted.
     */
    public void deleteNote(Note note) {
        if (!notes.remove(note))
            throw new InternalError("Tried to remove a Note that was not found in ModelMediator!");
        for (ModelListener listener : listeners) listener.onNoteDelete(note);
    }

    public boolean isMindmapTitleUsed(String title) {
        return false;
    }

    public boolean isNoteTitleUsed(String title) {
        return false;
    }

}
