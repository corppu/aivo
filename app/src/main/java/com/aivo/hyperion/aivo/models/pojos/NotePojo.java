package com.aivo.hyperion.aivo.models.pojos;

/**
 * Created by corpp on 20.10.2015.
 */
public class NotePojo {

    private String createdAt;
    private String updateAt;
    private int userId = -1;
    private int noteId = -1;
    private int mindmapId = -1;
    private int magnetId = -1;
    private int color = -1;
    private String title = "";
    private String content = "";

    public NotePojo() {
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";
        this.userId = -1;
        this.noteId = -1;
        this.mindmapId = -1;
        this.magnetId = -1;
        this.color = -1;
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

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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
