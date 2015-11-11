package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;

import java.io.IOException;

import java.util.ArrayList;

public class ModelMediator {
    // Create one LSM to be used by all model classes in the package
    private LocalStorageModule lsm;
    protected LocalStorageModule getLSM() { return lsm; }

    // The current User (null if none opened)
    private User user;

    // The currently open Mindmap (null if none opened)
    private Mindmap mindmap;

    // The currently open Note (null if none opened)
    private Note note;

    // List of magnets in the current mindmap (Never null, use clear!)
    private ArrayList<Magnet> magnets;

    public ModelMediator() {
        lsm = new LocalStorageModule();
        user = null;
        mindmap = null;
        magnets = new ArrayList<Magnet>();
    }

    //----------------------------------------------------------------------------------------------
    // Public getters
    /** Get current User. Must first open a User!
     *
     * @return  User.
     */
    public User getUser() {
        if (user==null)
            throw new InternalError("Tried to get User without first opening one!");
        return user;
    }

    /** Get current Mindmap. Must first open a Mindmap!
     *
     * @return  Mindmap.
     */
    public Mindmap getMindmap() {
        if (mindmap==null)
            throw new InternalError("Tried to get Mindmap without first opening one!");
        return mindmap;
    }

    /** Get all Magnets of current Mindmap. Must first open a Mindmap!
     *
     * @return  Magnets.
     */
    public ArrayList<Magnet> getMagnets() {
        if (mindmap==null)
            throw new InternalError("Tried to get Magnets without first opening a Mindmap!");
        return magnets;
    }

    /** Get a specific Magnet from the current Mindmap.
     * Must first open a Mindmap! Magnet must exist!
     *
     * @param magnetId  Identifier.
     * @return          Magnet.
     */
    public Magnet getMagnet(final int magnetId) {
        if (mindmap==null)
            throw new InternalError("Tried to get a Magnet without first opening a Mindmap!");
        for (Magnet magnet : magnets) {
            if (magnet.getId() == magnetId)
                return magnet;
        }
        throw new InternalError("Tried to open a Magnet that didn't exist in the current Mindmap!");
    }

    /** Get the currently open Note. Must first open a Note!
     *
     * @return  Note.
     */
    public Note getNote() {
        if (note==null)
            throw new InternalError("Tried to get Note without first opening one!");
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
                // TODO: Event: Unable to read the User file!
                return;
            }
        }
        // TODO: Event: User opened
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
                // TODO: Event: Unable to read the Mindmap file!
                return;
            }
        }

        // Try to open the magnets of the mindmap
        try {
            mindmap.openMagnets();
        } catch (IOException e) {
            mindmap = null;
            magnets.clear();
            // TODO: Event: Unable to read a Mindmap's Magnet file!
            return;
        }

        // TODO: Event: Mindmap opened
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
            // TODO: Event: Unable to read the Note file!
        }

        // TODO: Event: Note opened
    }

    /** First saves and closes any opened Note, then Mindmap with its Magnet and finally the User.
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
            // TODO: Event: Unable to save the User file!
            return false;
        }

        // Saved successfully, close user
        user = null;

        // TODO: Event: User closed
        return true;
    }

    /** Save and close any open Mindmap and its Magnets.
     *
     * @return  True if no Mindmap is open after. (no Magnets either)
     */
    public boolean closeMindmap() {
        if (mindmap == null) return true;

        try {
            mindmap.savePojo();
        } catch (IOException e) {
            // TODO: Event: Unable to save the Mindmap file!
            return false;
        }

        try {
            mindmap.saveMagnets();
        } catch (IOException e) {
            // TODO: Event: Unable to save a Mindmap Magnet file!
            return false;
        }

        // Saved successfully, close them
        magnets.clear();
        mindmap = null;

        // TODO: Event: Mindmap closed
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
            // TODO: Event: Unable to save the Note file!
            return false;
        }

        // Saved successfully, close note
        note = null;

        // TODO: Event: Note closed
        return true;
    }


    //----------------------------------------------------------------------------------------------
    // Public Magnet functions
    public void addMagnet(final int x, final int y) { // TODO:  root/parent etc
        if (user==null)
            throw new InternalError("Tried to add a Magnet without first opening a User!");
        if (mindmap==null)
            throw new InternalError("Tried to add a Magnet without first opening a Mindmap!");

    }

    public void removeMagnet(final int magnetId) {
        if (user==null)
            throw new InternalError("Tried to remove a Magnet without first opening a User!");
        if (mindmap==null)
            throw new InternalError("Tried to remove a Magnet without first opening a Mindmap!");
        if (!mindmap.getMagnetIds().contains(magnetId))
            throw new InternalError("Tried to remove a unlisted Magnet!");


    }
}
