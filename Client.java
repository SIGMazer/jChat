import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; 
        int serverPort = 6969; 

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the chat server. Type ':exit' to quit.");
            System.out.print("Enter your name: ");
            // Read messages from the server
            Thread receiveThread = new Thread(() -> {
                String serverMessage;
                try {
                    while (true) {
                        serverMessage = in.readLine();
                        if(serverMessage == null){
                            System.exit(1);
                        }
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String userMessage;
            while (true) {
                userMessage = userInput.readLine();
                out.println(userMessage);
                if (userMessage.equals(":exit")) {
                    break;
                }
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Shutting down...");
                    out.println(":exit");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

