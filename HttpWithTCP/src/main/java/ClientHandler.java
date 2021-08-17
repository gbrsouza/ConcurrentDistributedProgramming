import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    private final Socket socket;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        System.out.println("\n ClientHandler started for" + this.socket);
        handleRequest(this.socket);
        System.out.println("\n ClientHandler terminated for" + this.socket + "\n");
    }

    /**
     * The handleRequest method uses the input and output streams to communicate
     * with the server. In addition, it determines what request was made and then
     * processes that request.
     */
    private void handleRequest(Socket socket) {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String headerLine = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();

            switch (httpMethod){
                case "GET":
                    System.out.println("[INFO] GET method processed");
                    String token = tokenizer.nextToken();

                    if (!token.equals("/"))
                        sendResponse(socket, 404, "", "");

                    String response =
                            "<html><h1>WebServer Home Page...</h1><br>" +
                            "<b>Welcome to my web server!</b><br>" +
                            "</html>";

                    sendResponse(socket, 200, response, "");
                    break;
                case "POST":
                    System.out.println("[INFO] POST method processed");
                    token = tokenizer.nextToken();

                    if (token.equals("/"))
                        sendResponse(socket, 200, "", token);
                    else
                        sendResponse(socket, 404, "", token);
                    break;
                case "HEAD":
                    System.out.println("[INFO] HEAD method processed");

                    break;
                case "PUT":
                    System.out.println("[INFO] PUT method processed");
                    token = tokenizer.nextToken();

                    if (token.equals("/"))
                        sendResponse(socket, 201, "", token);
                    else if (token.equals("/existing.html"))
                        sendResponse(socket, 204, "", token);
                    else
                        sendResponse(socket, 404, "", token);
                    break;
                case "DELETE":
                    System.out.println("[INFO] DELETE method processed");

                    token = tokenizer.nextToken();

                    if (token.equals("/"))
                        sendResponse(socket, 200, "<html>\n" +
                                "  <body>\n" +
                                "    <h1>Arquivo removido.</h1>\n" +
                                "  </body>\n" +
                                "</html>", token);
                    else if (token.equals("/easy"))
                        sendResponse(socket, 204, "", token);
                    else if (token.equals("/big"))
                        sendResponse(socket, 202, "", token);
                    else
                        sendResponse(socket, 404, "", token);
                    break;
                default:
                    System.out.println("The HTTP method is not recognized");
                    sendResponse(socket, 405, "Method Not Allowed", "");

            }
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to read data from client");
        }
    }

    private void sendResponse(Socket socket, int statusCode, String responseString, String contentLocation) {
        String statusLine;
        String serverHeader = "Server:WebServer\r\n";
        String contentTypeHeader = "Content-Type:text/html\r\n";

        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            switch (statusCode){
                case 200:
                    statusLine = "HTTP/1.0 200 OK\r\n";
                    contentLocation = "Content-Location: " + contentLocation;
                    String contentLengthHeader = "Content-Length:" + responseString.length() + "\r\n";

                    out.writeBytes(contentLocation);
                    out.writeBytes(statusLine);
                    out.writeBytes(serverHeader);
                    out.writeBytes(contentTypeHeader);
                    out.writeBytes(contentLengthHeader);
                    out.writeBytes("\r\n");
                    out.writeBytes(responseString);
                    break;
                case 201:
                    statusLine = "HTTP/1.0 201 Created\r\n";
                    contentLengthHeader = "Content-Length:" + responseString.length() + "\r\n";

                    out.writeBytes(statusLine);
                    out.writeBytes(serverHeader);
                    out.writeBytes(contentTypeHeader);
                    out.writeBytes(contentLengthHeader);
                    out.writeBytes("\r\n");
                    out.writeBytes(responseString);
                    break;
                case 202:
                    statusLine = "HTTP/1.0 202 Accepted \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 203:
                    statusLine = "HTTP/1.0 203 Non-Authoritative Information \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 204:
                    statusLine = "HTTP/1.0 204 No Content\r\n";
                    contentLengthHeader = "Content-Length:" + responseString.length() + "\r\n";

                    out.writeBytes(statusLine);
                    out.writeBytes(serverHeader);
                    out.writeBytes(contentTypeHeader);
                    out.writeBytes(contentLengthHeader);
                    out.writeBytes("\r\n");
                    out.writeBytes(responseString);
                    break;
                case 205:
                    statusLine = "HTTP/1.0 205 Reset Content \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 206:
                    statusLine = "HTTP/1.0 206 Partial Content \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 207:
                    statusLine = "HTTP/1.0 207 Multi-Status \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 208:
                    statusLine = "HTTP/1.0 208 Multi-Status \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 226:
                    statusLine = "HTTP/1.0 226 IM Used \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 300:
                    statusLine = "HTTP/1.0 300 Multiple Choice\r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 301:
                    statusLine = "HTTP/1.0 301 Moved Permanently \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 302:
                    statusLine = "HTTP/1.0 302 Found \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 303:
                    statusLine = "HTTP/1.0 303 See Other \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 304:
                    statusLine = "HTTP/1.0 304 Not Modified \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 307:
                    statusLine = "HTTP/1.0 307 Temporary Redirect \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 308:
                    statusLine = "HTTP/1.0 308 Permanent Redirect \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 400:
                    statusLine = "HTTP/1.0 400 Bad Request \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 401:
                    statusLine = "HTTP/1.0 401 Unauthorized \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 402:
                    statusLine = "HTTP/1.0 402 Payment Required \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 403:
                    statusLine = "HTTP/1.0 403 Forbidden \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 404:
                    statusLine = "404 Not Found\r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 405:
                    statusLine = "HTTP/1.0 405 Method Not Allowed \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 406:
                    statusLine = "HTTP/1.0 406 Not Acceptable \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 407:
                    statusLine = "HTTP/1.0 407 Proxy Authentication Required \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 408:
                    statusLine = "HTTP/1.0 408 Request Timeout \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 409:
                    statusLine = "HTTP/1.0 409 Conflict \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 500:
                    statusLine = "HTTP/1.0 500 Internal Server Error \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 501:
                    statusLine = "HTTP/1.0 501 Not Implemented \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 502:
                    statusLine = "HTTP/1.0 502 Bad Gateway \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;
                case 503:
                    statusLine = "503 Service Unavailable \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
                    break;

                default:
                    statusLine = "HTTP/1.0 404 Not Found \r\n";
                    out.writeBytes(statusLine);
                    out.writeBytes("\r\n");
            }
            out.close();
        } catch (IOException e) {
            System.out.println("[ERROR] Problem to create message response");
        }
    }
}
