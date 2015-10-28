package com.aivo.hyperion.aivo.models.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corpp on 20.10.2015.
 */
public class UserPojo {

    private int userId;
    private String email;
    private String createdAt;
    private String updateAt;

    private int mindmapIdCounter;
    private List<Integer> mindmapIds;
    private List<Integer> favouriteMindmapIds;
    private List<Integer> deletedMindmapIds;
    private List<Integer> recentMindmapIds;

    private int noteIdCounter;
    private List<Integer> noteIds;
    private List<Integer> favouriteNoteIds;
    private List<Integer> deletedNoteIds;
    private List<Integer> recentNoteIds;

    public UserPojo() {
        this.userId = 0;
        this.email = "";
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";

        this.mindmapIdCounter = 0;
        this.mindmapIds = new ArrayList<>();
        this.favouriteMindmapIds = new ArrayList<>();
        this.deletedMindmapIds = new ArrayList<>();
        this.recentMindmapIds = new ArrayList<>();

        this.noteIdCounter = 0;
        this.noteIds = new ArrayList<>();
        this.favouriteNoteIds = new ArrayList<>();
        this.deletedNoteIds = new ArrayList<>();
        this.recentNoteIds = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMindmapIdCounter() {
        return mindmapIdCounter;
    }

    public void setMindmapIdCounter(int mindmapIdCounter) {
        this.mindmapIdCounter = mindmapIdCounter;
    }

    public List<Integer> getMindmapIds() {
        return mindmapIds;
    }

    public void setMindmapIds(List<Integer> mindmapIds) {
        this.mindmapIds = mindmapIds;
    }

    public List<Integer> getFavouriteMindmapIds() {
        return favouriteMindmapIds;
    }

    public void setFavouriteMindmapIds(List<Integer> favouriteMindmapIds) {
        this.favouriteMindmapIds = favouriteMindmapIds;
    }

    public List<Integer> getDeletedMindmapIds() {
        return deletedMindmapIds;
    }

    public void setDeletedMindmapIds(List<Integer> deletedMindmapIds) {
        this.deletedMindmapIds = deletedMindmapIds;
    }

    public List<Integer> getRecentMindmapIds() {
        return recentMindmapIds;
    }

    public void setRecentMindmapIds(List<Integer> recentMindmapIds) {
        this.recentMindmapIds = recentMindmapIds;
    }

    public int getNoteIdCounter() {
        return noteIdCounter;
    }

    public void setNoteIdCounter(int noteIdCounter) {
        this.noteIdCounter = noteIdCounter;
    }

    public List<Integer> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Integer> noteIds) {
        this.noteIds = noteIds;
    }

    public List<Integer> getFavouriteNoteIds() {
        return favouriteNoteIds;
    }

    public void setFavouriteNoteIds(List<Integer> favouriteNoteIds) {
        this.favouriteNoteIds = favouriteNoteIds;
    }

    public List<Integer> getDeletedNoteIds() {
        return deletedNoteIds;
    }

    public void setDeletedNoteIds(List<Integer> deletedNoteIds) {
        this.deletedNoteIds = deletedNoteIds;
    }

    public List<Integer> getRecentNoteIds() {
        return recentNoteIds;
    }

    public void setRecentNoteIds(List<Integer> recentNoteIds) {
        this.recentNoteIds = recentNoteIds;
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
}
