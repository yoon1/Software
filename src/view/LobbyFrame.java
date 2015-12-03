package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import model.Host;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.User;

public class LobbyFrame extends JFrame {
//    private static String GET_USERS_URL = "http://localhost:8080/users";
//    private static String GET_USER_URL = "http://localhost:8080/users/";
//    private static String GET_ROOMS_URL = "http://localhost:8080/rooms";
//    private static String ENTER_ROOM_URL = "http://localhost:8080/room/enter";
//    private static String MAKE_ROOM_URL = "http://localhost:8080/room/make";
    private static String GET_USERS_URL = "http://52.192.150.155:8080/users";
    private static String GET_USER_URL = "http://52.192.150.155:8080/users/";
    private static String GET_ROOMS_URL = "http://52.192.150.155:8080/rooms";
    private static String ENTER_ROOM_URL = "http://52.192.150.155:8080/room/enter";
    private static String MAKE_ROOM_URL = "http://52.192.150.155:8080/room/make";

    private String roomColumns[] = {"id", "title", "current_num", "limit_num", "is_playing"};
    private Object roomRows[][] = null;

    private String userColumns[] = {"id", "username", "current_room"};
    private Object userRows[][] = null;

    private static int ROOM_FIELDS_NUM = 5;
    private static int USER_FIELDS_NUM = 3;

    private static int selectedRoomId;
    private static int makedRoomId;

    private JTable roomTable;
    private JTable userTable;

    private DefaultTableModel defaultRoomTableModel = new DefaultTableModel(roomRows, roomColumns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private DefaultTableModel defaultUserTableModel = new DefaultTableModel(userRows, userColumns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };


    private JFrame jFrame = new JFrame("로비");

    public LobbyFrame(){
//      로비가 처음 로딩될 때, 방 목록과 사용자 목록을 초기화 해준다.
        getUser(User.getUser().getId());
        getRoomList();
        getUserList();

        userTable = new JTable();
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.setModel(defaultUserTableModel);

        roomTable = new JTable();
        roomTable.getTableHeader().setReorderingAllowed(false);
        roomTable.setModel(defaultRoomTableModel);
        //      방 목록 중 하나를 선택했을 때 발생하는 이벤트.
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                try {
                    selectedRoomId = ((Long) roomTable.getValueAt(roomTable.getSelectedRow(), 0)).intValue();
                    HttpResponse<JsonNode> jsonResponse = Unirest.post(ENTER_ROOM_URL)
                            .header("accept", "application/json")
                            .field("user_id", User.getUser().getId())
                            .field("room_id", selectedRoomId)
                            .asJson();
                    if (jsonResponse.getStatus() == 200) {
                        System.out.println("Enter!!");
                        User.getUser().setIsHost(false);
                        User.getUser().setCurrent_room(selectedRoomId);
                        jFrame.dispose();
                        new RoomFrame(selectedRoomId);
                    } else {
                        System.out.println("Failed enter");
                    }
                } catch (UnirestException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//      jScrollPane에 jTable을 포함시킴
        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        JScrollPane userScrollPane = new JScrollPane(userTable);

//        roomScrollPane.getViewport().setBackground(Color.BLUE);
        roomScrollPane.setBounds(0, 0, 600, 400);

//        userScrollPane.getViewport().setBackground(Color.GREEN);
        userScrollPane.setBounds(700, 0, 300, 600);

//      루트 패널에 tablePanel을 포함시킴
        JPanel rootPanel = new JPanel();
        rootPanel.add(roomScrollPane);
        rootPanel.add(userScrollPane);
//        rootPanel.setBackground(Color.RED);
        rootPanel.setLayout(null);

        JButton makeRoomBtn = new JButton();
        JButton refreshBtn = new JButton();

        makeRoomBtn.setBounds(50,450,200,100);
        makeRoomBtn.setText("방만들기");
        makeRoomBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                Integer[] items = {2, 4, 6, 8};
                JComboBox combo = new JComboBox(items);
                JTextField roomTitleField = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("방제목"));
                panel.add(roomTitleField);
                panel.add(new JLabel("제한인원"));
                panel.add(combo);
                int result = JOptionPane.showConfirmDialog(null, panel, "방만들기",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        makeRoom(roomTitleField.getText(),
                                (Integer) combo.getSelectedItem(),
                                User.getUser().getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        refreshBtn.setBounds(350, 450, 200, 100);
        refreshBtn.setText("새로고침");
        refreshBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                for (int i = defaultRoomTableModel.getRowCount() - 1; i >= 0; i--) {
                    defaultRoomTableModel.removeRow(i);
                }
                for (int i = defaultUserTableModel.getRowCount() - 1; i >= 0; i--) {
                    defaultUserTableModel.removeRow(i);
                }
                getRoomList();
                getUserList();
            }
        });

        rootPanel.add(makeRoomBtn);
        rootPanel.add(refreshBtn);

        jFrame.add(rootPanel);
        jFrame.setSize(1000, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(jFrame.DISPOSE_ON_CLOSE);
        jFrame.setLayout(null);
    }

    public void makeRoom(String roomTitle, int limitNum, int hostId) throws IOException {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(MAKE_ROOM_URL)
                    .header("accept", "application/json")
                    .field("title", roomTitle)
                    .field("limit_num", limitNum)
                    .field("host", hostId)
                    .asJson();
            JSONObject jsonObj = (JSONObject) JSONValue.parse(jsonResponse.getBody().toString());
            makedRoomId = ((Long) jsonObj.get("id")).intValue();
            jFrame.dispose();
            User.getUser().setIsHost(true);
            User.getUser().setCurrent_room(makedRoomId);
            new RoomFrame(makedRoomId);
        } catch (UnirestException e) {}
    }

