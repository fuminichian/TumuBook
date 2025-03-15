package model;

import java.util.List;
import java.util.ArrayList;

public class Note {
    private int id;
    private String title;
    private String content;
    private String createTime;
    private String updateTime;
    private List<Tag> tags;

    public Note(int id, String title, String content, String createTime, String updateTime, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.tags = tags;

    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCreateTime() { return createTime; }
    public String getUpdateTime() { return updateTime; }
    public List<Tag> getTags() { return tags; }

    @Override
    public String toString() {
        return title;  // 让 JList 直接显示标题
    }
}
