package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by haegyun on 11/8/15.
 */
public class LoginFrame extends JFrame{
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
}
