package com.aivo.hyperion.aivo.models.pojos;

/**
 * Created by corpp on 20.10.2015.
 */
public class MediaPojo {

    private String icon;
    private String createdAt;
    private String updateAt;
    private int userId = -1;
    private int mindmapId = -1;
    private int magnetId = -1;
    private String uri = "";

    public MediaPojo() {
        this.icon = "";
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.mindmapId = -1;
        this.magnetId = -1;
        this.uri = "";
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
