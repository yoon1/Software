package Service;

import model.Host;
import org.omg.SendingContext.RunTime;
import view.RoomBackground;
import view.RoomFrame;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService {
    private static Timer timer;
    private CountTimer countTimer;

    public void setHost(Host ht) {
        host = ht;
    }

    private static Host host;

    private static RoomFrame roomFrame;

    public void setRoomFrame(RoomFrame rf) {
        roomFrame = rf;
    }

    private static int left_sec;

    public TimerService(int initSec) {
        left_sec = initSec;
        countTimer = new CountTimer();

        timer = new Timer();
        timer.scheduleAtFixedRate(countTimer, 0, 1000);
    }

    public static class CountTimer extends TimerTask {

        public void run() {

            if (left_sec >= 0 && roomFrame.getPlayingState()) {
                try {
                    System.out.println("보내질 left_sec" + left_sec);
                    RoomBackground.getDos().writeUTF("/timerinfo/" + left_sec);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                left_sec--;
            }
//          누군가 정답을 맞춰서 game이 종료되었다면?
            else if(!roomFrame.getPlayingState()){
                System.out.println("타이머 종료");
                cancel();
            }
//          정답을 맞추는 사람없이 그냥 타이머가 종료되는 경우.
            else {
                System.out.println("타이머 종료");
                host.sendGameEndSignal();
                cancel();
            }
        }

        public void stop() {
            System.out.println("CountTimer를 종료합니다.");
            this.cancel();
        }

    }
}
