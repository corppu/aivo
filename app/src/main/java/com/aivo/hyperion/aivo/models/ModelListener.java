package com.aivo.hyperion.aivo.models;

public interface ModelListener {
    void onUserOpen(User user);
    void onUserChange(User user);
    void onUserClosed();

    void onMindmapOpen(Mindmap mindmap);
    void onMindmapTitleChange(Mindmap mindmap);
    void onMindmapClosed();

    void onMagnetGroupCreate(MagnetGroup magnetGroup);
    void onMagnetGroupChange(MagnetGroup magnetGroup);
    void onMagnetGroupDelete(MagnetGroup magnetGroup);

    void onMagnetCreate(Magnet magnet);
    void onMagnetChange(Magnet magnet);
    void onMagnetDelete(Magnet magnet);

    void onLineCreate(Line line);
    void onLineChange(Line line);
    void onLineDelete(Line line);

    void onNoteCreate(Note note);
    void onNoteChange(Note note);
    void onNoteDelete(Note note);

    void onException(Exception e);

}
