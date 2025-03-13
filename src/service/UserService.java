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
    public static boolean login(String username, String password) {
        String storedPassword = UserDAO.getPasswordByUsername(username);
        if (storedPassword == null) {
            System.out.println("❌ 用户不存在！");
            return false;
        }
        if (storedPassword.equals(PasswordUtil.encrypt(password))) {
            System.out.println("✅ 登录成功！");
            return true;
        } else {
            System.out.println("❌ 密码错误！");
            return false;
        }
    }

    public static String getuserInfo(String username) {
        return UserDAO.getUserInfo(username);
    }


}
