package command;

import client.ChatClient;
import com.google.gson.Gson;
import server.ChatConnection;

public abstract class Command {
    private String type;
    private final Gson gson = new Gson();

    public abstract void executeCommandOnServer(ChatConnection chatConnection);

    public abstract void executeCommandOnClient(ChatClient chatClient);

    private String convertObjectToJson(){
        return gson.toJson(this);
    }

    public void sendJsonMessage(ChatConnection chatConnection){
        chatConnection.sendMessage(this.convertObjectToJson());
    }
}
