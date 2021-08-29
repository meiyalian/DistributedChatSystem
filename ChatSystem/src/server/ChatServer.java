package server;

import com.google.gson.Gson;
import client_command.NewIdentityCommand;
import shared.IdentityValidator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static final int port = 6379;
    private static int identityCount = 1;
    private final Gson gson = new Gson();
    private static final ChatManager chatManager = new ChatManager();
    private static final CommandFactory commandFactory = new CommandFactory();
    private boolean alive;


    public static void main(String[] args) {
        new ChatServer().handle();
    }

    private synchronized  void generateIdentityForNewClient(ServerConnection serverConnection){
        String newID = autoGenerateIdentity();

        // set serverConnection name to "guest1"
        serverConnection.setName(newID);
        NewIdentityCommand newIdentityCommand = new NewIdentityCommand("", newID);

        // broadcast {"type": "newidentity", "identity": "guest1"} to client
        String jsonMessage = gson.toJson(newIdentityCommand);
        chatManager.broadCast(jsonMessage);
    }

    private synchronized String autoGenerateIdentity(){
        String identity = "guest" + identityCount;
        while (IdentityValidator.isIdentityInList(chatManager, identity)){
            identityCount += 1;
            identity = "guest" + identityCount;
        }
        return identity;
    }

    public void handle() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", port));
            alive = true;

            while (alive){
                System.out.println("Wait for client connection request: ");
                Socket soc = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(soc, chatManager, commandFactory);
                serverConnection.start(); // connection is thread

                generateIdentityForNewClient(serverConnection);
                chatManager.addClientConnection(serverConnection);
            }
        } catch (IOException e) {
            alive = false;
            e.printStackTrace();
        }
    }
}
