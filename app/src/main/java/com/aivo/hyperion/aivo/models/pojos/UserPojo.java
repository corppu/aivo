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

    private int objectIdCounter;

    private List<Integer> mindmapIds;
    private List<Integer> favouriteMindmapIds;
    private List<Integer> recentMindmapIds;

    private int defaultThemeId;
    private List<Integer> themeIds;

    public UserPojo() {
        this.userId = -1;
        this.email = "";
        this.createdAt = "1970-01-01T00:00:00.000Z";
        this.updateAt = "1970-01-01T00:00:00.000Z";

        this.objectIdCounter = 0;

        this.mindmapIds = new ArrayList<>();
        this.favouriteMindmapIds = new ArrayList<>();
        this.recentMindmapIds = new ArrayList<>();

        this.defaultThemeId = -1;
        this.themeIds = new ArrayList<>();
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

    public int getObjectIdCounter() {
        return objectIdCounter;
    }

    public void setObjectIdCounter(int objectIdCounter) {
        this.objectIdCounter = objectIdCounter;
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

    public List<Integer> getRecentMindmapIds() {
        return recentMindmapIds;
    }

    public void setRecentMindmapIds(List<Integer> recentMindmapIds) {
        this.recentMindmapIds = recentMindmapIds;
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

    public int getDefaultThemeId() {
        return defaultThemeId;
    }

    public void setDefaultThemeId(int defaultThemeId) {
        this.defaultThemeId = defaultThemeId;
    }

    public List<Integer> getThemeIds() {
        return themeIds;
    }

    public void setThemeIds(List<Integer> themeIds) {
        this.themeIds = themeIds;
    }
}
