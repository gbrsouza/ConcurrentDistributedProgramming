import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args){
        System.out.println("[INFO] Web server started");
        try {
            ServerSocket serverSocket = new ServerSocket(80);
            while (true){
                System.out.println("Waiting for client request...");
                Socket remote = serverSocket.accept();
                System.out.println("Connection made");
                new Thread(new ClientHandler(remote)).start();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] problems to start the web server");
        }
    }

}
