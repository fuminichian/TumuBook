import service.UserService;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        System.out.print("请输入邮箱: ");
        String email = scanner.nextLine();

        // 调用 UserService 进行注册
        boolean success = UserService.register(username, password, email);
        if (success) {
            System.out.println("✅ 用户注册成功！");
        } else {
            System.out.println("❌ 用户注册失败，可能用户名或邮箱已存在。");
        }

        scanner.close();
    }

}