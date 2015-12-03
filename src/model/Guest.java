package model;

import view.RoomBackground;
import view.RoomFrame;

import java.io.IOException;

public class Guest extends User{

    private RoomFrame roomFrame;

    public void setRoomFrame(RoomFrame roomFrame) {
        this.roomFrame = roomFrame;
    }

//  사실상 sendAnswer. 설계서에 compareAnswer라고 적어서 그냥 compareAnswer이라고 작명.
    public void sendAnswer(String expectAsw) {
        try {
            System.out.println("내가 보낼 답은  " + expectAsw);
            RoomBackground.getDos().writeUTF("/answerfromguest/ " + User.getUser().getUsername() + "/" + expectAsw);
            RoomBackground.getDos().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
