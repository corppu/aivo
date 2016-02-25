package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;

import java.util.ArrayList;
import java.util.List;

public class ModelMediator {

    // The current User (null if none opened)
    private User user;

    // The currently open Mindmap (null if none opened)
    private Mindmap mindmap;

    // The registered listeners of this ModelMediator
    private ArrayList<ModelListener> listeners;

    public ModelMediator() {
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

    public User getUser() { return user; }

    public Mindmap getMindmap() { return mindmap; }

    public List<ModelListener> getListeners() { return new ArrayList<ModelListener>(listeners); }

    //----------------------------------------------------------------------------------------------
    // Public open/close functions for User, Note and Magnet

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
        if (isMindmapTitleUsed(title)) return;

        // Close any opened Mindmap
        if (!closeMindmap()) return;

        // Create the mindmap
        mindmap = new Mindmap(this, title);
        for (ModelListener listener : listeners) listener.onMindmapOpen(mindmap);
    }

    /** Save and close any open Mindmap.
     *
     * @return  True if no Mindmap is open after.
     */
    public boolean closeMindmap() {
        if (mindmap == null) return true;

        /*try {
            mindmap.savePojo();
        } catch (IOException e) {
            for (ModelListener listener : listeners) listener.onException(e);
            return false;
        }*/

        // Saved successfully, close mindmap
        mindmap = null;

        for (ModelListener listener : listeners) listener.onMindmapClosed();
        return true;
    }

    public boolean isMindmapTitleUsed(String title) {
        return false;
    }

    public boolean isFavoriteMagnetTitleUsed(String title) {
        return false;
    }

}
