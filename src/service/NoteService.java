package service;

import dao.NoteDAO;
import dao.UserDAO;

public class NoteService {
    public static String addNote(int userId, String title, String content) {
        boolean result = NoteDAO.addNote(userId, title, content);
        if (result) {
            return "笔记添加成功";
        }
        else{
            return "笔记添加失败";
        }

    }

//    public static String getnotebookInfo(String username) {
//        int userId = UserDAO.getUserID(username);
//        return NoteDAO.getNotebookInfo(userId);
//    }

//    public static String updateNote(int userId, String title, String content) {
//        if (NoteDAO.updateNote(userId,title,content,notebookId)){
//            return "笔记修改成功";
//        }
//        else{
//            return "笔记修改失败";
//        }
//    }

}
