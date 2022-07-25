package com.discovery.sonic.contract;

public abstract class VideoService {


    protected boolean isProd = false;

    public abstract Video createVideo(String title);

    public abstract Video getVideo(String id);

    public abstract void incrementWatchCount(String videoId);

    public void setProd(boolean prod) {
        isProd = prod;
    }
}
