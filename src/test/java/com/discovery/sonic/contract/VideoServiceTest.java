package com.discovery.sonic.contract;

import com.discovery.sonic.recruitment.LegacyVideoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.stream.IntStream;

class VideoServiceTest {

    private LegacyVideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new LegacyVideoService();
    }

    @Test
    void create_video_returns_the_video() {
        // Given a video
        String title = "Big Bang Theory - Episode " + randomUUID();
        var video = videoService.createVideo(title);

        //verify result
        Assertions.assertEquals(title, video.getTitle());
    }

    @Test
    void get_video_returns_title_field() {
        // Given a video
        String title = "Big Bang Theory - Episode " + randomUUID();
        var video = videoService.createVideo(title);

        // When getting the video
        var result = videoService.getVideo(video.getId());

        //verify result
        Assertions.assertEquals(title, result.getTitle());
    }

    @Test
    void increment_count_increments_count() {

        // Given a video
        String title = "Big Bang Theory - Episode " + randomUUID();
        var video = videoService.createVideo(title);

        // When incrementing count
        var numberOfTimesWeWantToWatch = 3;

        IntStream.range(0, numberOfTimesWeWantToWatch).forEach(i -> videoService.incrementWatchCount(video.getId()));


        // Get the video to verify change
        var result = videoService.getVideo(video.getId());

        //verify result
        Assertions.assertEquals(numberOfTimesWeWantToWatch, result.getCounter());
    }

    private String randomUUID() {
        return UUID.randomUUID().toString();
    }


}