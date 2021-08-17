import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpClient {

    public static void main (String[] args){
        System.out.println("HTTP Client started");
        Socket connection = null;
        try{
            connection = new Socket("localhost", 80);
            OutputStream out = connection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            System.out.println("[INFO] Testing GET method");
            sendGet(out, "/");
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

            System.out.println("[INFO] Testing GET method without host");
            connection = new Socket("localhost", 80);
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            sendGet(out, "/index.html");
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

            System.out.println("[INFO] Testing POST method");
            connection = new Socket("localhost", 80);
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String insertion = "id=154&name=Desodorante&qnt=200&valor=13.84\r\n";
            sendPost(out, insertion);
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

            System.out.println("[INFO] Testing PUT method");
            connection = new Socket("localhost", 80);
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            insertion = "id=153&name=sabonete&qnt=200&valor=2.30\r\n";
            sendPut(out, insertion, "/");
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

            System.out.println("[INFO] Testing PUT method");
            connection = new Socket("localhost", 80);
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            insertion = "id=153&name=sabonete&qnt=200&valor=2.30\r\n";
            sendPut(out, insertion, "/existing.html");
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

            System.out.println("[INFO] Testing DELETE method");
            connection = new Socket("localhost", 80);
            out = connection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String deletion = "id=153\r\n";
            sendPut(out, deletion, "/");
            System.out.println(getResponse(in));
            connection.close();
            Thread.sleep(2000);

        } catch (UnknownHostException e) {
            System.out.println("[ERROR] Unknown Host");
        } catch (IOException e) {
            System.out.println("[ERROR] problem to create a new connection with the server");
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Interrupted exception in a thread");
        } finally {
            try{
                assert connection != null;
                connection.close();
            }catch (IOException e){
                System.out.println("error to close connection");
            }
        }
    }

    private static String getResponse(BufferedReader in) {
        try{
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine).append("\n");
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private static void sendGet(OutputStream out, String route) {
        String header = "GET " + route + "\r\n";
        try{
            out.write(header.getBytes());
            out.write("User-Agent:Mozilla/5.0\r\n".getBytes());
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to send a GET request");
        }
    }

    private static void sendPost(OutputStream out, String requestString){
        try {
            String contentLengthHeader = "Content-Length:" + requestString.length() + "\r\n";
            out.write("POST / HTTP/1.1\r\n".getBytes());
            out.write("User-Agent:Mozilla/5.0\r\n".getBytes());
            out.write("Content-Type: application/x-www-form-urlencoded\r\n".getBytes());
            out.write(contentLengthHeader.getBytes());
            out.write(requestString.getBytes());
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to send a POST request");
            e.printStackTrace();
        }
    }

    private static void sendPut(OutputStream out, String requestString, String route){
        String header = "PUT " + route + "\r\n";
        try {
            String contentLengthHeader = "Content-Length:" + requestString.length() + "\r\n";
            out.write(header.getBytes());
            out.write("User-Agent:Mozilla/5.0\r\n".getBytes());
            out.write("Content-Type: application/x-www-form-urlencoded\r\n".getBytes());
            out.write(contentLengthHeader.getBytes());
            out.write(requestString.getBytes());
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to send a PUT request");
            e.printStackTrace();
        }
    }

    private static void sendPDelete(OutputStream out, String requestString, String route){
        String header = "DELETE " + route + "\r\n";
        try {
            String contentLengthHeader = "Content-Length:" + requestString.length() + "\r\n";
            out.write(header.getBytes());
            out.write("User-Agent:Mozilla/5.0\r\n".getBytes());
            out.write("Content-Type: application/x-www-form-urlencoded\r\n".getBytes());
            out.write(contentLengthHeader.getBytes());
            out.write(requestString.getBytes());
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to send a DELETE request");
            e.printStackTrace();
        }
    }
}
