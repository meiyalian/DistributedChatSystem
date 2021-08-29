package client;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private final Socket socket;
    private String identity = "";
    public static final int PORT = 6379;

    public ChatClient() throws IOException {
        this.socket = new Socket("localhost", PORT);
    }

    public Socket getSocket(){
        return this.socket;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public static void main(String[] args){
        try {
            new ChatClient().handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() throws IOException {
        ClientSender clientSender = new ClientSender(socket, this);
        ClientReceiver clientReceiver = new ClientReceiver(this);

        clientSender.start();
        clientReceiver.start();
    }
}
