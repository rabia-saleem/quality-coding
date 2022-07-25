package com.discovery.sonic.recruitment.util;

import com.discovery.sonic.recruitment.IVideo;
import com.discovery.sonic.recruitment.VideoInfoDTO;
import com.discovery.sonic.recruitment.db.Database;
import com.discovery.sonic.recruitment.db.Database.DatabaseException;
import com.discovery.sonic.recruitment.impl.VideoImpl;

import java.util.HashMap;
import java.util.Optional;

public class VideoUtil {
    public static IVideo createVideo(VideoInfoDTO dto, Object dataStore, DataStoreType dataStoreType) {
        Optional<IVideo> result = null;
        try {
            result = Optional.ofNullable(insertiVideo(dto, dataStore, dataStoreType));
        } catch (Database.DatabaseException e) {
            return null;
        }
        if (result != null && result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public static IVideo getVideo(String id, Object dataStore, DataStoreType dataStoreType) {
        Optional<IVideo> result = null;
        try {
            result = getResult(null, id, dataStore, dataStoreType, Operation.READ);
        } catch (Database.DatabaseException e) {
            return null;
        }
        if (result != null && result.isPresent()) {
            return result.get();
        }
        return null;
    }


    public static void incrementWatchCount(String videoId, Object dataStore, DataStoreType dataStoreType) {
        try {
            getResult(null, videoId, dataStore, dataStoreType, Operation.INCREMENT);
        } catch (Database.DatabaseException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Gets the result
     * @param dto the dto
     * @param id the id
     * @param dataStore the dataStore
     * @return
     */
    public static Optional<IVideo> getResult(VideoInfoDTO dto, String id, Object dataStore, DataStoreType dataStoreType, final Operation operation) throws Database.DatabaseException {
        IVideo data = null;

        if (operation == Operation.READ) {
            data = readVideo(id, dataStore, dataStoreType, data);
        }
        if (operation == Operation.INCREMENT) {
            if (dataStoreType == DataStoreType.DATABASE) {
                var store = (Database) dataStore;
                store.incrementCounter(id);

                return null;
            } else if (dataStoreType == DataStoreType.IN_MEMORY) {
                if (dataStore != null) {
                    var store = (HashMap<String, IVideo>) dataStore;
                    data = store.get(id);
                    data.setCounter(data.getCounter() + 1);
                }
            }
        }
        return Optional.ofNullable(data);
    }

    private static IVideo readVideo(String id, Object dataStore, DataStoreType dataStoreType,
            IVideo data) throws DatabaseException {
        if (dataStoreType == DataStoreType.DATABASE) {
            var store = (Database) dataStore;
            data =store.readData(id);
        } else if (dataStoreType == DataStoreType.IN_MEMORY) {
            if (id == null) {
                data = null;
            }
            if (dataStore != null) {
                var store = (HashMap<String, IVideo>) dataStore;
                data = store.get(id);
            }
        }
        return data;
    }

    private static IVideo insertiVideo(VideoInfoDTO dto, Object dataStore, DataStoreType dataStoreType) throws DatabaseException {
        IVideo data = null;
        if (dataStoreType == DataStoreType.DATABASE) {
           data = insertInDatabase(dto, dataStore);
        } else if (dataStoreType == DataStoreType.IN_MEMORY) {
            data = insertInMemory(dto, dataStore);
        }
        return data;
    }

    private static IVideo insertInMemory(VideoInfoDTO dto, Object dataStore) {
        IVideo data;
        if (dto == null) {
            data = null;
        } else {
            data = populate(new VideoImpl(), dto);
            if (dataStore != null) {
                var store = (HashMap<String, IVideo>) dataStore;
                store.put(data.getId(), data);
            }
        }
        return data;
    }

    private static IVideo insertInDatabase(VideoInfoDTO dto, Object dataStore)
            throws DatabaseException {
        IVideo data;
        var store = (Database) dataStore;
        if (dto == null) {
            data = null;
        } else {
            data = populate(new VideoImpl(), dto);
            if (dataStore != null) {
                try {
                    store.saveData(data);
                } catch (DatabaseException e) {
                    if (e.getErrorCode() == 16032) {
                        var cutTitle = data.getTitle().substring(0, 55);
                        data.setTitle(cutTitle);
                        store.saveData(data);
                    } else {
                        throw e;
                    }
                }
            }
        }
        return data;
    }

    private static IVideo populate(IVideo out, VideoInfoDTO in) {
        out.setTitle(in.getTitle());
        return out;
    }


    public enum DataStoreType {
        IN_MEMORY, DATABASE
    }

    public enum Operation {
        CREATE, READ, INCREMENT
    }
}
