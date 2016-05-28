package com.keepingatimeline.kat;

import java.util.ArrayList;

/**
 * Created by Dana on 5/22/2016.
 */
public class Event {
    private String type, title, date, str1, str2, key;

    public Event(String type, String title, String date, String string1, String string2, String key) {
        this.type = type;
        this.title = title;
        this.date = date;
        this.str1 = string1;
        this.str2 = string2;
        this.key = key;
    }

    public Event() {
        this.type = "";
        this.title = "";
        this.date = "";
        this.str1 = "";
        this.str2 = "";
        this.key = "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getString1() {
        return str1;
    }

    public void setString1(String str1) {
        this.str1 = str1;
    }

    public String getString2() {
        return str2;
    }

    public void setString2(String str2) {
        this.str2 = str2;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }
}
