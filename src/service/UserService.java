package service;

import util.PasswordUtil;
import dao.UserDAO;

public class UserService
{
    public static boolean register(String name, String password, String email)
    {
        String encryptedPassword = PasswordUtil.encrypt(password);
        return UserDAO.registerUser(name, encryptedPassword, email);
    }

    public static String login(String username, String password) {
        String storedPassword = UserDAO.getPasswordByUsername(username);
        if (storedPassword == null) {
            return "❌ 用户不存在！";
        }
        if (storedPassword.equals(PasswordUtil.encrypt(password))) {
            return "✅ 登录成功！";
        } else {
            return "❌ 密码错误！";
        }
    }




}
