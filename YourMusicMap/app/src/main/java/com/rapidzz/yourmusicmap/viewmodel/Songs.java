package com.rapidzz.yourmusicmap.viewmodel;

import java.io.Serializable;

public class Songs implements Serializable {
    private int id;
    private String icon,title;

    public Songs(int id, String icon, String title){
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
