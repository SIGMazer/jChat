import java.util.*;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientHandler implements Runnable{
    private Socket client;
    private String name;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket){
        this.client = socket;
        try{
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            System.err.println("Error while creating client handler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void send(String msg){
        out.println(msg);
        out.flush();
    }
    @Override
    public void run(){
        try{
            setName();
            Server.clients.add(this);
            String msg;
            while((msg = in.readLine()) != null){
                if(msg.equals(":exit")){
                    Server.broadcast(this.name + " has left the chat!", this);
                    Server.clients.remove(this);
                    shutdown();
                    break;
                }
                if(msg.isEmpty())
                    continue;
                Server.broadcast(this.name +": "+msg, this);
            }
        }catch(IOException e){
            System.err.println("Error while reading from client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void shutdown(){
        out.close();
        try{
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void setName(){
        out.print("Enter your name: ");
        out.flush();
        try{
            this.name = in.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
