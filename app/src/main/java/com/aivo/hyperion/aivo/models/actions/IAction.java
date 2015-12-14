package com.aivo.hyperion.aivo.models.actions;

import android.view.View;

import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * IAction interface enables undoable and redoable action.
 */
interface IAction {
    void execute();
    void undo();
    void setMediator(final ModelMediator mediator);
    void setView(final View view);
}

