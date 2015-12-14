package com.aivo.hyperion.aivo.models.actions;

import java.util.Stack;

/**
 * The class to be called from undo and redo button presenter.
 */
public class ActionHandler {
    private Stack<IAction> mRedoStack;
    private Stack<IAction> mUndoStack;

    public ActionHandler() {
        mRedoStack = new Stack<>();
        mUndoStack = new Stack<>();
    }

    public void executeAction(IAction action) {
        mUndoStack.add(action);
        mRedoStack.clear();
        action.execute();
    }

    public boolean canUndo() {
        return mUndoStack.size() > 0;
    }

    public void undo() {
        if (canUndo()) {
            IAction action = mUndoStack.pop();
            action.undo();
            mRedoStack.add(action);
        }
    }

    public boolean canRedo() {
        return mRedoStack.size() > 0;
    }

    public void redo() {
        if (canRedo()) {
            IAction action = mRedoStack.pop();
            action.execute();
            mUndoStack.add(action);
        }
    }
}
