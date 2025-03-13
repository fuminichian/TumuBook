import service.UserService;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Plese enter the name of the user:");
        String name = scanner.nextLine();

        System.out.println("Plese enter the password of the user:");
        String password = scanner.nextLine();



        scanner.close();
    }

}