package com.aivo.hyperion.aivo.models.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corpp on 20.10.2015.
 */
public class User {

    private int userId;
    private String email;
    private String createdAt;
    private String updateAt;

    private int mindMapIdCounter;
    private List<Integer> mindMapIds;
    private List<Integer> favouriteMindMapIds;
    private List<Integer> deletedMindMapIds;
    private List<Integer> recentMindMapIds;

    private int noteIdCounter;
    private List<Integer> noteIds;
    private List<Integer> favouriteNoteIds;
    private List<Integer> deletedNoteIds;
    private List<Integer> recentNoteIds;

    public User() {
        this.userId = 0;
        this.email = "";
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";

        this.mindMapIdCounter = 0;
        this.mindMapIds = new ArrayList<>();
        this.favouriteMindMapIds = new ArrayList<>();
        this.deletedMindMapIds = new ArrayList<>();
        this.recentMindMapIds = new ArrayList<>();

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

    public int getMindMapIdCounter() {
        return mindMapIdCounter;
    }

    public void setMindMapIdCounter(int mindMapIdCounter) {
        this.mindMapIdCounter = mindMapIdCounter;
    }

    public List<Integer> getMindMapIds() {
        return mindMapIds;
    }

    public void setMindMapIds(List<Integer> mindMapIds) {
        this.mindMapIds = mindMapIds;
    }

    public List<Integer> getFavouriteMindMapIds() {
        return favouriteMindMapIds;
    }

    public void setFavouriteMindMapIds(List<Integer> favouriteMindMapIds) {
        this.favouriteMindMapIds = favouriteMindMapIds;
    }

    public List<Integer> getDeletedMindMapIds() {
        return deletedMindMapIds;
    }

    public void setDeletedMindMapIds(List<Integer> deletedMindMapIds) {
        this.deletedMindMapIds = deletedMindMapIds;
    }

    public List<Integer> getRecentMindMapIds() {
        return recentMindMapIds;
    }

    public void setRecentMindMapIds(List<Integer> recentMindMapIds) {
        this.recentMindMapIds = recentMindMapIds;
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
