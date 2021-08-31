package server;

import com.google.gson.Gson;
import client_command.NewIdentityCommand;
import shared.IdentityValidator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {
    public static final int PORT = 6379;
    private static int identityCount = 1;
    private final Gson gson = new Gson();
    private static final ChatManager chatManager = new ChatManager();
    private static final CommandFactory commandFactory = new CommandFactory();
    private boolean alive;
    public static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());


    public static void main(String[] args) {
        LOGGER.info("Listening on port " + PORT);
        new ChatServer().handle();
    }

    private synchronized String generateIdentityForNewClient(ServerConnection serverConnection){
        String newID = autoGenerateIdentity();

        // set serverConnection name to "guest1"
        serverConnection.setName(newID);
        NewIdentityCommand newIdentityCommand = new NewIdentityCommand("", newID);

        // broadcast {"type": "newidentity", "identity": "guest1"} to client
        return gson.toJson(newIdentityCommand);
    }

    private synchronized String autoGenerateIdentity(){
        String identity = "guest" + identityCount;
        identityCount += 1;
        //TODO ?
//        while (IdentityValidator.isIdentityInList(chatManager, identity)){
//            identity = "guest" + identityCount;
//            identityCount += 1;
//        }
        return identity;
    }

    public void handle() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            //todo: localhost must be changed to IP address for multiple clients to connect
            serverSocket.bind(new InetSocketAddress("localhost", PORT));
            alive = true;

            while (alive){
                Socket soc = serverSocket.accept();
                if (soc != null){
                    LOGGER.info("New connection received: " + soc.getRemoteSocketAddress().toString());
                    ServerConnection serverConnection = new ServerConnection(soc, chatManager, commandFactory);

                    //send first msg to client
                    String jsonMessage = generateIdentityForNewClient(serverConnection);
                    chatManager.addClientConnection(serverConnection, jsonMessage);
                    serverConnection.start(); // start thread
                }

            }
        } catch (IOException e) {
            alive = false;
            e.printStackTrace();
        }
    }
}
