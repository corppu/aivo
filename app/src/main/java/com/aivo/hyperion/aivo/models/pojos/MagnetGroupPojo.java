package com.aivo.hyperion.aivo.models.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroLoota on 17.12.2015.
 */
public class MagnetGroupPojo {

    private String createdAt;
    private String updateAt;
    private int userId;
    private int mindmapId;
    private int magnetGroupId;
    private int x;
    private int y;
    private int w;
    private int h;
    private String title;
    private List<Integer> magnetIds;
    private List<Integer> lineIds;


    public MagnetGroupPojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.magnetGroupId = -1;
        this.x = -1;
        this.y = -1;
        this.w = 1;
        this.h = 1;
        this.title = "";
        this.magnetIds = new ArrayList<>();
        this.lineIds = new ArrayList<>();
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

    public int getMagnetGroupId() {
        return magnetGroupId;
    }

    public void setMagnetGroupId(int magnetGroupId) {
        this.magnetGroupId = magnetGroupId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getMagnetIds() {
        return magnetIds;
    }

    public void setMagnetIds(List<Integer> magnetIds) {
        this.magnetIds = magnetIds;
    }

    public List<Integer> getLineIds() {
        return lineIds;
    }

    public void setLineIds(List<Integer> lineIds) {
        this.lineIds = lineIds;
    }
}
