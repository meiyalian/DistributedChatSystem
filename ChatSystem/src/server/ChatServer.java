package server;

import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;


import client_command.NewIdentityCommand;
import shared.IdentityValidator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private final int port;
    private static final int DEFAULT_PORT = 6379;
    private static int identityCount = 1;
    private final Gson gson = new Gson();
    private static final ChatManager chatManager = new ChatManager();
    private static final CommandFactory commandFactory = new CommandFactory();
    private boolean alive;

    public ChatServer(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        int port;
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("p", "port", true, "port");

        try {
            CommandLine commandLine = parser.parse(options, args);
            String portNum = commandLine.getOptionValue("p");

            if (portNum != null){
                port = Integer.parseInt(portNum);
            } else {
                port = DEFAULT_PORT;
            }
            System.out.println("Port number: " + port);
        } catch (ParseException e){
            e.printStackTrace();
            System.out.println("Default port " + DEFAULT_PORT + " will be used.");
            port = DEFAULT_PORT;
        }

        new ChatServer(port).handle();
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
        while (IdentityValidator.isIdentityInList(chatManager, identity)){
            identity = "guest" + identityCount;
            identityCount += 1;
        }
        return identity;
    }

    public void handle() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            alive = true;

            while (alive){
                Socket soc = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(soc, chatManager, commandFactory);
                serverConnection.start(); // connection is thread

                String jsonMessage = generateIdentityForNewClient(serverConnection);
                chatManager.addClientConnection(serverConnection, jsonMessage);
            }
        } catch (IOException e) {
            alive = false;
            e.printStackTrace();
        }
    }
}
