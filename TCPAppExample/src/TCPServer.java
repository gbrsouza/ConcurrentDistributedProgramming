import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main (String[] args) {
        ServerSocket server = null;

        try{
            server = new ServerSocket(8080);
        }catch (IOException e){
            System.out.println("Error to start the server");
        }

        while (true){
            try{
                assert server != null;
                Socket nextClient = server.accept();
                ObjectInputStream input = new ObjectInputStream(nextClient.getInputStream());
                MsgTCP msg = (MsgTCP) input.readObject();
                System.out.println(msg.toString());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error to receive data from client");
                try{
                    server.close();
                } catch (IOException ex) {
                    System.out.println("Error to close connection");
                }
            }


        }
    }

}
