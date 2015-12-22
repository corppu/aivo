package com.aivo.hyperion.aivo.models;

/**
 * Created by MicroLoota on 25.11.2015.
 */
public interface ModelListener {
    void onUserOpened(User user);
    void onMindmapOpened(Mindmap mindmap);

    void onUserChanged(User user);
    void onMindmapChanged(Mindmap mindmap);

    void onUserClosed();
    void onMindmapClosed();

    void onException(Exception e);

}
