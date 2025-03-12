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
}
