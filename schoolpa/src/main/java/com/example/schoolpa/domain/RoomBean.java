package com.example.schoolpa.domain;

/**
 * Created by admin on 2016/3/31.
 */
public class RoomBean {
    private String roomName;
    private String roomId;

    @Override
    public String toString() {
        return "RoomBean{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                '}';
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomBean(String roomName, String roomId) {

        this.roomName = roomName;
        this.roomId = roomId;
    }
}
