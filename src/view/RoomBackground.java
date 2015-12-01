package view;

import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomBackground {

    private DataInputStream chatIn;
    private DataOutputStream chatOut;
    private RoomFrame roomFrame;
    private JScrollPane jScrollPane;
    private JTextArea chatArea;

    private Socket chatSocket;


    private ChatReceiver chatReceiver;
    private int thisRoomId = User.getUser().getCurrent_room();
    private DefaultTableModel defaultUserTableModel;

    private static String socket_server = "127.0.0.1";
//    private static String socket_server = "52.192.150.155";

    public Socket getChatSocket() {
        return chatSocket;
    }
    public void setRoomFrame(RoomFrame roomFrame) {
        this.roomFrame = roomFrame;
    }

    public void setDefaultUserTableModel(DefaultTableModel defaultUserTableModel) {
        this.defaultUserTableModel = defaultUserTableModel;
    }

    public void setjScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
    }

    public void setChatArea(JTextArea chatArea) {
        this.chatArea = chatArea;
    }


    public RoomBackground() throws IOException {
//        Painter painter = new Painter();
//        painter.setRoomBackground(this);
        chatSocket = new Socket(socket_server, 7777);
        chatReceiver = new ChatReceiver(chatSocket);
        chatReceiver.start();
    }

    public void sendMsg(String msg2) {
        System.out.println("보내질 메세지는" + msg2);
        try {
            chatOut.writeUTF(msg2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ChatReceiver extends Thread {

        public ChatReceiver(Socket socket) throws IOException {
            chatOut = new DataOutputStream(socket.getOutputStream());
            chatIn = new DataInputStream(socket.getInputStream());
            // 처음 Receiver 쓰레드가 동작할 때, 서버에서 cliemtnMap이라는 변수에 각 채팅하는 사용자의 username을 저장되도록 전송해준다.
            chatOut.writeUTF(User.getUser().getUsername());
        }

        @Override
        public void run(){
            System.out.println("쓰레드가 돌아가는 중입니다.");
            try {
                while(chatIn != null) {
                    String chatMsg = chatIn.readUTF();
                    System.out.println("들어오는 메세지 ::: " + chatMsg);
                    if (chatMsg.contains("/xyp/")) {
                        System.out.println("잘받았다. 나는 RoomBackground다." + chatMsg);
                        String[] splitedChatMsg = chatMsg.split("/xyp/");
                        String[] splitedPaintInfos = splitedChatMsg[1].split(",");
                        Graphics2D g = (Graphics2D) roomFrame.getDrawingArea().getGraphics();
                        g.drawLine(
                                Integer.parseInt(splitedPaintInfos[0]),
                                Integer.parseInt(splitedPaintInfos[1]),
                                Integer.parseInt(splitedPaintInfos[2]),
                                Integer.parseInt(splitedPaintInfos[3])
                        );
                    }
                    else {
                        roomFrame.appendMsg(chatMsg);
                        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                        chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
                        if (chatMsg.contains("접속하셨습니다") || chatMsg.contains("나가셨습니다.")) {
                            System.out.println("입장하거나 퇴장했다.");
                            for (int i = defaultUserTableModel.getRowCount() - 1; i >= 0; i--) {
                                defaultUserTableModel.removeRow(i);
                            }
                            roomFrame.getUsersInRoom(thisRoomId);
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

    }

}

