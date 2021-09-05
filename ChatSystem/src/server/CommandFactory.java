package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server_command.*;

public class CommandFactory {
    private final Gson gson;

    public CommandFactory(){
        this.gson = new Gson();
    }

    /**
     * Convert json message sent by client at server side to command object
     * @param jsonMessage
     * @return
     */
    public ServerCommand convertClientMessageToCommand(String jsonMessage){
        String type = gson.fromJson(jsonMessage, JsonObject.class).get("type").getAsString();
        switch(type){
            case "identitychange":
                return this.gson.fromJson(jsonMessage, IdentityChangeCommand.class);
            case "join":
                return this.gson.fromJson(jsonMessage, JoinCommand.class);
            case "who":
                return this.gson.fromJson(jsonMessage, WhoCommand.class);
            case "list":
                return this.gson.fromJson(jsonMessage, ListCommand.class);
            case "createroom":
                return this.gson.fromJson(jsonMessage, CreateRoomCommand.class);
            case "delete":
                return this.gson.fromJson(jsonMessage, DeleteCommand.class);
            case "message":
                return this.gson.fromJson(jsonMessage, MessageCommand.class);
            case "quit":
                return this.gson.fromJson(jsonMessage, QuitCommand.class);
            default:
                return null;
        }
    }

}
