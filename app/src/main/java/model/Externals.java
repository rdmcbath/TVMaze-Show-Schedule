package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Externals {
    @SerializedName("tvrage")
    @Expose
    private Object tvrage;
    @SerializedName("thetvdb")
    @Expose
    private Integer thetvdb;
    @SerializedName("imdb")
    @Expose
    private String imdb;

    public Object getTvrage() {
        return tvrage;
    }

    public void setTvrage(Object tvrage) {
        this.tvrage = tvrage;
    }

    public Integer getThetvdb() {
        return thetvdb;
    }

    public void setThetvdb(Integer thetvdb) {
        this.thetvdb = thetvdb;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}