    public void getRoomList() {
        try {
            JSONObject room = null;
            HttpResponse<JsonNode> jsonResponse = Unirest.get(GET_ROOMS_URL)
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()!=404) {
                Object jsonObj = JSONValue.parse(jsonResponse.getBody().toString());
                JSONArray roomArray = (JSONArray) jsonObj;
                Object[] tempObject = new Object[ROOM_FIELDS_NUM]; // room의 colume 갯수 = 5
                for (int i = 0; i < roomArray.size(); ++i) {
                    room = (JSONObject) roomArray.get(i);
                    for (int j = 0; j < ROOM_FIELDS_NUM; ++j) {
                        if(roomColumns[j].equals("is_playing"))
                            tempObject[j] = "대기중";
                        else
                            tempObject[j] = room.get(roomColumns[j]);

                    }
                    defaultRoomTableModel.addRow(tempObject);
                }
            }else {
                System.out.println("대기중인 방이 없습니다.");
            }

        }catch (UnirestException e ) {}
    }

    public void getUserList() {
        try {
            JSONObject user = null;
            HttpResponse<JsonNode> jsonResponse = Unirest.get(GET_USERS_URL)
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()!=404) {
                Object jsonObj = JSONValue.parse(jsonResponse.getBody().toString());
                JSONArray userArray = (JSONArray) jsonObj;
                Object[] tempObject = new Object[USER_FIELDS_NUM]; // room의 colume 갯수 = 3
                for(Object u: userArray) {

                }
                for (int i = 0; i < userArray.size(); i++) {
                    user = (JSONObject) userArray.get(i);
                    for (int j = 0; j < USER_FIELDS_NUM; j++) {
                        if(userColumns[j].equals("current_room"))
                            tempObject[j] = "대기중";
                        else if (userColumns[j].equals("id") && (((Long)user.get(userColumns[j])).intValue() == User.getUser().getId()))
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

    public void getUser(int userId){
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(GET_USER_URL + userId)
                    .header("accept", "application/json")
                    .asJson();
            if (jsonResponse.getStatus()==200) {
                Object obj = JSONValue.parse(jsonResponse.getBody().toString());
                JSONObject jsonObject = (JSONObject) obj;
                User.getUser().setId(userId);
                User.getUser().setUsername((String) jsonObject.get("username"));
                System.out.println("사용자를 Get하다." + User.getUser());
            } else {
                System.out.println("사용자가 없습니다.");
            }

        }catch (UnirestException e ) {}
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("당신의 id 정수 값을 입력하세요. (1~3)의 아이디를 입력해야함.: ");
        User.getUser().setId(scanner.nextInt());

        scanner.close();

        new LobbyFrame();
    }
}
