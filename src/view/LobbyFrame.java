package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//public class LoginFrame extends JFrame implements ActionListener
public class LobbyFrame extends JFrame {
    String roomColumns[] = {"id", "title", "current_num", "limit_num", "is_playing"};
    Object roomRows[][] = null;

    String userColumns[] = {"username", "current_room"};
    Object userRows[][] = null;;

    private static String GET_USERS_URL = "http://localhost:8080/users";
    private static String GET_ROOMS_URL = "http://localhost:8080/rooms";

    private static int ROOM_FIELDS_NUM = 5;
    private static int USER_FIELDS_NUM = 2;

    DefaultTableModel defaultRoomTableModel = new DefaultTableModel(roomRows, roomColumns) {
//      테이블 내부의 데이터를 수정할 수 없게 한다.
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    DefaultTableModel defaultUserTableModel = new DefaultTableModel(userRows, userColumns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };


    JFrame jFrame = new JFrame("로비");

    public LobbyFrame() throws IOException, ParseException, URISyntaxException{
//      로비가 처음 로딩될 때, 방 목록과 사용자 목록을 초기화 해준다.
        getRoomList();
        getUserList();

        JTable roomTable = new JTable();
        roomTable.setModel(defaultRoomTableModel);
        roomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

            }
        });

        JTable userTable = new JTable();
        userTable.setModel(defaultUserTableModel);

//      jScrollPane에 jTable을 포함시킴
        JScrollPane roomScrollPane = new JScrollPane(roomTable);
        JScrollPane userScrollPane = new JScrollPane(userTable);

        roomScrollPane.getViewport().setBackground(Color.BLUE);
        roomScrollPane.setBounds(0, 0, 600, 400);

        userScrollPane.getViewport().setBackground(Color.GREEN);
        userScrollPane.setBounds(700, 0, 300, 600);

//      루트 패널에 tablePanel을 포함시킴
        JPanel rootPanel = new JPanel();
        rootPanel.add(roomScrollPane);
        rootPanel.add(userScrollPane);
        rootPanel.setBackground(Color.RED);
        rootPanel.setLayout(null);

        JButton makeRoomBtn = new JButton();
        JButton refreshBtn = new JButton();

        makeRoomBtn.setBounds(50,450,200,100);
        makeRoomBtn.setText("방만들기");
        refreshBtn.setBounds(350,450,200,100);
        refreshBtn.setText("새로고침");

        rootPanel.add(makeRoomBtn);
        rootPanel.add(refreshBtn);
//      프레임 설정
        jFrame.add(rootPanel);
        jFrame.setSize(1000, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(jFrame.DISPOSE_ON_CLOSE);
        jFrame.setLayout(null);

    }

    public void getRoomList() throws URISyntaxException, IOException, ParseException {

        URI uri = new URI(GET_ROOMS_URL);
        uri = new URIBuilder(uri).build();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri));
        HttpEntity json_entity = response.getEntity();
        String jsonInfo = EntityUtils.toString(json_entity);

        if(response.getStatusLine().getStatusCode() == 200) {
            Object jsonObject=  JSONValue.parse(jsonInfo);
            JSONArray roomArray = (JSONArray)jsonObject;

            JSONObject room = null;
            Object []tempObject = new Object[ROOM_FIELDS_NUM]; // room의 colume 갯수 = 3
            for (int i=0; i<roomArray.size(); ++i) {
                room = (JSONObject) roomArray.get(i);
                for( int j=0; j<ROOM_FIELDS_NUM; ++j) {
                    tempObject[j] = room.get(roomColumns[j]);
                }
                defaultRoomTableModel.addRow(tempObject);
            }
        } else {
            // 대기중인 방이 단 하나도 없을 경우
        }
    }

    public void getUserList() throws URISyntaxException, IOException, ParseException {
        URI uri = new URI(GET_USERS_URL);
        uri = new URIBuilder(uri).build();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri));
        HttpEntity json_entity = response.getEntity();
        String jsonInfo = EntityUtils.toString(json_entity);

        System.out.println(jsonInfo);
        if(response.getStatusLine().getStatusCode() == 200) {
            Object jsonObject=  JSONValue.parse(jsonInfo);
            JSONArray userArray = (JSONArray)jsonObject;

            JSONObject user = null;
            Object []tempObject = new Object[USER_FIELDS_NUM]; // room의 colume 갯수 = 3
            for (int i=0; i<userArray.size(); ++i) {
                user = (JSONObject) userArray.get(i);
                for( int j=0; j<USER_FIELDS_NUM; ++j) {
                    tempObject[j] = user.get(userColumns[j]);
                }
                defaultUserTableModel.addRow(tempObject);
            }
        } else {
            // 대기중인 사용자가 한명도 없을 경우.
        }
    }

//    public void actionPerformed(ActionEvent e){
//
//
//        //로그인 할때
//        if(e.getSource() == loginButton) {
//            System.out.println("LoginButton click");
//
//            String id = loginIDField.getText();
//            char[] pass = loginPasswordField.getPassword(); //...인걸 받을땐 캐릭터형태로 받는듯
//            String passward = new String(pass); // 이걸 다시 String으로 바꿈
//
//            if(id.equals("") || passward.equals("")){
//                //아이디 혹은 비밀번호를 입력하세요 라는 메세지 띄우기
//                JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 모두 입력하세요.");
//                //원래 null에는 상속하는게 들어감.
//            }
//            else{
//                boolean existLogin = LoginFrame.Login(id, passward);
//                if(existLogin){//성공 시 실행 할 부분
//                    JOptionPane.showMessageDialog(null, "로그인 성공!!!");
//                    this.dispose();//로그인 창 끄기
//                    LobbyFrame lobbyFrame = new LobbyFrame();//LobbyFrame창 띄우기
//                }
//                else{//실패 시 실행 할 부분
//                    JOptionPane.showMessageDialog(null, "로그인 실패!!!");
//                }
//            }
//        }//회원가입 할 때
//        else if(e.getSource() == RegitButton) {
//            System.out.println("RegitButton click");
//
//            this.dispose();//로그인 창 끄기
//            SignUp lobbyFrame = new SignUp();//LobbyFrame창 띄우기
//        }
//    }

    public static void main(String[] args) throws URISyntaxException, IOException, ParseException {


        new LobbyFrame();
    }
}
