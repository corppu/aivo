package com.aivo.hyperion.aivo.views;

import android.graphics.PointF;
import java.util.ArrayList;


/**
 * Created by corpp on 29.1.2016.
 */
public class MagnetGroupViewModel {

    static private MagnetViewModel compare(MagnetViewModel magnetViewModel,
                                           MagnetViewModel nearestModel, MagnetViewModel compareModel) {

        if (nearestModel != null) {
            float distanceNearest =
                    (float) Math.hypot(
                            (double)(magnetViewModel.pointF.x - nearestModel.pointF.x),
                            (double)(magnetViewModel.pointF.y - nearestModel.pointF.y));

            float distanceCompare =
                    (float) Math.hypot(
                            (double)(magnetViewModel.pointF.x - compareModel.pointF.x),
                            (double)(magnetViewModel.pointF.y - compareModel.pointF.y));


            if (distanceCompare > distanceNearest) return nearestModel;
        }

        return compareModel;
    }

    public void push(MagnetViewModel magnetViewModel) {
        int nearestRow = 0;
        int nearestColumn = 0;

        MagnetViewModel nearestModel = null;
        MagnetViewModel compareModel = null;

        for (int row = 0; row < magnetViewModels.size(); row++) {
            for (int column = 0; column < magnetViewModels.get(row).size(); column++) {
                compareModel = magnetViewModels.get(row).get(column);
                nearestModel = compare(magnetViewModel, nearestModel, compareModel);
                if (nearestModel == compareModel) {
                    nearestColumn = column;
                    nearestRow = row;
                }
            }
        }

        int pushColumn = nearestColumn;
        int pushRow = nearestRow;

        if (nearestModel.pointF.x >= magnetViewModel.pointF.x) {

        }

        if (nearestModel.pointF.y >= magnetViewModel.pointF.y) {

        }

        PointF modifier = new PointF(MagnetViewModel.HALF_WIDTH * 2 + MagnetViewModel.HIGHLIGHT_BORDER_SIZE * 3,
                MagnetViewModel.HALF_HEIGHT * 2 + MagnetViewModel.HIGHLIGHT_BORDER_SIZE * 3);
    }


    // Draw magnets:
    void drawRow(int row) {

    }


    private ArrayList<ArrayList<MagnetViewModel>> magnetViewModels = new ArrayList<>();
    public PointF pointF = new PointF();
    public boolean isSelected = false;
    public String title = "Preview";
}
