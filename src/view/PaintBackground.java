package view;

import model.User;
import org.apache.log4j.net.SocketServer;

import javax.swing.text.BadLocationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by haegyun on 11/26/15.
 */
public class PaintBackground {

    DataInputStream paintIn;
    DataOutputStream paintOut;

    private String paintInfo;

    private String socket_server = "127.0.0.1";
    private Socket paintSocket;
    private PaintReceiver paintReceiver;

    public void setPaint(Painter paint) {
        this.paint = paint;
    }

    private Painter paint;

    public PaintBackground() {
        try {
//            painter.setRoomBackground(this);
            paintSocket = new Socket(socket_server, 7777);
            paintReceiver = new PaintReceiver(paintSocket);
            paintReceiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPaintInfo(String msg2) {
        try {
            paintOut.writeUTF(msg2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class PaintReceiver extends Thread {
        public PaintReceiver(Socket socket) throws IOException {
            paintOut = new DataOutputStream(socket.getOutputStream());
            paintIn = new DataInputStream(socket.getInputStream());
            // 처음 Receiver 쓰레드가 동작할 때, 서버에서 cliemtnMap이라는 변수에 각 채팅하는 사용자의 username을 저장되도록 전송해준다.
//            paintOut.writeUTF(User.getUser().getUsername());
            paintOut.writeUTF(User.getUser().getUsername());
        }

        @Override
        public void run() {
            System.out.println("Paint 쓰레드가 돌아가는 중입니다.");
            try {
                while (paintIn != null) {
                    paintInfo = paintIn.readUTF();
                    String tempMsg = paintInfo;
                    System.out.println("잘받았다. PainterBackground다 나는." + paintInfo);
//                        paint.drawIntoBufferedImage();
////                    if {
//                    roomFrame.appendMsg(chatMsg);
//                    jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
//                    chatArea.setCaretPosition(chatArea.getLineEndOffset(chatArea.getLineCount() - 1));
//                    if (chatMsg.indexOf("님이 접속하셨습니다") >= 0 || chatMsg.indexOf("님이 나가셨습니다.") >= 0) {
//                        System.out.println("입장하거나 퇴장했다.");
//                        for (int i = defaultUserTableModel.getRowCount() - 1; i >= 0; i--) {
//                            defaultUserTableModel.removeRow(i);
//                        }
//                        roomFrame.getUsersInRoom(thisRoomId);
//                    }
////                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}