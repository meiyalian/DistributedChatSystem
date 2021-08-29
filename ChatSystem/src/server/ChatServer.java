package server;

import com.google.gson.Gson;
import command.NewIdentityCommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ChatServer {
    public static final int port = 6379;
    private int identityCount = 1;
    private final Gson gson = new Gson();
    private static final ChatManager chatManager = new ChatManager();
    private static final CommandFactory commandFactory = new CommandFactory();
    private boolean alive;


    public static void main(String[] args) {
        new ChatServer().handle();
    }

    private void generateIdentityForNewClient(ServerConnection serverConnection){
        String newID = autoGenerateIdentity();
        serverConnection.setName(newID);
        NewIdentityCommand newIdentityCommand = new NewIdentityCommand("", newID);

        String jsonMessage = gson.toJson(newIdentityCommand);
        chatManager.sendToOneClient(jsonMessage, serverConnection);
    }

    private synchronized String autoGenerateIdentity(){
        String identity = "guest" + identityCount;
        while (isIdentityInList(identity)){
            identityCount += 1;
            identity = "guest" + identityCount;
        }
        return identity;
    }

    private synchronized boolean isIdentityInList(String identity){
        ArrayList<ServerConnection> serverConnections = chatManager.getClientConnectionList();
        for (ServerConnection serverConnection: serverConnections){
            if (identity.equals(serverConnection.getName())){
                return true;
            }
        }
        return false;
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
