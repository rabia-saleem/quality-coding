package com.discovery.sonic.recruitment.db;

import com.discovery.sonic.recruitment.IVideo;
import com.discovery.sonic.contract.db.table.VideoTable;
import com.discovery.sonic.recruitment.impl.VideoImpl;

public interface Database {

    static Database standard() {
        return new VideoTableWrapper();
    }

    void incrementCounter(String id);

    abstract class DatabaseException extends Exception {
        public int getErrorCode() {
            return -1;
        }
    }

    void saveData(IVideo video) throws DatabaseException;

    IVideo readData(String id) throws DatabaseException;

    class VideoTableWrapper implements Database {

        public static final int NOT_NULL = 22000;
        public static final int TOO_WIDE = 16032;
        public static final int NOT_FOUND = 102;
        private final VideoTable videoTable = new VideoTable();

        @Override
        public void incrementCounter(String id) {
            videoTable.incrementCounterInRow(id);
        }

        @Override
        public void saveData(IVideo video) throws DatabaseException {
            try {
                videoTable.writeRow(new Object[]{video.getId(), video.getTitle()});
            } catch (VideoTable.NotNullConstraintException e) {
                throw new Database.CodedException(NOT_NULL);
            } catch (VideoTable.ColumnWidthExceededException e) {
                throw new Database.CodedException(TOO_WIDE);
            }
        }

        @Override
        public IVideo readData(String id) throws DatabaseException {
            var row = videoTable.readRow(id);
            if (null == row) {
                throw new Database.CodedException(NOT_FOUND);
            }
            var video = new VideoImpl();
            video.setId(id);
            video.setTitle(row[1].toString());
            video.setCounter(((long) row[2]));
            return video;
        }


    }

    class CodedException extends DatabaseException {
        private final int errorCode;

        public CodedException(int errorCode) {
            this.errorCode = errorCode;
        }


        @Override
        public int getErrorCode() {
            return errorCode;
        }
    }
}
