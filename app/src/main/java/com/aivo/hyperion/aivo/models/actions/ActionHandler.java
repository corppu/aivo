package com.aivo.hyperion.aivo.models.actions;

import java.util.Stack;

/**
 * The class to be called from undo and redo button presenter.
 */
public class ActionHandler {
    private Stack<Action> mRedoStack;
    private Stack<Action> mUndoStack;

    public ActionHandler() {
        mRedoStack = new Stack<>();
        mUndoStack = new Stack<>();
    }

    public void executeAction(Action action) {
        mUndoStack.add(action);
        mRedoStack.clear();
        action.execute();
    }

    public boolean canUndo() {
        return mUndoStack.size() > 0;
    }

    public void undo() {
        if (canUndo()) {
            Action action = mUndoStack.pop();
            action.undo();
            mRedoStack.add(action);
        }
    }

    public boolean canRedo() {
        return mRedoStack.size() > 0;
    }

    public void redo() {
        if (canRedo()) {
            Action action = mRedoStack.pop();
            action.execute();
            mUndoStack.add(action);
        }
    }
}
