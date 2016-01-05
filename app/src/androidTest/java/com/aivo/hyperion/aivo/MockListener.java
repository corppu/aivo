package com.aivo.hyperion.aivo;

import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;

/**
 * Created by corpp on 22.12.2015.
 */

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
    public void onUserOpened(User user) {
        this.user = user;
    }

    @Override
    public void onMindmapOpened(Mindmap mindmap) {
        this.mindmap = mindmap;
    }

    @Override
    public void onUserChanged(User user) {
        this.user = user;
    }

    @Override
    public void onMindmapChanged(Mindmap mindmap) {
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
    public void onException(Exception e) {
        this.exception = e;
    }
}
