package org.example.services;

import org.example.models.Room;
import org.example.untils.CR;
import org.example.untils.GetValue;
import org.example.untils.SerializationUtil;

import java.util.ArrayList;
import java.util.List;

public class RoomService implements CR<Room> {
    private List<Room> roomList;
    private String fileName = "rooms.txt";

    public RoomService() {
        roomList = (List<Room>) SerializationUtil.deserialize(fileName);
        if (roomList == null) {
            roomList = new ArrayList<>();
        }
    }

    private static RoomService INSTANCE_ROOM_SERVICE;

    public static RoomService getInstance() {
        if (INSTANCE_ROOM_SERVICE == null) {
            INSTANCE_ROOM_SERVICE = new RoomService();
        }
        return INSTANCE_ROOM_SERVICE;
    }

    @Override
    public void save() {
        SerializationUtil.serialize(roomList, fileName);
    }


    @Override
    public void update(Room room) {
        if (roomList != null) {
            for (Room item : roomList) {
                if (item.getId() == room.getId()) {
                    item.setStatus("ĐANG ĐẶT");
                    break;
                }
            }
            save();
        }
    }

    public void update(Room room, String str) {
        List<Room> rooms = RoomService.getInstance().readFile();
        if (rooms != null) {
            for (Room item : rooms) {
                if (item.getId() == room.getId()) {
                    item.setStatus(str);
                    break;
                }
            }
            SerializationUtil.serialize(rooms, fileName);;
        }
    }

    @Override
    public List<Room> readFile() {
        return (List<Room>) SerializationUtil.deserialize(fileName);
    }


    @Override
    public Room find(int idT) {
        return null;
    }

    public Room find(int idT, String str, Room r) {
        if (roomList.stream().anyMatch(room -> room.getId() == idT
                && room.getStatus().equals(str)
                && room.getId() != r.getId())) {
            return roomList.stream()
                    .filter(room -> room.getId() == idT)
                    .findFirst()
                    .orElseThrow();
        }
        return find(GetValue.getInt("Nhập lại ID phòng: "), str,r);
    }
    public Room find(int idT, String str) {
        if (roomList.stream().anyMatch(room -> room.getId() == idT
                && room.getStatus().equals(str))) {
            return roomList.stream()
                    .filter(room -> room.getId() == idT)
                    .findFirst()
                    .orElseThrow();
        }
        return find(GetValue.getInt("Nhập lại ID phòng: "), str);
    }
}
