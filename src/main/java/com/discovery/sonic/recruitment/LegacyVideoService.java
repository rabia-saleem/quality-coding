package com.discovery.sonic.recruitment;

import com.discovery.sonic.contract.Video;
import com.discovery.sonic.contract.VideoService;
import com.discovery.sonic.recruitment.db.Database;
import com.discovery.sonic.recruitment.util.VideoUtil;

import java.util.HashMap;

public class LegacyVideoService extends VideoService {

    private Database database = null;

    public static final HashMap<String, IVideo> DATA_STORE = new HashMap<>();

    @Override
    public Video createVideo(String title) {
        var dto = new VideoInfoDTO(title);
        var video = VideoUtil.createVideo(dto, getDataStore(), getDataStoreType());
        return video == null ? null : video.toVideo();
    }

    @Override
    public Video getVideo(String id) {
        var video = VideoUtil.getVideo(id, getDataStore(), getDataStoreType());
        return video == null ? null : video.toVideo();
    }

    @Override
    public void incrementWatchCount(String videoId) {
        VideoUtil.incrementWatchCount(videoId, getDataStore(), getDataStoreType());
    }

    private Object getDataStore() {
        if (isProd) {
            if (database == null) {
                database = Database.standard();
            }
            return database;
        }
        return DATA_STORE;
    }

    private VideoUtil.DataStoreType getDataStoreType() {
        return isProd ? VideoUtil.DataStoreType.DATABASE : VideoUtil.DataStoreType.IN_MEMORY;
    }
}
