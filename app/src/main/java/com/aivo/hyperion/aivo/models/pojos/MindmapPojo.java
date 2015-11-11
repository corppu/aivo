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
    private int magnetIdCounter;
    private List<Integer> removedMagnetIds;
    private List<Integer> magnetIds;
    private int lineIdCounter;
    private List<Integer> removedLineIds;
    private List<Integer> lineIds;
    private int color;
    private String title;
    private int thumbnailId;

    public MindmapPojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.magnetIdCounter = 0;
        this.removedMagnetIds = new ArrayList<>();
        this.magnetIds = new ArrayList<>();
        this.lineIdCounter = 0;
        this.removedLineIds = new ArrayList<>();
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

    public int getMagnetIdCounter() {
        return magnetIdCounter;
    }

    public void setMagnetIdCounter(int magnetIdCounter) {
        this.magnetIdCounter = magnetIdCounter;
    }

    public List<Integer> getRemovedMagnetIds() {
        return removedMagnetIds;
    }

    public void setRemovedMagnetIds(List<Integer> removedMagnetIds) {
        this.removedMagnetIds = removedMagnetIds;
    }

    public List<Integer> getMagnetIds() {
        return magnetIds;
    }

    public void setMagnetIds(List<Integer> magnetIds) {
        this.magnetIds = magnetIds;
    }

    public int getLineIdCounter() {
        return lineIdCounter;
    }

    public void setLineIdCounter(int lineIdCounter) {
        this.lineIdCounter = lineIdCounter;
    }

    public List<Integer> getRemovedLineIds() {
        return removedLineIds;
    }

    public void setRemovedLineIds(List<Integer> removedLineIds) {
        this.removedLineIds = removedLineIds;
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
