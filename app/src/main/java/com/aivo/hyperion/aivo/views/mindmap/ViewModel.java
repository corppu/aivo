package com.aivo.hyperion.aivo.views.mindmap;

import android.graphics.RectF;

/**
 * Created by corpp on 6.4.2016.
 */
public interface ViewModel {
    Object getModel();
    void getOuterRectF(RectF outerRectF);
    void setIsSelected(boolean isSelected);
    boolean getIsSelected();
    void setHasMoved(boolean hasMoved);
    boolean getHasMoved();
    void setIsGhost(boolean isGhost);
    boolean getIsGhost();
    void setIsHighLighted(boolean isHighLighted);
    boolean getIsHighLighted();
    void setLastTouchPoint(float canvasTouchX, float canvasTouchY);
    float getLastTouchX();
    float getLastTouchY();
    void moveBy(float distanceX, float distanceY);
}
