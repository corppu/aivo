package com.aivo.hyperion.aivo.models;

/**
 * Created by MicroLoota on 25.11.2015.
 */
public interface ModelListener {
    void onUserOpened();
    void onMindmapOpened();
    void onNoteOpened();

    void onUserChanged();
    void onMindmapChanged();
    void onMagnetChanged();
    void onNoteChanged();

    void onUserClosed();
    void onMindmapClosed();
    void onNoteClosed();

    void onException(Exception e);

}
