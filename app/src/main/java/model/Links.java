package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("previousepisode")
    @Expose
    private PreviousEpisode previousEpisode;
    @SerializedName("nextepisode")
    @Expose
    private NextEpisode nextEpisode;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public PreviousEpisode getPreviousEpisode() {
        return previousEpisode;
    }

    public void setPreviousEpisode(PreviousEpisode previousepisode) {
        this.previousEpisode = previousepisode;
    }

    public NextEpisode getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(NextEpisode nextepisode) {
        this.nextEpisode = nextEpisode;
    }
}
