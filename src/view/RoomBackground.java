package view;

import Service.TimerService;
import model.Host;
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

//    private static String socket_server = "127.0.0.1";
    private static String socket_server = "52.192.150.155";

    private static Socket chatSocket;
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private ChatReceiver chatReceiver;

    private JScrollPane jScrollPane;

    private static String currentAnswer;

    private RoomFrame roomFrame;

    private Host host;

    private JTextArea chatArea;

    private Color currentColor;
    private Color eraseColor = new Color(255,255,255);

//    private ChatReceiver chatReceiver;
    private int thisRoomId = User.getUser().getCurrent_room();

    private DefaultTableModel defaultUserTableModel;

    public Socket getChatSocket() {
        return chatSocket;
    }

    public void setRoomFrame(RoomFrame rf) {
        roomFrame = rf;
    }

    public void setHost(Host ht) {
        this.host = ht;
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
        System.out.println("socket_server"+ socket_server);
        chatSocket = new Socket(socket_server, 7777);

        dos = new DataOutputStream(chatSocket.getOutputStream());
        dis = new DataInputStream(chatSocket.getInputStream());

        // 처음 Receiver 쓰레드가 동작할 때, 서버에서 cliemtnMap이라는 변수에 각 채팅하는 사용자의 username을 저장되도록 전송해준다.
        chatReceiver = new ChatReceiver();
        chatReceiver.start();

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
                System.out.println("쓰레드를 시작합니다." + dis);
                while(dis != null) {
                    String chatMsg = dis.readUTF();
                    if(chatMsg.contains("/gamestart/")){
                        System.out.println("게임이 시작됩니다. 이것은 전체 메세지입니다.");
                        roomFrame.appendMsg("게임이 시작됩니다.\n");
                        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                        chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
                        roomFrame.setPlayingState(true);
                        if(User.getUser().getIsHost()) {
                            System.out.println("host.getCurrentAnswer()");
                            currentAnswer = host.requestAnswer();
                            roomFrame.changeAnswerBar(currentAnswer);
                        }
//                      정해진 타이머가 다 돌아가고 게임이 종료되는 경우.
                    }
                    else if(chatMsg.contains("/gameend/")) {
                        System.out.println("게임이 종료됩니다. 이것도 전체 메세지입니다.");
                        roomFrame.appendMsg("게임이 종료됩니다.\n");
                        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                        chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
                        roomFrame.setPlayingState(false);

                        roomFrame.getDrawingArea().clearDrawings();
                        roomFrame.getDrawingArea().points.clear();
                        roomFrame.getDrawingArea().repaint();
                    }
                    else if (chatMsg.contains("/timerinfo/")) {
                        String[] splitedTimerInfo = chatMsg.split("/");
                        String left_time = splitedTimerInfo[2];
                        roomFrame.changeStatusBar("" + left_time);
                    } else if (roomFrame.getPlayingState() && chatMsg.contains("/answerfromguest/")) {
//                        방장이 정답을 비교하는 로직. 들어가면됨.
                        String[] splitedChatMsg = chatMsg.split("/");
                        String username = splitedChatMsg[2].replaceAll("\\s+", "") ;
                        String answerFromGuest = splitedChatMsg[3].replaceAll("\\s+", "") ;
//                        System.out.println("username, answer ! ! ! ! ! ! " + username + answerFromGuest);
                        if(User.getUser().getIsHost()) {
                            if(currentAnswer.equals(answerFromGuest)) {
                                host.correctAnswerSignal(username, currentAnswer);
                            }
                        }
                        roomFrame.appendMsg(username+":"+answerFromGuest+"\n");
                        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                        chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
                    } else if (roomFrame.getPlayingState() && chatMsg.contains("/correctanswer/")) {
                        String[] splitedChatMsg = chatMsg.split("/");
                        String username = splitedChatMsg[2].replaceAll("\\s+", "") ;
                        String answer = splitedChatMsg[3].replaceAll("\\s+", "") ;
//                        roomFrame.changeStatusBar("정답자:"+username+"\n"+"정답:"+answer);
                        roomFrame.changeStatusBar("" + 0);
                        roomFrame.appendMsg("정답자:" + username + "@정답은:" + answer + "\n");
                        roomFrame.appendMsg("게임이 종료됩니다.\n");
                        jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
                        chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
                        roomFrame.setPlayingState(false);

                        roomFrame.getDrawingArea().clearDrawings();
                        roomFrame.getDrawingArea().points.clear();
                        roomFrame.getDrawingArea().repaint();
                    }
                    else if (chatMsg.contains("/paintinfos/") && !(User.getUser().getIsHost())) {
                        System.out.println("paintinfos" + chatMsg);

                        String[] splitedChatMsg = chatMsg.split("/");
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

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

    }

}

