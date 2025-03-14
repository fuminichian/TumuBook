package model;

public class User {
    private String username;
    private String email;
    private String registerTime;

    public User(String username, String email, String registerTime) {
        this.username = username;
        this.email = email;
        this.registerTime = registerTime;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRegisterTime() { return registerTime; }
}
