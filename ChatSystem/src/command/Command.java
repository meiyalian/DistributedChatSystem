package command;

import client.ChatClient;
import com.google.gson.Gson;
import server.ServerConnection;

public class Command {
    private final Gson gson = new Gson();

    /**
     * The detailed implementation is left to the child class to implement
     * @param serverConnection
     */
    public void execute(ServerConnection serverConnection){};

    /**
     * The detailed implementation is left to the child class to implement
     * @param chatClient
     */
    public void execute(ChatClient chatClient){};

    private String convertObjectToJson(){
        return gson.toJson(this);
    }

    public void sendJsonMessage(ServerConnection serverConnection){
        serverConnection.sendMessage(this.convertObjectToJson());
    }
}