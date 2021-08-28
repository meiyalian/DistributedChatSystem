package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer {
    public static final int port = 6379;
    private static final ChatManager chatManager = new ChatManager();
    private static final CommandFactory commandFactory = new CommandFactory();
    private boolean alive;


    public static void main(String[] args) {
        new ChatServer().handle();
    }


    public void handle() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", port));
            alive = true;

            while (alive){
                System.out.println("Wait for client connection request: ");
                Socket soc = serverSocket.accept();
                ChatConnection chatConnection = new ChatConnection(soc, chatManager, commandFactory);
                chatConnection.start(); // connection is thread
                chatManager.addClientConnection(chatConnection);
            }
        } catch (IOException e) {
            alive = false;
            e.printStackTrace();
        }
    }
}
