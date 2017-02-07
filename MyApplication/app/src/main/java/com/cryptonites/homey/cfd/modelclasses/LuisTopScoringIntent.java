package com.cryptonites.homey.cfd.modelclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 7/2/17.
 */

public class LuisTopScoringIntent {
    @SerializedName("intent")
    @Expose
    private String intent;
    @SerializedName("score")
    @Expose
    private Double score;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
