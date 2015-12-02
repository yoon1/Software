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
import java.util.Arrays;

public class RoomBackground {

    private static String socket_server = "127.0.0.1";
//    private static String socket_server = "52.192.150.155";

    private static Socket chatSocket;
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private ChatReceiver chatReceiver;

    private JScrollPane jScrollPane;

    private static RoomFrame roomFrame;

    private JTextArea chatArea;

    private Color currentColor;
    private Color eraseColor = new Color(255,255,255);

//    private ChatReceiver chatReceiver;
    private int thisRoomId = User.getUser().getCurrent_room();
    private DefaultTableModel defaultUserTableModel;



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
        System.out.println("RoomBackground()");
        chatSocket = new Socket(socket_server, 7777);

        dos = new DataOutputStream(chatSocket.getOutputStream());
        dis = new DataInputStream(chatSocket.getInputStream());

        chatReceiver = new ChatReceiver();
        chatReceiver.start();

        // 처음 Receiver 쓰레드가 동작할 때, 서버에서 cliemtnMap이라는 변수에 각 채팅하는 사용자의 username을 저장되도록 전송해준다.
        dos.writeUTF(User.getUser().getUsername());
        dos.flush();
    }

    public static DataOutputStream getDos() {
        return dos;
    }

    class ChatReceiver extends Thread {

        @Override
        public void run(){
            try {
                while(dis != null) {
                    while (dis != null) {
                        String chatMsg = dis.readUTF();
                        System.out.println("들어오는 메세지 ::: " + chatMsg);
                        if (chatMsg.contains("/paintinfos/") && !(User.getUser().getIsHost())) {
                            String[] splitedChatMsg = chatMsg.split("/");
                            System.out.println(Arrays.toString(splitedChatMsg));
                            String[] splitedPaintInfos = splitedChatMsg[2].split(",");
                            String[] splitedColorInfos = splitedChatMsg[3].split(",");

                            Graphics2D g = (Graphics2D) roomFrame.getDrawingArea().getGraphics();

                            currentColor = new Color(Integer.parseInt(splitedColorInfos[0]), Integer.parseInt(splitedColorInfos[1]), Integer.parseInt(splitedColorInfos[2]));

                            g.setColor(currentColor);
                            if (g.getColor().toString().equals(eraseColor.toString())) {
                                g.setStroke(new BasicStroke(30));
                            } else {
                                g.setStroke(new BasicStroke(1));
                            }

                            g.drawLine(Integer.parseInt(splitedPaintInfos[0]), Integer.parseInt(splitedPaintInfos[1]), Integer.parseInt(splitedPaintInfos[2]), Integer.parseInt(splitedPaintInfos[3]));
                        } else if (!(chatMsg.contains("/paintinfos/"))) {
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
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

    }

}

