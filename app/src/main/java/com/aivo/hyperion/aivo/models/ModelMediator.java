package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.actions.ActionHandler;
import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;

import java.io.IOException;

import java.util.ArrayList;

public class ModelMediator {
    // The ActioHandler
    protected ActionHandler actionHandler;
    
    // Create one LSM to be used by all model classes in the package
    protected LocalStorageModule lsm;

    // The current User (null if none opened)
    protected User user;

    // The currently open Mindmap (null if none opened)
    protected Mindmap mindmap;

    // The registered listeners of this ModelMediator
    protected ArrayList<ModelListener> listeners;

    public ModelMediator() {
        lsm = new LocalStorageModule();
        user = null;
        mindmap = null;
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

    //----------------------------------------------------------------------------------------------
    // Public open/close functions for User, Note and Magnet

    /** Create a new User and open it.
     *
     */
    public void createUser() {
        // Close any opened User
        if (!closeUser()) return;

        user = new User(this);
        for (ModelListener listener : listeners) listener.onUserOpened(user);
    }
    /** Open a existing User.
     *
     * @param userId    User identifier.
     */
    public void openUser(final int userId) {
        // Close any opened User
        if (!closeUser()) return;

        // Try to open a existing User file
        try {
            user = new User(this, userId);
        } catch (IOException e) {
            user = null;
            for (ModelListener listener : listeners) listener.onException(e);
            return;
        }

        for (ModelListener listener : listeners) listener.onUserOpened(user);
    }

    /** Create a new Mindmap and open it.
     *
     */
    public void createMindmap() {
        if (user==null)
            throw new InternalError("Tried to create a Mindmap without first opening a User!");

        // Close any opened Mindmap
        if (!closeMindmap()) return;

        mindmap = new Mindmap(this);
        for (ModelListener listener : listeners) listener.onMindmapOpened(mindmap);
    }

    /** Open a existing Mindmap.
     *
     * @param mindMapId     Mindmap identifier.
     */
    public void openMindmap(final int mindMapId) {
        if (user==null)
            throw new InternalError("Tried to open a Mindmap without first opening a User!");

        // Close any opened Mindmap
        if (!closeMindmap()) return;

        // Try to open the Mindmap file
        try {
            mindmap = new Mindmap(this, mindMapId);
        } catch (IOException e) {
            mindmap = null;
            for (ModelListener listener : listeners) listener.onException(e);
            return;
        }

        for (ModelListener listener : listeners) listener.onMindmapOpened(mindmap);
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

}
