package com.aivo.hyperion.aivo.models;

/**
 * Created by MicroLoota on 25.11.2015.
 */
public interface ModelListener {
    void onUserOpened(User user);
    void onMindmapOpened(Mindmap mindmap);
    void onNoteOpened(Note note);

    void onUserChanged(User user);
    void onMindmapChanged(Mindmap mindmap);
    void onNoteChanged(Note note);

    void onUserClosed();
    void onMindmapClosed();
    void onNoteClosed();

    void onUnableToRead(String filename);
    void onUnableToWrite(String filename);
}
