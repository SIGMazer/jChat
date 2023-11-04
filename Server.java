import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server{
    public static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        int port = 6969;
        try(ServerSocket socket = new ServerSocket(port)){
            System.out.println("Server is listening on port " + port);
            while(true){
                Socket client = socket.accept();
                System.out.println(client.getRemoteSocketAddress()+" is connected!");
                ClientHandler handler = new ClientHandler(client);
                Thread thread = new Thread(handler);
                thread.start();
            }
        }catch(IOException e){
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void broadcast(String msg, ClientHandler sender){
        for(ClientHandler client : clients){
            if(client != sender)
                client.send(msg);
        }
    }
}




