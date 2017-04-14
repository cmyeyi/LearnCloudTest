package com.aile.www.basesdk.entity;

public class RelationTo {
    public static final String OP_RELATEDTO = "$relatedTo";

    private String key;
    private Pointer object;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Pointer getObject() {
        return object;
    }

    public void setObject(Pointer object) {
        this.object = object;
    }
}
