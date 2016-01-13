package com.aivo.hyperion.aivo.models.pojos;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corpp on 20.10.2015.
 */
public class MindmapPojo {

    private String createdAt;
    private String updateAt;
    private int userId;
    private int mindmapId;
    private List<Integer> magnetGroupIds;
    private List<Integer> lineIds;
    private int color;
    private String title;
    private int thumbnailId;

    public MindmapPojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.magnetGroupIds = new ArrayList<>();
        this.lineIds = new ArrayList<>();
        this.color = Color.WHITE;
        this.title = "";
        this.thumbnailId = -1;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public List<Integer> getMagnetGroupIds() {
        return magnetGroupIds;
    }

    public void setMagnetGroupIds(List<Integer> magnetGroupIds) {
        this.magnetGroupIds = magnetGroupIds;
    }

    public List<Integer> getLineIds() {
        return lineIds;
    }

    public void setLineIds(List<Integer> lineIds) {
        this.lineIds = lineIds;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }
}
