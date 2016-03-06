package com.aivo.hyperion.aivo;

import com.aivo.hyperion.aivo.models.Line;
import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.Note;
import com.aivo.hyperion.aivo.models.User;

public class MockListener implements ModelListener {

    private Exception exception;
    private ModelMediator mediator;
    private User user;
    private Mindmap mindmap;

    public MockListener() {
        this.mediator = null;
        this.user = null;
        this.mindmap = null;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public ModelMediator getMediator() {
        return mediator;
    }

    public void setMediator(ModelMediator mediator) {
        this.mediator = mediator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mindmap getMindmap() {
        return mindmap;
    }

    public void setMindmap(Mindmap mindmap) {
        this.mindmap = mindmap;
    }

    @Override
    public void onUserOpen(User user) {
        this.user = user;
    }

    @Override
    public void onMindmapOpen(Mindmap mindmap) {
        this.mindmap = mindmap;
    }

    @Override
    public void onUserChange(User user) {
        this.user = user;
    }

    @Override
    public void onMindmapTitleChange(Mindmap mindmap) {
        this.mindmap = mindmap;
    }

    @Override
    public void onUserClosed() {
        this.user = null;
    }

    @Override
    public void onMindmapClosed() {
        this.mindmap = null;
    }

    @Override
    public void onMagnetGroupChange(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetCreate(Magnet magnet) {

    }

    @Override
    public void onMagnetChange(Magnet magnet) {

    }

    @Override
    public void onMagnetDelete(Magnet magnet) {

    }

    @Override
    public void onLineCreate(Line line) {

    }

    @Override
    public void onLineChange(Line line) {

    }

    @Override
    public void onLineDelete(Line line) {

    }

    @Override
    public void onException(Exception e) {
        this.exception = e;
    }

    @Override
    public void onMagnetGroupCreate(MagnetGroup magnetGroup) {

    }

    @Override
    public void onMagnetGroupDelete(MagnetGroup magnetGroup) {

    }

    @Override
    public void onNoteCreate(Note note) {

    }

    @Override
    public void onNoteChange(Note note) {

    }

    @Override
    public void onNoteDelete(Note note) {

    }
}
