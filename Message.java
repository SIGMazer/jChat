enum MessageType{
    CONNECT, DISCONNECT, MESSAGE, 
}
public class Message{
    public String content;
    public ClientHandler sender;
    public MessageType type;
    public Message(ClientHandler sender, String content, MessageType type){
        this.sender = sender;
        this.content = content;
        this.type = type;
    }
}
