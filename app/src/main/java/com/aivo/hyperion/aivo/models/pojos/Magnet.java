package com.aivo.hyperion.aivo.models.pojos;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corpp on 20.10.2015.
 */
public class Magnet {

    private String createdAt;
    private String updateAt;
    private int userId;
    private int mindMapId;
    private int magnetId;
    private int x;
    private int y;
    private int rootId;
    private int parentId;
    private int noteId;
    private List<Integer> childIds;
    private List<Integer> parentRelativeIds;
    private List<Integer> childRelativeIds;
    private int color;
    private String title;

    public Magnet() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindMapId = -1;
        this.magnetId = -1;
        this.x = -1;
        this.y = -1;
        this.rootId = -1;
        this.parentId = -1;
        this.noteId = -1;
        this.childIds = new ArrayList<>();
        this.parentRelativeIds = new ArrayList<>();
        this.childRelativeIds = new ArrayList<>();
        this.color = Color.YELLOW;
        this.title = "";
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

    public int getMindMapId() {
        return mindMapId;
    }

    public void setMindMapId(int mindMapId) {
        this.mindMapId = mindMapId;
    }

    public int getMagnetId() {
        return magnetId;
    }

    public void setMagnetId(int magnetId) {
        this.magnetId = magnetId;
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

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public List<Integer> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<Integer> childIds) {
        this.childIds = childIds;
    }

    public List<Integer> getParentRelativeIds() {
        return parentRelativeIds;
    }

    public void setParentRelativeIds(List<Integer> parentRelativeIds) {
        this.parentRelativeIds = parentRelativeIds;
    }

    public List<Integer> getChildRelativeIds() {
        return childRelativeIds;
    }

    public void setChildRelativeIds(List<Integer> childRelativeIds) {
        this.childRelativeIds = childRelativeIds;
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
}
