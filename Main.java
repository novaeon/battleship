import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner scanner = new Scanner(System.in);
    String role;

    System.out.println(
        "██████╗  █████╗ ████████╗████████╗██╗     ███████╗███████╗██╗  ██╗██╗██████╗ \r\n" + //
        "██╔══██╗██╔══██╗╚══██╔══╝╚══██╔══╝██║     ██╔════╝██╔════╝██║  ██║██║██╔══██╗\r\n" + //
        "██████╔╝███████║   ██║      ██║   ██║     █████╗  ███████╗███████║██║██████╔╝\r\n" + //
        "██╔══██╗██╔══██║   ██║      ██║   ██║     ██╔══╝  ╚════██║██╔══██║██║██╔═══╝ \r\n" + //
        "██████╔╝██║  ██║   ██║      ██║   ███████╗███████╗███████║██║  ██║██║██║     \r\n" + //
        "╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝╚═╝     \r\n" + //
        "-----------------------------------------------------------------------------");
                                                                                
    System.out.print("Do you want to be a server or client? ");
    do {
        role = scanner.nextLine();

        if (role.equalsIgnoreCase("server")) {
          System.out.println("------------------------------ Starting server ------------------------------");
          ServerSocket socket = new ServerSocket(2007);
          Server server = new Server(socket);
          server.startServer();
        } else if (role.equalsIgnoreCase("client")) {
          System.out.println("------------------------------ Starting client ------------------------------");
          BattleshipClient client = new BattleshipClient();
          client.run();
        } else {
          System.out.print("Invalid input. Please enter 'server' or 'client': ");
        }
    } while (!role.equalsIgnoreCase("server") && !role.equalsIgnoreCase("client"));

    scanner.close();
  }
}
