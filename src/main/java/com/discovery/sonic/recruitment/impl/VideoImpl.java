package com.discovery.sonic.recruitment.impl;

import com.discovery.sonic.contract.Video;
import com.discovery.sonic.recruitment.IVideo;

import java.util.UUID;

public class VideoImpl implements IVideo {

    private String title;
    private String id;
    private long counter;

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        if (null == title) {
            return null;
        }
        return title;
    }

    @Override
    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getCounter() {
        return counter;
    }

    @Override
    public void setCounter(long counter) {
        this.counter = counter;
    }

    @Override
    public Video toVideo() {
        return new ContractVideo();
    }

    private class ContractVideo extends Video {
        public ContractVideo() {
            this.counter = VideoImpl.this.counter;
            this.id = VideoImpl.this.id;
            this.title = VideoImpl.this.title;
        }
    }

}
