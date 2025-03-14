import service.UserService;
import service.NoteService;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.LoginRegisterGUI;
import util.DBUtil;

public class Main
{

    public static void main(String[] args) {
        DBUtil.initializeDatabase();
        SwingUtilities.invokeLater(LoginRegisterGUI::new);
    }

}