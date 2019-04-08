package com.rapidzz.yourmusicmap.datamodel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dev on 6/17/17.
 */

public class Term {

    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("value")
    @Expose
    private String value;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
