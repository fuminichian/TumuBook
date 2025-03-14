package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import service.NoteService;
import dao.UserDAO;
import java.util.List;
import java.util.ArrayList;
import dao.NoteDAO;
import model.Note;
import model.User;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame implements ActionListener {
    private String username;
    private int userId;
    private DefaultListModel<Note> noteListModel;
    private JList<Note> noteList; // 确保使用类成员变量
    private JButton addButton, editButton, deleteButton, logoutButton;
    private JButton userInfoButton;


    public MainFrame(String username) {
        this.username = username;
        this.userId = UserDAO.getUserID(username);
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel); // 使用类成员变量初始化 noteList
        JScrollPane scrollPane = new JScrollPane(noteList);
        add(scrollPane);

        setTitle("在线笔记系统 - 主页");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();

        loadNotes(username);

        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        // 笔记列表
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel);
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加鼠标双击监听器
        noteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击打开详细信息
                    int selectedIndex = noteList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Note selectedNote = noteListModel.getElementAt(selectedIndex);
                        new NoteDetailFrame(selectedNote);
                    }
                }
            }
        });

        panel.add(new JScrollPane(noteList), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        addButton = new JButton("新建笔记");
        editButton = new JButton("编辑笔记");
        deleteButton = new JButton("删除笔记");
        logoutButton = new JButton("退出");
        userInfoButton = new JButton("用户信息");

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        logoutButton.addActionListener(this);
        userInfoButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(userInfoButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
    }

    private void loadNotes(String username) {
        noteListModel.clear();  // 清空旧数据

        List<Note> notes = NoteDAO.getNotebookInfo(userId);

        for (Note note : notes) {
            noteListModel.addElement(note);  // 添加笔记到模型中
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            createNote();
        } else if (e.getSource() == editButton) {
            editNote();
        } else if (e.getSource() == deleteButton) {
            deleteNote();
        } else if (e.getSource() == logoutButton) {
            logout();
        }else if (e.getSource() == userInfoButton) { // 点击用户信息按钮
            showUserInfo();
        }
    }

    private void createNote() {
        String title = JOptionPane.showInputDialog(this, "输入笔记标题:");
        if (title == null || title.trim().isEmpty()) return;

        String content = JOptionPane.showInputDialog(this, "输入笔记内容:");
        if (content == null || content.trim().isEmpty()) return;
        System.out.println(NoteService.addNote(userId, title, content));
        loadNotes(username);
    }

    private void editNote() {
        // 检查 noteList 是否为 null
        if (noteList == null) {
            JOptionPane.showMessageDialog(this, "笔记列表未初始化！");
            return;
        }

        // 获取选中的笔记
        int selectedIndex = noteList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的笔记！");
            return;
        }

        // 获取笔记的旧内容
        Note selectedNote = noteListModel.getElementAt(selectedIndex);
        String oldTitle = selectedNote.getTitle();
        String oldContent = selectedNote.getContent();
        int notebookId = selectedNote.getId();
        int userId = UserDAO.getUserID(username);

        // 创建编辑框
        JTextField titleField = new JTextField(oldTitle);
        JTextArea contentArea = new JTextArea(oldContent, 5, 20);  // 设置默认大小为 5 行 20 列
        JScrollPane scrollPane = new JScrollPane(contentArea);

        // 创建输入面板
        JPanel panel = new JPanel(new GridLayout(3, 1));  // 使用 3 行（标题、内容和内容区域）
        panel.add(new JLabel("标题:"));
        panel.add(titleField);
        panel.add(new JLabel("内容:"));
        panel.add(scrollPane);

        // 弹出确认对话框
        int result = JOptionPane.showConfirmDialog(this, panel, "编辑笔记", JOptionPane.OK_CANCEL_OPTION);

        // 如果点击 OK，执行笔记更新
        if (result == JOptionPane.OK_OPTION) {
            String newTitle = titleField.getText().trim();
            String newContent = contentArea.getText().trim();

            // 检查标题和内容是否为空
            if (newTitle.isEmpty() || newContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "标题和内容不能为空！");
                return;
            }

            // 更新笔记内容
            boolean success = NoteDAO.updateNote(userId, newTitle, newContent, notebookId);
            if (success) {
                JOptionPane.showMessageDialog(this, "笔记更新成功！");
                loadNotes(username); // 刷新笔记列表
            } else {
                JOptionPane.showMessageDialog(this, "笔记更新失败，请稍后再试。");
            }
        }
    }

    private void deleteNote() {
        if (noteList == null) {
            JOptionPane.showMessageDialog(this, "笔记列表未初始化！");
            return;
        }

        // 获取选中的笔记索引
        int selectedIndex = noteList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的笔记！");
            return;
        }

        // 获取选中的笔记对象
        Note selectedNote = noteListModel.getElementAt(selectedIndex);
        int noteId = selectedNote.getId();

        // 弹出确认对话框，避免误删除
        int confirm = JOptionPane.showConfirmDialog(
                this, "确定要删除这条笔记吗？", "删除确认", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = NoteDAO.deleteNote(noteId);
            if (success) {
                JOptionPane.showMessageDialog(this, "笔记删除成功！");
                loadNotes(username); // 重新加载笔记列表
            } else {
                JOptionPane.showMessageDialog(this, "笔记删除失败，请稍后再试。");
            }
        }
    }

    private void logout() {
        dispose();
        this.setVisible(false); // 仅隐藏 MainFrame
        LoginRegisterGUI.getInstance().setVisible(true);
    }

    private void showUserInfo() {
        User user = UserDAO.getUserInfo(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "无法获取用户信息，请稍后再试。");
            return;
        }

        String message = String.format(
                "用户名: %s\n邮箱: %s\n注册时间: %s",
                user.getUsername(),
                user.getEmail(),
                user.getRegisterTime()
        );

        JOptionPane.showMessageDialog(this, message, "用户信息", JOptionPane.INFORMATION_MESSAGE);
    }


}

