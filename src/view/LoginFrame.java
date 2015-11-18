package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.*;


import controller.LoginService;
import view.LobbyFrame;


public class LoginFrame extends JFrame implements ActionListener{
    BufferedImage img = null;
    JTextField loginIDField = null;
    JPasswordField loginPasswordField = null;
    JButton loginButton = null;

    public static void main(String[] args) {
        new LoginFrame();
    }

    public LoginFrame() {
        setTitle("로그인");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        try {
            img = ImageIO.read(new File("img/bg_login.jpg"));
        } catch (IOException e) {
            System.out.println("이미지 받아오기 실패!");
            System.exit(0);
        }

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1000, 600);
        layeredPane.setLayout(null);


        MyPanel panel = new MyPanel();
        panel.setBounds(0,0,1000,600);

        // ID 입력 필드
        loginIDField = new JTextField(15);
        loginIDField.setBounds(400, 300, 200, 30);


        // ID Label 지정
        JLabel labelID = new JLabel("ID", JLabel.CENTER);
        labelID.setBounds(350, 288, 50, 50);
        labelID.setLabelFor(loginIDField);

        layeredPane.add(labelID);
        layeredPane.add(loginIDField);

        // Password 입력 필드
        loginPasswordField = new JPasswordField(15);
        loginPasswordField.setBounds(400, 400, 200, 30);

        // Password Label 지정
        JLabel labelPassword = new JLabel("Password", JLabel.CENTER);
        labelPassword.setBounds(300,388,100,50);
        labelPassword.setLabelFor(loginPasswordField);

        layeredPane.add(labelPassword);
        layeredPane.add(loginPasswordField);

        loginButton = new JButton(new ImageIcon("img/btn_login.png"));
        loginButton.setBounds(480, 500, 60, 57);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.addActionListener(this);

        layeredPane.add(loginButton);

        layeredPane.add(panel);
        add(layeredPane);

        setVisible(true);
    }

    class MyPanel extends JPanel{
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    }

    @Override
    public  void actionPerformed(ActionEvent e) {

        String id = loginIDField.getText();
        char[] pass = loginPasswordField.getPassword();
        String password = new String(pass);

        System.out.println(id + password);


//        if (id.equals("") || password.equals("")) {
//            // 메시지를 날린다.
//            JOptionPane.showMessageDialog(null, "빈칸이 있네요");
//        } else {
//
//            // 로그인 참 거짓 여부를 판단
//            boolean existLogin = LoginService.loginTest(id, password);
//
//            if (existLogin) {
//                // 로그인 성공일 경우
//                JOptionPane.showMessageDialog(null, "로그인 성공");
//            } else {
//                // 로그인 실패일 경우
//                JOptionPane.showMessageDialog(null, "로그인 실패");
//            }
//
//        }
        password = null;
    }

}
