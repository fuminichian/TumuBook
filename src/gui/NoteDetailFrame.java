package gui;

import model.Note;
import model.Tag;
import dao.NoteDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NoteDetailFrame extends JFrame {
    private JTextField tagInputField; // 标签输入框
    private JList<Tag> tagList; // 显示标签的 JList
    private DefaultListModel<Tag> tagListModel; // 标签列表模型

    public NoteDetailFrame(Note note) {
        setTitle("笔记详情");
        setSize(400, 400);  // 增加尺寸，以容纳标签输入区域
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

        // 标签显示部分
        JPanel tagPanel = new JPanel(new BorderLayout());
        tagPanel.setBorder(BorderFactory.createTitledBorder("标签"));

        tagListModel = new DefaultListModel<>();
        tagList = new JList<>(tagListModel);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tagScrollPane = new JScrollPane(tagList);

        // 获取当前笔记的标签
        List<Tag> tags = NoteDAO.getTagsForNote(note.getId());
        for (Tag tag : tags) {
            tagListModel.addElement(tag);
        }

        // 标签输入区域
        tagInputField = new JTextField(15);
        JButton addTagButton = new JButton("添加标签");
        addTagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tagName = tagInputField.getText().trim();
                if (!tagName.isEmpty()) {
                    // 添加标签到笔记
                    NoteDAO.addTagToNote(note.getId(), tagName);
                    tagListModel.addElement(new Tag(0, tagName)); // 将新标签添加到标签列表
                    tagInputField.setText("");  // 清空输入框
                }
            }
        });

        JPanel tagInputPanel = new JPanel();
        tagInputPanel.add(tagInputField);
        tagInputPanel.add(addTagButton);

        tagPanel.add(tagScrollPane, BorderLayout.CENTER);
        tagPanel.add(tagInputPanel, BorderLayout.SOUTH);

        // 将各部分组件添加到主面板
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        panel.add(tagPanel, BorderLayout.EAST);  // 标签部分放在右侧

        add(panel);
        setVisible(true);
    }
}


