package com.aivo.hyperion.aivo.models.pojos;

import android.graphics.Color;

/**
 * Created by corpp on 20.10.2015.
 */
public class MagnetPojo {

    private String createdAt;
    private String updateAt;
    private int userId;
    private int mindmapId;
    private int magnetId;
    private int magnetGroupId;
    private int color;
    private String title;
    private String content;

    public MagnetPojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.magnetId = -1;
        this.magnetGroupId = -1;
        this.color = Color.YELLOW;
        this.title = "";
        this.content = "";
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

    public int getMagnetId() {
        return magnetId;
    }

    public void setMagnetId(int magnetId) {
        this.magnetId = magnetId;
    }

    public int getMagnetGroupId() {
        return magnetGroupId;
    }

    public void setMagnetGroupId(int magnetGroupId) {
        this.magnetGroupId = magnetGroupId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
