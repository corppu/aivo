package com.aivo.hyperion.aivo.models.pojos;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroLoota on 11.11.2015.
 */
public class LinePojo {

    private String createdAt;
    private String updateAt;

    private int userId;
    private int mindmapId;
    private int lineId;

    private int type;
    private int color;
    private int thickness;

    private int magnet1;
    private int magnet2;
    private List<Point> points;

    public LinePojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.lineId = -1;
        this.type = 0;
        this.color = Color.BLACK;
        this.thickness = 10;
        this.magnet1 = -1;
        this.magnet2 = -1;
        this.points = new ArrayList<>();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMindmapId() {
        return mindmapId;
    }

    public void setMindmapId(int mindmapId) {
        this.mindmapId = mindmapId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getMagnet1() {
        return magnet1;
    }

    public void setMagnet1(int magnet1) {
        this.magnet1 = magnet1;
    }

    public int getMagnet2() {
        return magnet2;
    }

    public void setMagnet2(int magnet2) {
        this.magnet2 = magnet2;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
