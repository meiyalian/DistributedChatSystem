package client_command;

import client.ChatClient;
import com.google.gson.Gson;
import server_command.ReceiveAckCommand;

public class AskAckCommand{
    private final String type = "ack";

    public String executeAckCommand(ChatClient chatClient){
        Gson gson = new Gson();
        ReceiveAckCommand receiveAckCommand = new ReceiveAckCommand();
        return gson.toJson(receiveAckCommand);
    }
}