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
    private static void broadcast(String msg, ClientHandler sender){
        for(ClientHandler client : clients){
            if(client != sender)
                client.send(msg);
        }
    }

    public static void messagehandler(Message mas){
        Message msg = mas;
        String name = msg.sender.getName(); 
        ClientHandler client = msg.sender;
        switch(mas.type){
            case CONNECT:
                client.setName();
                for(ClientHandler c : clients){
                    if(c.getName().equals(client.getName())){
                        client.send("Name already taken!");
                        client.shutdown();
                        return;
                    }
                }
                clients.add(client);
                broadcast(client.getName() +": join the group", mas.sender);
                client.send("Welcome to the group buddy!");
                break;
            case DISCONNECT:
                broadcast(name +": left the group", mas.sender);
                clients.remove(mas.sender);
                client.send("Bye!");
                client.shutdown();
                break;
            case MESSAGE:
                broadcast(name +": "+msg.content, mas.sender);
                break;
        }
    }
}




