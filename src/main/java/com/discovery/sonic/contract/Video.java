package com.discovery.sonic.contract;

public abstract class Video {

    protected String id, title;
    protected long counter;

    public String getTitle() {
        return title;
    }

    public long getCounter() {
        return counter;
    }

    public String getId() {
        return id;
    }
}
