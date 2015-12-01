package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Guest;
import model.Host;
import model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RoomFrame extends JFrame{

    private static String EXIT_ROOM_URL = "http://localhost:8080/room/exit";
    private static String ROOM_URL = "http://localhost:8080/rooms/";
//    private static String EXIT_ROOM_URL = "http://52.192.150.155:8080/room/exit";
//    private static String ROOM_URL = "http://52.192.150.155:8080/rooms/";

    private int ROOM_ID;
    private static int USER_FIELDS_NUM = 3;
    private String userColumns[] = {"id", "username", "current_room"};
    private Object userRows[][] = null;
    private BufferedImage img = null;
    private Painter drawingArea = new Painter();
    private JPanel buttonContainer = new JPanel();
    private JButton redColor = new JButton("Red");
    private JButton blueColor = new JButton("Blue");
    private JButton blackColor = new JButton("Black");
    private JButton eraser = new JButton("Eraser");
    private JButton startButton = new JButton("Start");
    private JButton exitButton = new JButton("Exit");
    private JTextArea chatArea = new JTextArea();
    private JTextField chatField = new JTextField(30);
    private RoomBackground roomBackground;
    private JScrollPane jScrollPane;
    private Host host;
    private Guest guest;
    private String answer;
    private DefaultTableModel defaultUserTableModel = new DefaultTableModel(userRows, userColumns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };;

    public RoomFrame(int roomId) throws IOException {
        ROOM_ID = roomId;
        jScrollPane = new JScrollPane(chatArea);
        roomBackground  = new RoomBackground();
        roomBackground.setRoomFrame(this);
        roomBackground.setDefaultUserTableModel(defaultUserTableModel);
        roomBackground.setjScrollPane(jScrollPane);
        roomBackground.setChatArea(chatArea);

//      "makeRoom"을 통해 방에 입장한 사용자일 경우, Host 역할.
        if(User.getUser().getIsHost()) {
            User.getUser().setIsHost(true);
            host = new Host();
        }
//      "enterRoom"을 통해 방에 입장한 사용자일 경우, Guest 역할.
        else {
            User.getUser().setIsHost(false);
            guest = new Guest();
            guest.setRoomFrame(this);
        }

        try {
            img = ImageIO.read(new File("img/bg_login.jpg"));
        } catch (IOException e) {
            System.out.println("이미지 받아오기 실패!");
            System.exit(0);
        }

        getUsersInRoom(roomId);

        JTable userTable = new JTable();
        userTable.setModel(defaultUserTableModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1000, 600);
        layeredPane.setLayout(null);
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 1000, 600);

        // 색고르기
        buttonContainer.add(redColor);
        buttonContainer.add(blueColor);
        buttonContainer.add(blackColor);
        buttonContainer.add(eraser);

        redColor.setBounds(80, 70, 70, 30);
        blueColor.setBounds(150, 70, 70, 30);
        blackColor.setBounds(220, 70, 70, 30);
        eraser.setBounds(290, 70, 70, 30);
        startButton.setBounds(600, 70, 70, 30);
        exitButton.setBounds(700, 70, 70, 30);
        chatField.setBounds(550, 450, 320, 30);
        userScrollPane.setBounds(550, 127, 320, 110);
        drawingArea.setBounds(43, 130, 335, 361);
        jScrollPane = new JScrollPane(chatArea);
        jScrollPane.setBounds(550, 250, 320, 135);

        layeredPane.add(redColor);
        layeredPane.add(blueColor);
        layeredPane.add(blackColor);
        layeredPane.add(eraser);
        layeredPane.add(startButton);
        layeredPane.add(exitButton);
        layeredPane.add(jScrollPane);
        layeredPane.add(chatField);
        layeredPane.add(userScrollPane);
        layeredPane.add(drawingArea);

        pack();
        this.add(layeredPane);
        this.setTitle("방");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);

        redColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.setCurrentColor(Color.RED);
            }
        });
        blueColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.setCurrentColor(Color.blue);
            }
        });
        blackColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.setCurrentColor(Color.black);
            }
        });
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.setCurrentColor(Color.white);
            }
        });
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                answer = host.requestAnswer();
                System.out.println("문제는.." + answer);
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                try {
                    HttpResponse<JsonNode> jsonResponse = Unirest.post(EXIT_ROOM_URL)
                            .header("accept", "application/json")
                            .field("user_id", User.getUser().getId())
                            .field("room_id", ROOM_ID)
                            .field("is_host", User.getUser().getIsHost())
                            .asJson();
                    System.out.println("Before" + jsonResponse.getBody());
                    //  StatusCode가 204(no content) 즉, 서버에서 delete가 성공했을 경우에
                    if (jsonResponse.getStatus() == 204 || jsonResponse.getStatus() == 200) {
                        roomBackground.getChatSocket().close();
                        User.getUser().setCurrent_room(0);
                        dispose();
                        new LobbyFrame();
                    } else {
                        System.out.println("Fail exit from room");
                    }
                } catch (UnirestException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        chatField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chatField.getText().contains("*정답은*")) {
                    if (guest.compareAnswer("chatField"))
                        System.out.println("정답입니다.");
                    else
                        System.out.println("오답입니다.");
                } else {
                    String msg = User.getUser().getUsername() + ":" + chatField.getText() + "\n";
                    roomBackground.sendMsg(msg);
                    chatField.setText("");
                }
            }
        });
    }

    public void getUsersInRoom(int roomId) {
        try {
            JSONObject user;
            HttpResponse<JsonNode> jsonResponse = Unirest.get(ROOM_URL + roomId + "/users")
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()!=404) {
                Object jsonObj = JSONValue.parse(jsonResponse.getBody().toString());
                JSONArray userArray = (JSONArray) jsonObj;
                Object[] tempObject = new Object[USER_FIELDS_NUM];
                for (int i = 0; i < userArray.size(); ++i) {
                    user = (JSONObject) userArray.get(i);
                    for (int j = 0; j < USER_FIELDS_NUM; ++j) {
                        if (userColumns[j].equals("id") && (((Long)user.get(userColumns[j])).intValue() == User.getUser().getId()))
                            tempObject[j] = user.get(userColumns[j]) + "(나)";
                        else
                            tempObject[j] = user.get(userColumns[j]);
                    }
                    defaultUserTableModel.addRow(tempObject);
                }
            } else {
                System.out.println("사용자가 없습니다.");
            }

        }catch (UnirestException e ) {}
    }

    public Painter getDrawingArea() {
        return drawingArea;
    }

    public void appendMsg(String msg) {
        chatArea.append(msg);
    }

    public String getAnswer() {
        return answer;
    }

    class MyPanel extends JPanel{
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
