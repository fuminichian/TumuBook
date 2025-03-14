package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import service.UserService;


public class LoginRegisterGUI extends JFrame implements ActionListener {
    private JTextField loginUserText;
    private JPasswordField loginPasswordText;
    private JTextField registerUserText;
    private JTextField registerEmailText;
    private JPasswordField registerPasswordText;
    private JButton loginButton;
    private JButton registerButton;
    private static LoginRegisterGUI instance;

    public LoginRegisterGUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("在线笔记系统 - 登录与注册");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("登录", createLoginPanel());
        tabbedPane.addTab("注册", createRegisterPanel());
        add(tabbedPane);
        setVisible(true);

    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("用户名:"));
        loginUserText = new JTextField();
        panel.add(loginUserText);

        panel.add(new JLabel("密码:"));
        loginPasswordText = new JPasswordField();
        panel.add(loginPasswordText);

        loginButton = new JButton("登录");
        loginButton.addActionListener(this);
        panel.add(new JLabel());
        panel.add(loginButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("用户名:"));
        registerUserText = new JTextField();
        panel.add(registerUserText);

        panel.add(new JLabel("邮箱:"));
        registerEmailText = new JTextField();
        panel.add(registerEmailText);

        panel.add(new JLabel("密码:"));
        registerPasswordText = new JPasswordField();
        panel.add(registerPasswordText);

        registerButton = new JButton("注册");
        registerButton.addActionListener(this);
        panel.add(new JLabel());
        panel.add(registerButton);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = loginUserText.getText();
            String password = new String(loginPasswordText.getPassword());
            String loginResult = UserService.login(username, password);

            JOptionPane.showMessageDialog(this, loginResult);
            if (loginResult.contains("✅ 登录成功")) {
                setVisible(false); // 隐藏登录窗口
                new MainFrame(username); // 打开主界面，并传递登录窗口
            }

        }
        else if (e.getSource() == registerButton) {
            String username = registerUserText.getText();
            String email = registerEmailText.getText();
            String password = new String(registerPasswordText.getPassword());
            if(UserService.register(username, password, email)) {
                JOptionPane.showMessageDialog(this, "注册成功");
                registerUserText.setText("");
                registerEmailText.setText("");
                registerPasswordText.setText("");
            }
            else{
                JOptionPane.showMessageDialog(this, "注册失败，请重试");
            }

        }
    }


    public static LoginRegisterGUI getInstance() {
        if (instance == null) {
            instance = new LoginRegisterGUI();
        }
        return instance;
    }

}

