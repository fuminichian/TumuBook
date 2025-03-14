package model;

public class Note {
    private int id;
    private String title;
    private String content;
    private String createTime;
    private String updateTime;

    public Note(int id, String title, String content, String createTime, String updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCreateTime() { return createTime; }
    public String getUpdateTime() { return updateTime; }

    @Override
    public String toString() {
        return title;  // 让 JList 直接显示标题
    }
}
