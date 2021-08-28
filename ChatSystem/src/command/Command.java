package command;

import client.ChatClient;
import com.google.gson.Gson;
import server.ServerSideConnection;

public class Command {
    private final Gson gson = new Gson();

    /**
     * The detailed implementation is left to the child class to implement
     * @param serverSideConnection
     */
    public void execute(ServerSideConnection serverSideConnection){};

    /**
     * The detailed implementation is left to the child class to implement
     * @param chatClient
     */
    public void execute(ChatClient chatClient){};

    private String convertObjectToJson(){
        return gson.toJson(this);
    }

    public void sendJsonMessage(ServerSideConnection serverSideConnection){
        serverSideConnection.sendMessage(this.convertObjectToJson());
    }
}