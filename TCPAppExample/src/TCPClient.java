import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

    public static void main (String[] args){
        Socket connection = null;
        try{
            connection = new Socket("localhost", 8080);
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            MsgTCP msg = new MsgTCP();
            msg.setMem(20);
            output.writeObject(msg);
            output.flush();
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
        } catch (IOException e) {
            System.out.println("error to create a new connection with the server");
        } finally {
            try{
                assert connection != null;
                connection.close();
            }catch (IOException e){
                System.out.println("error to close connection");
            }
        }
    }
}
