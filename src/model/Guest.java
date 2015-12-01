package model;

import view.RoomFrame;

public class Guest extends User{

    private RoomFrame roomFrame;

    public void setRoomFrame(RoomFrame roomFrame) {
        this.roomFrame = roomFrame;
    }

    public Boolean compareAnswer(String expectAnswer) {
        return roomFrame.getAnswer().equals(expectAnswer);
    }
}
