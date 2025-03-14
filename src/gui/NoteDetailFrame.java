package gui;

import model.Note;
import javax.swing.*;
import java.awt.*;

public class NoteDetailFrame extends JFrame {
    public NoteDetailFrame(Note note) {
        setTitle("笔记详情");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("标题: " + note.getTitle());
        JTextArea contentArea = new JTextArea(note.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        JLabel createTimeLabel = new JLabel("创建时间: " + note.getCreateTime());
        JLabel updateTimeLabel = new JLabel("最后修改: " + note.getUpdateTime());

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(createTimeLabel);
        infoPanel.add(updateTimeLabel);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}

