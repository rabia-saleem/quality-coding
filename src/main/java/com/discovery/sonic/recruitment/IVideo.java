package com.discovery.sonic.recruitment;

import com.discovery.sonic.contract.Video;

public interface IVideo {
    void setTitle(String title);

    String getTitle();

    String getId();

    void setId(String id);

    long getCounter();

    void setCounter(long l);

    Video toVideo();
}
