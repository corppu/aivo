package com.aivo.hyperion.aivo.models.actions;

import com.aivo.hyperion.aivo.models.ModelMediator;

/**
 * IAction interface enables undoable and redoable action.
 */
public abstract class Action {
    protected ModelMediator mediator;

    abstract void execute();
    abstract void undo();
    final protected void setMediator(final ModelMediator mediator_) {
        if (mediator_ == null)
            throw new InternalError("Action created without a valid ModelMediator reference!");
        mediator = mediator_;
    }
}

