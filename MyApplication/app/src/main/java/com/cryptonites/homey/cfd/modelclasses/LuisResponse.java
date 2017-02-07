package com.cryptonites.homey.cfd.modelclasses;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 7/2/17.
 */

public class LuisResponse {

    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("topScoringIntent")
    @Expose
    private LuisTopScoringIntent topScoringIntent;
    @SerializedName("intents")
    @Expose
    private List<Intent> intents = null;
    @SerializedName("entities")
    @Expose
    private List<Entity> entities = null;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public LuisTopScoringIntent getTopScoringIntent() {
        return topScoringIntent;
    }

    public void setTopScoringIntent(LuisTopScoringIntent topScoringIntent) {
        this.topScoringIntent = topScoringIntent;
    }

    public List<Intent> getIntents() {
        return intents;
    }

    public void setIntents(List<Intent> intents) {
        this.intents = intents;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;

    }

}