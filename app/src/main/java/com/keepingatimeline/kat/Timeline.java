package com.keepingatimeline.kat;

/**
 * Created by Dana on 5/30/2016.
 */
public class Timeline {
    private String title, members, id, lastmodified;

    public Timeline() {
        this.title = "";
        this.members = "";
        this.id = "";
        this.lastmodified = "";
    }

    public Timeline(String title, String members, String id, String lastmodified) {
        this.title = title;
        this.members = members;
        this.id = id;
        this.lastmodified = lastmodified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }
}
